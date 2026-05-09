package ame_dynamics.config;

import mekanism.api.math.FloatingLong;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedFloatingLongValue;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class AMEDStorageConfig extends BaseMekanismConfig {

    private final ForgeConfigSpec configSpec;
    public final CachedFloatingLongValue essentialSqueezer;
    public final CachedFloatingLongValue essentialDryingBasin;

    AMEDStorageConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Machine Energy Usage Config. This config is synced from server to client.").push("usage");
        essentialSqueezer = CachedFloatingLongValue.define(this, builder, "Base energy storage (Joules).",
                "essentialSqueezer", FloatingLong.createConst(250000));
        essentialDryingBasin = CachedFloatingLongValue.define(this, builder, "Base energy storage (Joules).",
                "essentialDryingBasin", FloatingLong.createConst(250000));
        builder.pop();
        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "ame-dynamycs-storage";
    }

    @Override
    public ForgeConfigSpec getConfigSpec() {
        return configSpec;
    }

    @Override
    public Type getConfigType() {
        return Type.SERVER;
    }

    @Override
    public boolean addToContainer() {
        return false;
    }

}
