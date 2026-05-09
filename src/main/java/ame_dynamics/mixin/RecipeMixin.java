package ame_dynamics.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

@Mixin(value = Recipe.class, remap = true)
public class RecipeMixin {

    @ModifyReturnValue(method = "getIngredients", at = @At("RETURN"))
    protected NonNullList<Ingredient> ame_dynamics$getIngredientsModify(NonNullList<Ingredient> original) {
        return original;
    }
}
