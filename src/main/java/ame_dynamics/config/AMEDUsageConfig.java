package ame_dynamics.config;

import mekanism.api.math.FloatingLong;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedFloatingLongValue;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class AMEDUsageConfig extends BaseMekanismConfig {

    private final ForgeConfigSpec configSpec;
    public final CachedFloatingLongValue essentialSqueezer;
    public final CachedFloatingLongValue essentialDryingBasin;

    AMEDUsageConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Machine Energy Usage Config. This config is synced from server to client.").push("usage");
        essentialSqueezer = CachedFloatingLongValue.define(this, builder, "Energy per operation tick (Joules).",
                "essentialSqueezer", FloatingLong.create(50));
        essentialDryingBasin = CachedFloatingLongValue.define(this, builder, "Energy per operation tick (Joules).",
                "essentialDryingBasin", FloatingLong.create(50));
        builder.pop();
        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "ame-dynamycs-usage";
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
