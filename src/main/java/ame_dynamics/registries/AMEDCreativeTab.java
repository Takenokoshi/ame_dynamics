package ame_dynamics.registries;

import ame_dynamics.AMEDConstants;
import ame_dynamics.AMEDLang;
import mekanism.common.registration.impl.CreativeTabDeferredRegister;
import mekanism.common.registration.impl.CreativeTabRegistryObject;

public class AMEDCreativeTab {
    public static final CreativeTabDeferredRegister CREATIVE_TABS = new CreativeTabDeferredRegister(
            AMEDConstants.MODID);

    public static final CreativeTabRegistryObject AMED_TAB = CREATIVE_TABS.register("ame_dynamics_tab",
            AMEDLang.CREATIVE_TAB, AMEDMachines.ASTRAL_SQUEEZER, builder -> builder
                    .displayItems((displayParameters, output) -> {
                        CreativeTabDeferredRegister.addToDisplay(AMEDMachines.MACHINES.blockRegister, output);
                    }));
}
