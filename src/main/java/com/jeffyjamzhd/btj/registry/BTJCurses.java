package com.jeffyjamzhd.btj.registry;

import com.jeffyjamzhd.btj.BetterThanJosh;
import com.jeffyjamzhd.btj.api.CurseRegistry;
import com.jeffyjamzhd.btj.api.curse.AbstractCurseMeter;
import com.jeffyjamzhd.btj.api.curse.ICurse;
import com.jeffyjamzhd.btj.api.event.curse.EventCurseListener;
import com.jeffyjamzhd.btj.curse.CurseMITE;

public class BTJCurses implements EventCurseListener {
    private static final AbstractCurseMeter CURSE_MITE = new CurseMITE(BetterThanJosh.idOf("textures/gui/curse/seed.png"));

    @Override
    public void register(CurseRegistry registry) {
        registerCurse(registry, "mite", CURSE_MITE);
    }

    private void registerCurse(CurseRegistry reg, String id, ICurse curse) {
        reg.registerCurse(BetterThanJosh.idOfString(id), curse);
    }
}
