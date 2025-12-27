package com.jeffyjamzhd.btj.api.registry;

import com.jeffyjamzhd.btj.api.CurseRegistry;

import java.util.ArrayList;
import java.util.List;

import static com.jeffyjamzhd.btj.BetterThanJosh.LOGGER;

/**
 * Curse registry event
 */
public class RegistryEventCurse {
    private static final List<RegistryEventCurseListener> LISTENERS = new ArrayList<>();

    /**
     * Registers provided listener
     */
    public static void register(RegistryEventCurseListener listener) {
        LISTENERS.add(listener);
    }

    /**
     * Called when time to register
     */
    public static void init() {
        for (RegistryEventCurseListener listener : LISTENERS) {
            LOGGER.info("Curse register - {}", listener.getClass().getSimpleName());
            listener.register(CurseRegistry.INSTANCE);
        }
    }
}
