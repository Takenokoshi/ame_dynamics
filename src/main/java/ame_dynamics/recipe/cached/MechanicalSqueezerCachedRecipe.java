package ame_dynamics.recipe.cached;

import java.util.function.BooleanSupplier;

import org.cyclops.integrateddynamics.core.recipe.type.RecipeMechanicalSqueezer;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeSqueezer.IngredientChance;

import ame_dynamics.recipe.output.IngrediencChanceOutputHandler;
import astral_mekanism.generalrecipe.cachedrecipe.GeneralCachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class MechanicalSqueezerCachedRecipe extends GeneralCachedRecipe<RecipeMechanicalSqueezer> {

    private final IInputHandler<ItemStack> inputHandler;
    private final IngrediencChanceOutputHandler firstOutputHandler;
    private final IngrediencChanceOutputHandler secondOutputHandler;
    private final IngrediencChanceOutputHandler thirdOutputHandler;
    private final IOutputHandler<FluidStack> fluidOutputHandler;

    private ItemStack recipeInput = ItemStack.EMPTY;
    private IngredientChance firstOutput;
    private IngredientChance secondOutput;
    private IngredientChance thirdOutput;
    private FluidStack fluidOutput = FluidStack.EMPTY;

    public MechanicalSqueezerCachedRecipe(RecipeMechanicalSqueezer recipe, BooleanSupplier recheckAllErrors,
            IInputHandler<ItemStack> inputHandler, IngrediencChanceOutputHandler firstOutputHandler,
            IngrediencChanceOutputHandler secondOutputHandler, IngrediencChanceOutputHandler thirdOutputHandler,
            IOutputHandler<FluidStack> fluidOutputHandler) {
        super(recipe, recheckAllErrors);
        this.inputHandler = inputHandler;
        this.firstOutputHandler = firstOutputHandler;
        this.secondOutputHandler = secondOutputHandler;
        this.thirdOutputHandler = thirdOutputHandler;
        this.fluidOutputHandler = fluidOutputHandler;
    }

    @Override
    protected void calculateOperationsThisTick(CachedRecipe.OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (!tracker.shouldContinueChecking()) {
            return;
        }
        recipeInput = inputHandler.getRecipeInput(IngredientCreatorAccess.item().from(recipe.getInputIngredient()));
        NonNullList<IngredientChance> chances = recipe.getOutputItems();
        firstOutput = chances.size() < 1 ? null : chances.get(0);
        secondOutput = chances.size() < 2 ? null : chances.get(1);
        thirdOutput = chances.size() < 3 ? null : chances.get(2);
        fluidOutput = recipe.getOutputFluid();
        inputHandler.calculateOperationsCanSupport(tracker, recipeInput);
        firstOutputHandler.calculateOperationsCanSupport(tracker, firstOutput);
        secondOutputHandler.calculateOperationsCanSupport(tracker, secondOutput);
        thirdOutputHandler.calculateOperationsCanSupport(tracker, thirdOutput);
        fluidOutputHandler.calculateOperationsCanSupport(tracker, fluidOutput);
    }

    @Override
    protected void finishProcessing(int processes) {
        inputHandler.use(recipeInput, processes);
        firstOutputHandler.handleOutput(firstOutput, processes);
        secondOutputHandler.handleOutput(secondOutput, processes);
        thirdOutputHandler.handleOutput(thirdOutput, processes);
        fluidOutputHandler.handleOutput(fluidOutput, processes);
    }

    @Override
    public boolean isInputValid() {
        return recipe.getInputIngredient().test(inputHandler.getInput());
    }

}
