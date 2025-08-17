package com.jeffyjamzhd.btj.api.event.curse;

import com.jeffyjamzhd.btj.api.CurseRegistry;

import java.util.ArrayList;
import java.util.List;

import static com.jeffyjamzhd.btj.BetterThanJosh.LOGGER;

/**
 * Curse registry event
 */
public class EventCurse {
    private static final List<EventCurseListener> LISTENERS = new ArrayList<>();

    /**
     * Registers provided listener
     */
    public static void register(EventCurseListener listener) {
        LISTENERS.add(listener);
    }

    /**
     * Called when time to register
     */
    public static void init() {
        for (EventCurseListener listener : LISTENERS) {
            LOGGER.info("Curse register - {}", listener.getClass().getSimpleName());
            listener.register(CurseRegistry.INSTANCE);
        }
    }
}
