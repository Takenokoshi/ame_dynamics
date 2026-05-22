package ame_dynamics.registries;

import java.util.EnumSet;

import com.jerry.mekanism_extras.api.ExtraUpgrade;

import ame_dynamics.AMEDConstants;
import ame_dynamics.AMEDLang;
import ame_dynamics.blockentity.machine.BEAstralDryingBasin;
import ame_dynamics.blockentity.machine.BEAstralSqueezer;
import ame_dynamics.blockentity.machine.BEEnchantedDryingBasin;
import ame_dynamics.blockentity.machine.BEEnchantedSqueezer;
import ame_dynamics.blockentity.machine.BEEssentialDryingBasin;
import ame_dynamics.blockentity.machine.BEEssentialSqueezer;
import ame_dynamics.config.AMEDConfig;
import astral_mekanism.enums.AMEUpgrade;
import astral_mekanism.registration.MachineDeferredRegister;
import astral_mekanism.registration.MachineRegistryObject;
import mekanism.api.Upgrade;
import mekanism.api.math.FloatingLong;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AMEDMachines {

    public static final VoxelShape SHAPE = Shapes.or(
            Block.box(0, 0, 0, 16, 2, 16),

            Block.box(0, 2, 0, 2, 16, 2),
            Block.box(14, 2, 0, 16, 16, 2),
            Block.box(0, 2, 14, 2, 16, 16),
            Block.box(14, 2, 14, 16, 16, 16),

            Block.box(2, 10, 0, 14, 16, 2),
            Block.box(2, 10, 14, 14, 16, 16),
            Block.box(0, 10, 2, 2, 16, 14),
            Block.box(14, 10, 2, 16, 16, 14));

    public static final VoxelShape[] SHAPES = new VoxelShape[] { SHAPE, SHAPE, SHAPE, SHAPE };
    public static final MachineDeferredRegister MACHINES = new MachineDeferredRegister(AMEDConstants.MODID);
    public static final MachineRegistryObject<BEEssentialSqueezer, ?, MekanismTileContainer<BEEssentialSqueezer>, ?> ESSENTIAL_SQUEEZER = MACHINES
            .registerSimple("essential_squeezer",
                    BEEssentialSqueezer::new,
                    BEEssentialSqueezer.class,
                    AMEDLang.MACHINE_DESCRIPTION,
                    builder -> builder
                            .withEnergyConfig(AMEDConfig.usage.essentialSqueezer, AMEDConfig.storage.essentialSqueezer)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.ENERGY, Upgrade.SPEED, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            ExtraUpgrade.STACK)));
    public static final MachineRegistryObject<BEEnchantedSqueezer, ?, MekanismTileContainer<BEEnchantedSqueezer>, ?> ENCHANTED_SQUEEZER = MACHINES
            .registerSimple("enchanted_squeezer",
                    BEEnchantedSqueezer::new,
                    BEEnchantedSqueezer.class,
                    AMEDLang.MACHINE_DESCRIPTION,
                    builder -> builder
                            .withEnergyConfig(() -> AMEDConfig.usage.essentialSqueezer.get().multiply(200),
                                    () -> AMEDConfig.storage.essentialSqueezer.get().multiply(12800))
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.ENERGY, Upgrade.SPEED, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            ExtraUpgrade.STACK)));
    public static final MachineRegistryObject<BEAstralSqueezer, ?, MekanismTileContainer<BEAstralSqueezer>, ?> ASTRAL_SQUEEZER = MACHINES
            .registerSimple("astral_squeezer",
                    BEAstralSqueezer::new,
                    BEAstralSqueezer.class,
                    AMEDLang.MACHINE_DESCRIPTION,
                    builder -> builder
                            .withEnergyConfig(() -> AMEDConfig.usage.essentialSqueezer.get().multiply(200))
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.ENERGY, AMEUpgrade.COBBLESTONE_SUPPLY.getValue())));
    public static final MachineRegistryObject<BEEssentialDryingBasin, ?, MekanismTileContainer<BEEssentialDryingBasin>, ?> ESSENTIAL_DRYING_BASIN = MACHINES
            .registerSimple("essential_drying_basin",
                    BEEssentialDryingBasin::new,
                    BEEssentialDryingBasin.class,
                    AMEDLang.MACHINE_DESCRIPTION,
                    builder -> builder
                            .withCustomShape(SHAPES)
                            .withEnergyConfig(AMEDConfig.usage.essentialDryingBasin,
                                    AMEDConfig.storage.essentialDryingBasin)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.ENERGY, Upgrade.SPEED, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            AMEUpgrade.WATER_SUPPLY.getValue(),
                                            ExtraUpgrade.STACK)));
    public static final MachineRegistryObject<BEEnchantedDryingBasin, ?, MekanismTileContainer<BEEnchantedDryingBasin>, ?> ENCHANTED_DRYING_BASIN = MACHINES
            .registerSimple("enchanted_drying_basin",
                    BEEnchantedDryingBasin::new,
                    BEEnchantedDryingBasin.class,
                    AMEDLang.MACHINE_DESCRIPTION,
                    builder -> builder
                            .withCustomShape(SHAPES)
                            .withEnergyConfig(() -> AMEDConfig.usage.essentialDryingBasin.get().multiply(200),
                                    () -> AMEDConfig.storage.essentialDryingBasin.get().multiply(400))
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.ENERGY, Upgrade.SPEED, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            AMEUpgrade.WATER_SUPPLY.getValue(),
                                            ExtraUpgrade.STACK)));
    public static final MachineRegistryObject<BEAstralDryingBasin, ?, MekanismTileContainer<BEAstralDryingBasin>, ?> ASTRAL_DRYING_BASIN = MACHINES
            .registerSimple("astral_drying_basin",
                    BEAstralDryingBasin::new,
                    BEAstralDryingBasin.class,
                    AMEDLang.MACHINE_DESCRIPTION,
                    builder -> builder
                            .withCustomShape(SHAPES)
                            .withEnergyConfig(() -> AMEDConfig.usage.essentialDryingBasin.get().multiply(200),
                                    () -> FloatingLong.MAX_VALUE)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.ENERGY,
                                            AMEUpgrade.WATER_SUPPLY.getValue(),
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue())));
}
