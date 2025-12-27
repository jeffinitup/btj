package com.jeffyjamzhd.btj.registry;

import com.jeffyjamzhd.btj.BetterThanJosh;
import com.jeffyjamzhd.btj.api.CurseRegistry;
import com.jeffyjamzhd.btj.api.curse.ICurse;
import com.jeffyjamzhd.btj.api.registry.RegistryEventCurseListener;
import com.jeffyjamzhd.btj.curse.CurseFPS;
import com.jeffyjamzhd.btj.curse.CurseMITE;
import com.jeffyjamzhd.btj.curse.CurseThirst;

public class BTJCurses implements RegistryEventCurseListener {
    public static final Class<? extends ICurse> CURSE_MITE = CurseMITE.class;
    public static final Class<? extends ICurse> CURSE_THIRST = CurseThirst.class;
    public static final Class<? extends ICurse> CURSE_FPS = CurseFPS.class;

    @Override
    public void register(CurseRegistry registry) {
        registerCurse(registry, "mite", CURSE_MITE);
        registerCurse(registry, "thirst", CURSE_THIRST);
        registerCurse(registry, "fps", CURSE_FPS);
    }

    private void registerCurse(CurseRegistry reg, String id, Class<? extends ICurse> curse) {
        reg.registerCurse(BetterThanJosh.idOfString(id), curse);
    }
}
