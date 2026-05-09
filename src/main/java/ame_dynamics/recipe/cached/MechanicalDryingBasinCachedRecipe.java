package ame_dynamics.recipe.cached;

import java.util.function.BooleanSupplier;

import org.cyclops.cyclopscore.recipe.ItemStackFromIngredient;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeMechanicalDryingBasin;

import com.mojang.datafixers.util.Either;

import astral_mekanism.generalrecipe.cachedrecipe.GeneralCachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

public class MechanicalDryingBasinCachedRecipe extends GeneralCachedRecipe<RecipeMechanicalDryingBasin> {

    private final IInputHandler<ItemStack> itemInputHandler;
    private final IInputHandler<FluidStack> fluidInputHandler;
    private final IOutputHandler<ItemStack> itemOutputHandler;
    private final IOutputHandler<FluidStack> fluidOutputHandler;

    private final ItemStackIngredient itemIngredient;
    private final FluidStackIngredient fluidIngredient;
    private ItemStack inputItem = ItemStack.EMPTY;
    private FluidStack inputFluid = FluidStack.EMPTY;
    private final ItemStack outputItem;
    private final FluidStack outputFluid;

    public MechanicalDryingBasinCachedRecipe(RecipeMechanicalDryingBasin recipe, BooleanSupplier recheckAllErrors,
            IInputHandler<ItemStack> itemInputHandler, IInputHandler<FluidStack> fluidInputHandler,
            IOutputHandler<ItemStack> itemOutputHandler, IOutputHandler<FluidStack> fluidOutputHandler) {
        super(recipe, recheckAllErrors);
        this.itemInputHandler = itemInputHandler;
        this.fluidInputHandler = fluidInputHandler;
        this.itemOutputHandler = itemOutputHandler;
        this.fluidOutputHandler = fluidOutputHandler;
        Ingredient ingredient = recipe.getInputIngredient();
        this.itemIngredient = ingredient.isEmpty() ? null : IngredientCreatorAccess.item().from(ingredient);
        FluidStack inputFluid = recipe.getInputFluid();
        this.fluidIngredient = inputFluid.isEmpty() ? null
                : IngredientCreatorAccess.fluid().from(recipe.getInputFluid());
        Either<ItemStack, ItemStackFromIngredient> either = recipe.getOutputItem();
        ItemStack outputItem;
        if (either.left().isPresent()) {
            outputItem = either.left().get();
        } else if (either.right().isPresent()) {
            outputItem = either.right().get().getFirstItemStack();
        } else {
            outputItem = ItemStack.EMPTY;
        }
        this.outputItem = outputItem;
        this.outputFluid = recipe.getOutputFluid();
    }

    @Override
    protected void calculateOperationsThisTick(CachedRecipe.OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (!tracker.shouldContinueChecking()) {
            return;
        }
        if (itemIngredient != null) {
            inputItem = itemInputHandler.getRecipeInput(itemIngredient);
            if (inputItem.isEmpty()) {
                tracker.mismatchedRecipe();
                return;
            }
            itemInputHandler.calculateOperationsCanSupport(tracker, inputItem);
        }
        if (fluidIngredient != null) {
            inputFluid = fluidInputHandler.getRecipeInput(fluidIngredient);
            if (inputFluid.isEmpty()) {
                tracker.mismatchedRecipe();
                return;
            }
            fluidInputHandler.calculateOperationsCanSupport(tracker, inputFluid);
        }
        if (!outputItem.isEmpty()) {
            itemOutputHandler.calculateOperationsCanSupport(tracker, outputItem);
        }
        if (!outputFluid.isEmpty()) {
            fluidOutputHandler.calculateOperationsCanSupport(tracker, outputFluid);
        }
    }

    @Override
    protected void finishProcessing(int processes) {
        if (itemIngredient != null) {
            itemInputHandler.use(inputItem, processes);
        }
        if (fluidIngredient != null) {
            fluidInputHandler.use(inputFluid, processes);
        }
        if (!outputItem.isEmpty()) {
            itemOutputHandler.handleOutput(outputItem, processes);
        }
        if (!outputFluid.isEmpty()) {
            fluidOutputHandler.handleOutput(outputFluid, processes);
        }
    }

    @Override
    public boolean isInputValid() {
        if (itemIngredient == null) {
            if (fluidIngredient == null) {
                return false;
            }
            return itemInputHandler.getInput().isEmpty() && fluidIngredient.test(fluidInputHandler.getInput());
        }
        if (fluidIngredient == null) {
            return fluidInputHandler.getInput().isEmpty() && itemIngredient.test(itemInputHandler.getInput());
        }
        return itemIngredient.test(itemInputHandler.getInput()) && fluidIngredient.test(fluidInputHandler.getInput());
    }

}
