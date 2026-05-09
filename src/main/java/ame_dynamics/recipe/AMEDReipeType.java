package ame_dynamics.recipe;

import org.cyclops.cyclopscore.recipe.type.IInventoryFluid;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeMechanicalDryingBasin;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeMechanicalSqueezer;

import ame_dynamics.recipe.inputCache.MechanicalDryingBasinRecipeCache;
import astral_mekanism.generalrecipe.GeneralRecipeType;
import astral_mekanism.generalrecipe.lookup.cache.recipe.SingleInputGeneralRecipeCache;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.world.Container;

public class AMEDReipeType {
    public static final GeneralRecipeType<Container, RecipeMechanicalSqueezer, SingleInputGeneralRecipeCache.GeneralSingleItem<Container, RecipeMechanicalSqueezer>> MECHANICAL_SQUEEZER = new GeneralRecipeType<>(
            RegistryEntries.RECIPETYPE_MECHANICAL_SQUEEZER,
            type -> new SingleInputGeneralRecipeCache.GeneralSingleItem<>(type,
                    recipe -> IngredientCreatorAccess.item().from(recipe.getInputIngredient()),
                    (stack, recipe) -> recipe.getInputIngredient().test(stack))) {
        @Override
        protected boolean isNotRecipeIncomplete(RecipeMechanicalSqueezer recipe) {
            return true;
        }
    };

    public static final GeneralRecipeType<IInventoryFluid, RecipeMechanicalDryingBasin, MechanicalDryingBasinRecipeCache> MECHANICAL_DRYING_BASIN = new GeneralRecipeType<>(
            RegistryEntries.RECIPETYPE_MECHANICAL_DRYING_BASIN, MechanicalDryingBasinRecipeCache::new) {
        @Override
        protected boolean isNotRecipeIncomplete(RecipeMechanicalDryingBasin recipe) {
            return true;
        }
    };
}
