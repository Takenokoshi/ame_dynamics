package ame_dynamics.registries;

import java.util.EnumSet;

import com.jerry.mekanism_extras.api.ExtraUpgrade;

import ame_dynamics.AMEDConstants;
import ame_dynamics.AMEDLang;
import ame_dynamics.blockentity.machine.BEEssentialSqueezer;
import ame_dynamics.config.AMEDConfig;
import astral_mekanism.enums.AMEUpgrade;
import astral_mekanism.registration.BlockTypeMachine;
import astral_mekanism.registration.MachineDeferredRegister;
import astral_mekanism.registration.MachineRegistryObject;
import mekanism.api.Upgrade;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.item.block.machine.ItemBlockMachine;

public class AMEDMachines {
    public static final MachineDeferredRegister MACHINES = new MachineDeferredRegister(AMEDConstants.MODID);
    public static final MachineRegistryObject<BEEssentialSqueezer, BlockTileModel<BEEssentialSqueezer, BlockTypeMachine<BEEssentialSqueezer>>, MekanismTileContainer<BEEssentialSqueezer>, ItemBlockMachine> ESSENTIAL_SQUEEZER = MACHINES
            .registerSimple("essential_squeezer",
                    BEEssentialSqueezer::new,
                    BEEssentialSqueezer.class,
                    AMEDLang.MACHINE_DESCRIPTION,
                    builder -> builder
                            .withEnergyConfig(AMEDConfig.usage.essentialSqueezer, AMEDConfig.storage.essentialSqueezer)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.ENERGY, Upgrade.SPEED, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            ExtraUpgrade.STACK)));
}
