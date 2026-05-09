package ame_dynamics.mixin;

import org.cyclops.integrateddynamics.core.recipe.type.RecipeSqueezer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.SoftOverride;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;

@Mixin(value = RecipeSqueezer.class, remap = false)
public class RecipeSqueezerMixin extends RecipeMixin {

    @Override
    @SoftOverride
    protected NonNullList<Ingredient> ame_dynamics$getIngredientsModify(NonNullList<Ingredient> original) {
        return NonNullList.of(((RecipeSqueezer) (Object) this).getInputIngredient());
    }
}
