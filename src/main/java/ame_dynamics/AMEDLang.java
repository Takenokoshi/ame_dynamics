package ame_dynamics;

import mekanism.api.text.ILangEntry;
import net.minecraft.Util;

public class AMEDLang implements ILangEntry {

    private final String key;

    private AMEDLang(String type, String Path) {
        this(Util.makeDescriptionId(type, AMEDConstants.rl(Path)));
    }

    private AMEDLang(String key) {
        this.key = key;
    }

    @Override
    public String getTranslationKey() {
        return key;
    }

    public static final AMEDLang MACHINE_DESCRIPTION = new AMEDLang("description", "machine");
    public static final AMEDLang CREATIVE_TAB = new AMEDLang("tab", "tab_name");

}
