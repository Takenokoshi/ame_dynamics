package ame_dynamics.jei;

import org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicaldryingbasin.MechanicalDryingBasinRecipeCategory;
import org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicaldryingbasin.MechanicalDryingBasinRecipeJEI;
import org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicalsqueezer.MechanicalSqueezerRecipeCategory;
import org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicalsqueezer.MechanicalSqueezerRecipeJEI;

import mekanism.client.jei.MekanismJEIRecipeType;

public class AMEDJEIRecipeType {
    public static final MekanismJEIRecipeType<MechanicalSqueezerRecipeJEI> MECHANICAL_SQUEEZER = new MekanismJEIRecipeType<>(
            MechanicalSqueezerRecipeCategory.TYPE.getUid(), MechanicalSqueezerRecipeJEI.class);
    public static final MekanismJEIRecipeType<MechanicalDryingBasinRecipeJEI> MECHANICAL_DRYING_BASIN = new MekanismJEIRecipeType<>(
            MechanicalDryingBasinRecipeCategory.TYPE.getUid(), MechanicalDryingBasinRecipeJEI.class);
}
