package ame_dynamics.jei;

import org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicaldryingbasin.MechanicalDryingBasinRecipeCategory;
import org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicalsqueezer.MechanicalSqueezerRecipeCategory;
import org.jetbrains.annotations.Nullable;

import ame_dynamics.AMEDConstants;
import ame_dynamics.registries.AMEDMachines;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class AMEDJEIPlugin implements IModPlugin {

    private static IJeiRuntime runtime;
    private static IRecipesGui recipesGui;
    @Override
    public ResourceLocation getPluginUid() {
        return AMEDConstants.rl("jei_plugin");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalysts(MechanicalSqueezerRecipeCategory.TYPE,
            AMEDMachines.ESSENTIAL_SQUEEZER,
            AMEDMachines.ENCHANTED_SQUEEZER,
            AMEDMachines.ASTRAL_SQUEEZER);
        registry.addRecipeCatalysts(MechanicalDryingBasinRecipeCategory.TYPE,
            AMEDMachines.ESSENTIAL_DRYING_BASIN,
            AMEDMachines.ENCHANTED_DRYING_BASIN,
            AMEDMachines.ASTRAL_DRYING_BASIN);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
        recipesGui = runtime.getRecipesGui();
    }

    public static @Nullable IJeiRuntime getRuntime() {
        return runtime;
    }

    public static @Nullable IRecipesGui getRecipesGui() {
        return recipesGui;
    }

}
