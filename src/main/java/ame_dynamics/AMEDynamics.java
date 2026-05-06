package ame_dynamics;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import ame_dynamics.config.AMEDConfig;
import ame_dynamics.registries.AMEDMachines;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class AMEDynamics {

    public static final Logger LOGGER = LogUtils.getLogger();

    public AMEDynamics() {
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();
        AMEDConfig.registerConfigs(context);
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup0);
        AMEDMachines.MACHINES.register(modEventBus);
    }

    private void commonSetup0(final FMLCommonSetupEvent event) {
    }
}
