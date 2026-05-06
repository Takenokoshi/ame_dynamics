package ame_dynamics.jei;

import ame_dynamics.AMEDConstants;
import ame_dynamics.registries.AMEDMachines;
import mekanism.client.jei.CatalystRegistryHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class AMEDJEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return AMEDConstants.rl("jei_plugin");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        CatalystRegistryHelper.register(registry, AMEDJEIRecipeType.MECHANICAL_SQUEEZER,
                AMEDMachines.ESSENTIAL_SQUEEZER);
    }

}
