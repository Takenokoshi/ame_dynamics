package ame_dynamics;

import ame_dynamics.blockentity.machine.BEEssentialSqueezer;
import ame_dynamics.gui.GuiAMEDSqueezer;
import ame_dynamics.registries.AMEDMachines;
import astral_mekanism.registration.MachineRegistryObject;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.MenuScreens.ScreenConstructor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class AMEDClient extends AMEDynamics {

    private static AMEDClient INSTANCE;

    public AMEDClient() {
        super();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        INSTANCE = this;
        eventBus.addListener(this::clientSetup);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            @SuppressWarnings("unused")
            Minecraft minecraft = Minecraft.getInstance();
            initScreens();
        });
    }

    private static void initScreens() {
        registerScreenMek(AMEDMachines.ESSENTIAL_SQUEEZER, GuiAMEDSqueezer<BEEssentialSqueezer>::new);
    }

    private static <BE extends TileEntityMekanism, CONTAINER extends MekanismTileContainer<BE>, U extends Screen & MenuAccess<CONTAINER>> void registerScreenMek(
            MachineRegistryObject<BE, ?, ? extends CONTAINER, ?> registryObject,
            ScreenConstructor<CONTAINER, U> constructor) {
        MenuScreens.register(registryObject.getContainer().get(), constructor);
    }
}
