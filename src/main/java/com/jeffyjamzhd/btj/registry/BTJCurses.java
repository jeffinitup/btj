package com.jeffyjamzhd.btj.registry;

import com.jeffyjamzhd.btj.BetterThanJosh;
import com.jeffyjamzhd.btj.api.CurseRegistry;
import com.jeffyjamzhd.btj.api.curse.AbstractCurse;
import com.jeffyjamzhd.btj.api.curse.AbstractCurseMeter;
import com.jeffyjamzhd.btj.api.curse.ICurse;
import com.jeffyjamzhd.btj.api.event.curse.EventCurseListener;
import com.jeffyjamzhd.btj.curse.CurseFPS;
import com.jeffyjamzhd.btj.curse.CurseMITE;
import com.jeffyjamzhd.btj.curse.CurseThirst;
import net.minecraft.src.ResourceLocation;

public class BTJCurses implements EventCurseListener {
    private static final ResourceLocation TEX_PATH = BetterThanJosh.idOf("textures/gui/curse/tex.png");
    public static final AbstractCurseMeter CURSE_MITE = new CurseMITE(TEX_PATH);
    public static final AbstractCurseMeter CURSE_THIRST = new CurseThirst(TEX_PATH);
    public static final AbstractCurse CURSE_FPS = new CurseFPS();

    @Override
    public void register(CurseRegistry registry) {
        registerCurse(registry, "mite", CURSE_MITE);
        registerCurse(registry, "thirst", CURSE_THIRST);
        registerCurse(registry, "fps", CURSE_FPS);
    }

    private void registerCurse(CurseRegistry reg, String id, ICurse curse) {
        reg.registerCurse(BetterThanJosh.idOfString(id), curse);
    }
}
