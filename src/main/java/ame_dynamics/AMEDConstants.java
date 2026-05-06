package ame_dynamics;

import net.minecraft.resources.ResourceLocation;

public class AMEDConstants {
    public static final String MODID = "ame_dynamics";
    public static ResourceLocation rl(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
