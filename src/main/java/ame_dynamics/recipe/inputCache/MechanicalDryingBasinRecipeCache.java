package ame_dynamics.recipe.inputCache;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.cyclops.cyclopscore.recipe.type.IInventoryFluid;
import org.cyclops.integrateddynamics.core.recipe.type.RecipeMechanicalDryingBasin;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.generalrecipe.IUnifiedRecipeType;
import astral_mekanism.generalrecipe.lookup.cache.recipe.GeneralInputRecipeCache;
import astral_mekanism.generalrecipe.lookup.cache.type.ItemGeneralInputCache;
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

public class MechanicalDryingBasinRecipeCache
        extends GeneralInputRecipeCache<IInventoryFluid, RecipeMechanicalDryingBasin> {

    private final Map<Fluid, List<RecipeMechanicalDryingBasin>> fluidRecipeMap = new LinkedHashMap<>();
    private final ItemGeneralInputCache<RecipeMechanicalDryingBasin> itemCache = new ItemGeneralInputCache<>();
    private final List<RecipeMechanicalDryingBasin> emptyItemRecipes = new ArrayList<>();

    public MechanicalDryingBasinRecipeCache(IUnifiedRecipeType<RecipeMechanicalDryingBasin, ?> recipeType) {
        super(recipeType);
    }

    public void clear() {
        super.clear();
        fluidRecipeMap.clear();
        itemCache.clear();
        emptyItemRecipes.clear();
    }

    @Override
    protected void initCache(List<RecipeMechanicalDryingBasin> recipes) {
        IItemStackIngredientCreator creator = IngredientCreatorAccess.item();
        for (RecipeMechanicalDryingBasin recipe : recipes) {
            Ingredient ingredient = recipe.getInputIngredient();
            if (ingredient.isEmpty()) {
                emptyItemRecipes.add(recipe);
            } else {
                itemCache.mapInputs(recipe, creator.from(ingredient));
            }
            FluidStack fluidStack = recipe.getInputFluid();
            Fluid fluid = fluidStack.isEmpty() ? Fluids.EMPTY : fluidStack.getFluid();
            if (!fluidRecipeMap.containsKey(fluid)) {
                fluidRecipeMap.put(fluid, new ArrayList<>());
            }
            fluidRecipeMap.get(fluid).add(recipe);
        }
    }

    public boolean containsInputItem(Level world, ItemStack itemStack) {
        initCacheIfNeeded(world);
        return itemStack.isEmpty() ? false : itemCache.contains(itemStack);
    }

    public boolean containsInputFluid(Level world, FluidStack fluidStack) {
        initCacheIfNeeded(world);
        return fluidStack.isEmpty() ? false : fluidRecipeMap.containsKey(fluidStack.getFluid());
    }

    public boolean containsInputItemFluid(Level world, ItemStack itemStack, FluidStack fluidStack) {
        if (itemStack.isEmpty()) {
            return false;
        }
        if (fluidStack.isEmpty()) {
            return containsInputItem(world, itemStack);
        }
        initCacheIfNeeded(world);
        Fluid fluid = fluidStack.getFluid();
        if (fluidRecipeMap.containsKey(fluid)) {
            return fluidRecipeMap.get(fluid).stream().filter(r -> r.getInputIngredient().test(itemStack)).findFirst()
                    .isPresent();
        }
        return false;
    }

    public boolean containsInputFluidItem(Level world, ItemStack itemStack, FluidStack fluidStack) {
        if (fluidStack.isEmpty()) {
            return false;
        }
        if (itemStack.isEmpty()) {
            return containsInputFluid(world, fluidStack);
        }
        initCacheIfNeeded(world);
        return itemCache.contains(itemStack, r -> r.getInputFluid().isFluidEqual(fluidStack));
    }

    @Nullable
    public RecipeMechanicalDryingBasin findFirstRecipe(Level world, ItemStack itemStack, FluidStack fluidStack) {
        initCacheIfNeeded(world);
        if (itemStack.isEmpty()) {
            return emptyItemRecipes.stream().filter(r -> r.getInputFluid().isFluidEqual(fluidStack)).findFirst()
                    .orElse(null);
        }
        Fluid fluid = fluidStack.isEmpty() ? Fluids.EMPTY : fluidStack.getFluid();
        if (fluidRecipeMap.containsKey(fluid)) {
            return fluidRecipeMap.get(fluid).stream().filter(r -> r.getInputIngredient().test(itemStack)).findFirst()
                    .orElse(null);
        }
        return null;
    }

}
