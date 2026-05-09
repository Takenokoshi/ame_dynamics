package ame_dynamics.blockentity.interfaces;

import java.util.List;

import org.cyclops.integrateddynamics.core.recipe.type.RecipeMechanicalDryingBasin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ame_dynamics.recipe.AMEDReipeType;
import ame_dynamics.recipe.inputCache.MechanicalDryingBasinRecipeCache;
import astral_mekanism.generalrecipe.IUnifiedRecipeTypeProvider;
import astral_mekanism.generalrecipe.lookup.handler.IUnifiedRecipeTypedLookupHandler;
import mekanism.api.fluid.IExtendedFluidTank;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IAMEDDryingBasin
        extends IUnifiedRecipeTypedLookupHandler<RecipeMechanicalDryingBasin, MechanicalDryingBasinRecipeCache> {

    public static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_SECONDARY_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE);

    @Override
    default @NotNull IUnifiedRecipeTypeProvider<RecipeMechanicalDryingBasin, MechanicalDryingBasinRecipeCache> getRecipeType() {
        return AMEDReipeType.MECHANICAL_DRYING_BASIN;
    }

    default boolean containsInputItem(ItemStack itemStack) {
        return getRecipeType().getInputCache().containsInputItem(getHandlerWorld(), itemStack);
    }

    default boolean containsInputFluid(FluidStack fluidStack) {
        return getRecipeType().getInputCache().containsInputFluid(getHandlerWorld(), fluidStack);
    }

    default boolean containsInputItemFluid(ItemStack itemStack, FluidStack fluidStack) {
        return getRecipeType().getInputCache().containsInputItemFluid(getHandlerWorld(), itemStack, fluidStack);
    }

    default boolean containsInputFluidItem(ItemStack itemStack, FluidStack fluidStack) {
        return getRecipeType().getInputCache().containsInputFluidItem(getHandlerWorld(), itemStack, fluidStack);
    }

    @Nullable
    default RecipeMechanicalDryingBasin findFirstRecipe(ItemStack itemStack, FluidStack fluidStack) {
        return getRecipeType().getInputCache().findFirstRecipe(getHandlerWorld(), itemStack, fluidStack);
    }

    @Nullable
    default RecipeMechanicalDryingBasin findFirstRecipe(IInputHandler<ItemStack> item,
            IInputHandler<FluidStack> fluid) {
        return findFirstRecipe(item.getInput(), fluid.getInput());
    }

    IExtendedFluidTank getInputTank();

    IExtendedFluidTank getOutputTank();

    MachineEnergyContainer<?> getEnergyContainer();

    double getScaledProgress();

}
