package com.jeffyjamzhd.btj.api.extend;

import com.jeffyjamzhd.btj.api.CurseDisplayManager;
import net.minecraft.src.FontRenderer;

public interface IGuiIngame {
    /**
     * Gets curse display manager instance
     * @return CurseDisplayManager
     */
    default CurseDisplayManager btj$getCurseDisplay() {
        return null;
    }

    /**
     * Gets the FontRender from the GUI
     * @return FontRenderer
     */
    default FontRenderer btj$getFontRenderer() {
        return null;
    }
}
