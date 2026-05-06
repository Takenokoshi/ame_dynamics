package ame_dynamics.recipe.output;

import java.util.Random;

import org.cyclops.integrateddynamics.core.recipe.type.RecipeSqueezer;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeSqueezer.IngredientChance;

import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.math.MathUtils;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;

public class IngrediencChanceOutputHandler implements IOutputHandler<RecipeSqueezer.IngredientChance> {

    private static final Random rng = new Random();

    private final IInventorySlot slot;
    private final RecipeError notEnoughSpaceError;

    public IngrediencChanceOutputHandler(IInventorySlot slot, RecipeError notEnoughSpaceError) {
        this.slot = slot;
        this.notEnoughSpaceError = notEnoughSpaceError;

    }

    @Override
    public void handleOutput(IngredientChance toOutput, int operations) {
        if (toOutput == null) {
            return;
        }
        ItemStack stack = toOutput.getIngredientFirst();
        slot.insertItem(
                stack.copyWithCount(MathUtils
                        .clampToInt(toOutput.getChance() * operations * stack.getCount() + rng.nextFloat(0, 1))),
                Action.EXECUTE, AutomationType.INTERNAL);
    }

    @Override
    public void calculateOperationsCanSupport(OperationTracker tracker, IngredientChance toOutput) {
        if (toOutput == null) {
            return;
        }
        ItemStack stack = toOutput.getIngredientFirst();
        if (slot.isEmpty() || ItemStack.isSameItemSameTags(slot.getStack(), stack)) {
            int v = (slot.getLimit(stack) - slot.getCount()) / stack.getCount();
            if (v > 1) {
                tracker.updateOperations(MathUtils.clampToInt((v - 1) / toOutput.getChance()));
                return;
            }
        }
        tracker.resetProgress(notEnoughSpaceError);
    }

}
