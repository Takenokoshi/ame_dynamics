package ame_dynamics.blockentity.machine;

import java.util.function.Predicate;

import com.jerry.mekanism_extras.api.ExtraUpgrade;

import astral_mekanism.integration.AMEEmpowered;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.Upgrade;
import mekanism.api.math.MathUtils;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.fluid.VariableCapacityFluidTank;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableInt;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class BEEnchantedDryingBasin extends BETickWorkDryingBasin {
    private int baselineMaxOperations = 200;
    private int inputTankCapacity = 200 * 5000;

    public BEEnchantedDryingBasin(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected BasicFluidTank createInputTank(Predicate<FluidStack> canInsert, Predicate<FluidStack> validator,
            IContentsListener listener) {
        return VariableCapacityFluidTank.create(this::getInputTankCapacity,
                (f, t) -> t == AutomationType.MANUAL,
                (f, t) -> canInsert.test(f), validator, listener);
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (upgrade == Upgrade.SPEED || AMEEmpowered.isEmpoweredSpeed(upgrade) || upgrade == ExtraUpgrade.STACK) {
            baselineMaxOperations = 200
                    * ((1 << upgradeComponent.getUpgrades(Upgrade.SPEED)) + 2 << AMEEmpowered.getEmpoweredSpeeds(this))
                    * (1 << upgradeComponent.getUpgrades(ExtraUpgrade.STACK));
            inputTankCapacity = MathUtils.clampToInt(5000l * baselineMaxOperations);
        }
    }

    @Override
    protected int getBaselineMaxOperations() {
        return baselineMaxOperations;
    }

    private int getInputTankCapacity() {
        return inputTankCapacity;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableInt.create(this::getBaselineMaxOperations, (v) -> this.baselineMaxOperations = v));
        container.track(SyncableInt.create(this::getInputTankCapacity, (v) -> this.inputTankCapacity = v));
    }

}
