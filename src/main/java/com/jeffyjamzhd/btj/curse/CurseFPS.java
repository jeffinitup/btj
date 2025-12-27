package com.jeffyjamzhd.btj.curse;

import com.jeffyjamzhd.btj.api.curse.AbstractCurse;
import com.jeffyjamzhd.btj.api.curse.AbstractCurse;
import net.minecraft.src.I18n;

/**
 * Curse of Power Saver, forced low fps!
 * @see com.jeffyjamzhd.btj.mixin.render.EntityRendererMixin
 */
public class CurseFPS extends AbstractCurse {
    public CurseFPS() {
        this.setDisplayStrings(new String[]{
                "curse.btj.fps.name",
                "curse.btj.fps.desc"
        });
    }

    @Override
    public boolean usePacketSystem() {
        return false;
    }
}
