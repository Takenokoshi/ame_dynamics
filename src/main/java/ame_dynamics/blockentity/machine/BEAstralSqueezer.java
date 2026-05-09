package ame_dynamics.blockentity.machine;

import java.util.List;

import org.cyclops.integrateddynamics.core.recipe.type.RecipeMechanicalSqueezer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ame_dynamics.blockentity.interfaces.IAMEDSqueezer;
import ame_dynamics.recipe.cached.MechanicalSqueezerCachedRecipe;
import ame_dynamics.recipe.output.IngrediencChanceOutputHandler;
import astral_mekanism.block.blockentity.base.BlockEntityRecipeMachine;
import astral_mekanism.generalrecipe.cachedrecipe.ICachedRecipe;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.fluid.IExtendedFluidTank;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class BEAstralSqueezer extends BlockEntityRecipeMachine<RecipeMechanicalSqueezer> implements IAMEDSqueezer {

    private InputInventorySlot inputSlot;
    private EnergyInventorySlot energySlot;
    private OutputInventorySlot firstOutputSlot;
    private OutputInventorySlot secondOutputSlot;
    private OutputInventorySlot thirdOutputSlot;
    private BasicFluidTank fluidTank;
    private MachineEnergyContainer<BEAstralSqueezer> energyContainer;

    private final IInputHandler<ItemStack> inputHandler;
    private final IngrediencChanceOutputHandler firstOutputHandler;
    private final IngrediencChanceOutputHandler secondOutputHandler;
    private final IngrediencChanceOutputHandler thirdOutputHandler;
    private final IOutputHandler<FluidStack> outputHandler;

    public BEAstralSqueezer(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.FLUID,
                TransmissionType.ENERGY);
        configComponent.setupItemIOConfig(List.<IInventorySlot>of(inputSlot),
                List.<IInventorySlot>of(firstOutputSlot, secondOutputSlot, thirdOutputSlot), energySlot, false);
        configComponent.setupOutputConfig(TransmissionType.FLUID, fluidTank, RelativeSide.values());
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ejectorComponent = new TileComponentEjector(this, () -> 0, () -> 0x7fffffff).setOutputData(configComponent,
                TransmissionType.ITEM, TransmissionType.FLUID);
        this.inputHandler = InputHelper.getInputHandler(inputSlot, RecipeError.NOT_ENOUGH_INPUT);
        this.firstOutputHandler = new IngrediencChanceOutputHandler(firstOutputSlot,
                RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
        this.secondOutputHandler = new IngrediencChanceOutputHandler(secondOutputSlot,
                RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
        this.thirdOutputHandler = new IngrediencChanceOutputHandler(thirdOutputSlot,
                RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
        this.outputHandler = OutputHelper.getOutputHandler(fluidTank, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(inputSlot = InputInventorySlot.at(this::containsRecipe, recipeCacheListener, 64, 17));
        builder.addSlot(firstOutputSlot = OutputInventorySlot.at(listener, 116, 17));
        builder.addSlot(secondOutputSlot = OutputInventorySlot.at(listener, 116, 35));
        builder.addSlot(thirdOutputSlot = OutputInventorySlot.at(listener, 116, 53));
        builder.addSlot(
                energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 64, 53));
        return builder.build();
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(fluidTank = BasicFluidTank.output(0x7fffffff, listener));
        return builder.build();
    }

    protected void onUpdateServer() {
        super.onUpdateServer();
        recipeCacheLookupMonitor.updateAndProcess();
    }

    @Override
    public @NotNull ICachedRecipe<RecipeMechanicalSqueezer> createNewCachedRecipe(
            @NotNull RecipeMechanicalSqueezer recipe, int cacheIndex) {
        return new MechanicalSqueezerCachedRecipe(recipe, recheckAllRecipeErrors, inputHandler, firstOutputHandler,
                secondOutputHandler, thirdOutputHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setBaselineMaxOperations(() -> 0x7fffffff)
                .setOnFinish(this::markForSave);
    }

    @Override
    public @Nullable RecipeMechanicalSqueezer getRecipe(int arg0) {
        return findFirstRecipe(inputHandler);
    }

    @Override
    public MachineEnergyContainer<?> getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public IExtendedFluidTank getFluidTank() {
        return fluidTank;
    }

    @Override
    public double getScaledProgress() {
        return getActive() ? 1 : 0;
    }
}
