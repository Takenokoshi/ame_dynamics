package ame_dynamics.blockentity.machine;

import java.util.function.Predicate;

import org.cyclops.integrateddynamics.core.recipe.type.RecipeMechanicalDryingBasin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ame_dynamics.blockentity.interfaces.IAMEDDryingBasin;
import ame_dynamics.recipe.cached.MechanicalDryingBasinCachedRecipe;
import astral_mekanism.block.blockentity.base.BlockEntityRecipeMachine;
import astral_mekanism.block.blockentity.elements.ExtendedComponentEjector;
import astral_mekanism.generalrecipe.cachedrecipe.ICachedRecipe;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.fluid.IExtendedFluidTank;
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
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public abstract class BETickWorkDryingBasin extends BlockEntityRecipeMachine<RecipeMechanicalDryingBasin>
        implements IAMEDDryingBasin {

    private InputInventorySlot inputSlot;
    private OutputInventorySlot outputSlot;

    private BasicFluidTank inputTank;
    private BasicFluidTank outputTank;

    private MachineEnergyContainer<?> energyContainer;
    private EnergyInventorySlot energySlot;

    private final IInputHandler<ItemStack> itemInputHandler;
    private final IInputHandler<FluidStack> fluidInputHandler;
    private final IOutputHandler<ItemStack> itemOutputHandler;
    private final IOutputHandler<FluidStack> fluidOutputHandler;

    protected BETickWorkDryingBasin(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.FLUID,
                TransmissionType.ENERGY);
        configComponent.setupItemIOConfig(inputSlot, outputSlot, energySlot);
        configComponent.setupIOConfig(TransmissionType.FLUID, inputTank, outputTank, RelativeSide.RIGHT, false);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ejectorComponent = new ExtendedComponentEjector(this, () -> 0x7fffffff)
                .setOutputData(configComponent, TransmissionType.ITEM, TransmissionType.FLUID)
                .setCanFluidTankEject((tank, type) -> tank == outputTank && type.canOutput());
        this.itemInputHandler = InputHelper.getInputHandler(inputSlot, RecipeError.NOT_ENOUGH_INPUT);
        this.fluidInputHandler = InputHelper.getInputHandler(inputTank, RecipeError.NOT_ENOUGH_SECONDARY_INPUT);
        this.itemOutputHandler = OutputHelper.getOutputHandler(outputSlot, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
        this.fluidOutputHandler = OutputHelper.getOutputHandler(outputTank, RecipeError.NOT_ENOUGH_SECONDARY_INPUT);
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
        builder.addSlot(inputSlot = InputInventorySlot.at(this::containsInputItem, recipeCacheListener, 64, 17));
        builder.addSlot(outputSlot = OutputInventorySlot.at(listener, 116, 35));
        builder.addSlot(
                energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 64, 53));
        return builder.build();
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(
                inputTank = createInputTank(
                        stack -> containsInputFluidItem(inputSlot.getStack(), stack),
                        this::containsInputFluid, recipeCacheListener));
        builder.addTank(outputTank = BasicFluidTank.output(0x7fffffff, listener));
        return builder.build();
    }

    protected abstract BasicFluidTank createInputTank(Predicate<FluidStack> canInsert, Predicate<FluidStack> validator,
            IContentsListener listener);

    protected void onUpdateServer() {
        super.onUpdateServer();
        recipeCacheLookupMonitor.updateAndProcess();

    }

    @Override
    public @NotNull ICachedRecipe<RecipeMechanicalDryingBasin> createNewCachedRecipe(
            @NotNull RecipeMechanicalDryingBasin recipe, int cacheIndex) {
        return new MechanicalDryingBasinCachedRecipe(recipe, recheckAllRecipeErrors, itemInputHandler,
                fluidInputHandler, itemOutputHandler, fluidOutputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setBaselineMaxOperations(this::getBaselineMaxOperations)
                .setOnFinish(this::markForSave);
    }

    protected abstract int getBaselineMaxOperations();

    @Override
    public @Nullable RecipeMechanicalDryingBasin getRecipe(int cacheIndex) {
        return findFirstRecipe(itemInputHandler, fluidInputHandler);
    }

    @Override
    public IExtendedFluidTank getInputTank() {
        return inputTank;
    }

    @Override
    public IExtendedFluidTank getOutputTank() {
        return outputTank;
    }

    @Override
    public MachineEnergyContainer<?> getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public double getScaledProgress() {
        return getActive() ? 1 : 0;
    }

}
