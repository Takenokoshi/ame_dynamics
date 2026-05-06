package ame_dynamics.config;

import ame_dynamics.AMEDConstants;
import mekanism.common.config.MekanismConfigHelper;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AMEDConstants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AMEDConfig {
    public static final AMEDUsageConfig usage = new AMEDUsageConfig();
    public static final AMEDStorageConfig storage = new AMEDStorageConfig();

    public static void registerConfigs(ModLoadingContext modLoadingContext) {
        ModContainer modContainer = modLoadingContext.getActiveContainer();
        MekanismConfigHelper.registerConfig(modContainer, storage);
        MekanismConfigHelper.registerConfig(modContainer, usage);
    }
}
