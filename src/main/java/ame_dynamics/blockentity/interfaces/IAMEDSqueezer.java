package ame_dynamics.blockentity.interfaces;

import java.util.List;

import org.cyclops.integrateddynamics.core.recipe.type.RecipeMechanicalSqueezer;
import org.jetbrains.annotations.NotNull;

import ame_dynamics.recipe.AMEDReipeType;
import astral_mekanism.generalrecipe.IUnifiedRecipeTypeProvider;
import astral_mekanism.generalrecipe.lookup.cache.recipe.SingleInputGeneralRecipeCache;
import astral_mekanism.generalrecipe.lookup.cache.recipe.SingleInputGeneralRecipeCache.GeneralSingleItem;
import astral_mekanism.generalrecipe.lookup.handler.IUnifiedSingelRecipeLookupHandler;
import mekanism.api.fluid.IExtendedFluidTank;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public interface IAMEDSqueezer extends
        IUnifiedSingelRecipeLookupHandler<ItemStack, RecipeMechanicalSqueezer, SingleInputGeneralRecipeCache.GeneralSingleItem<Container, RecipeMechanicalSqueezer>> {

    public static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    MachineEnergyContainer<?> getEnergyContainer();

    IExtendedFluidTank getFluidTank();

    double getScaledProgress();

    @Override
    default @NotNull IUnifiedRecipeTypeProvider<RecipeMechanicalSqueezer, GeneralSingleItem<Container, RecipeMechanicalSqueezer>> getRecipeType() {
        return AMEDReipeType.MECHANICAL_SQUEEZER;
    }

}
