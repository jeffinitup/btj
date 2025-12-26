package com.jeffyjamzhd.btj.api;

import com.google.common.collect.HashBiMap;
import com.jeffyjamzhd.btj.api.curse.ICurse;

import java.util.*;
import java.util.function.Consumer;

import static com.jeffyjamzhd.btj.BetterThanJosh.LOGGER;

/**
 * Holds all possible curses
 */
public class CurseRegistry {
    public static final CurseRegistry INSTANCE = new CurseRegistry();
    private static final HashBiMap<String, Class<? extends ICurse>> CURSES = HashBiMap.create();
    private static final Consumer<CurseInfo> REGISTRY = CurseRegistry::consumeCurse;

    /**
     * Accepts a curse into registry
     * @param info Curse record
     */
    private static void consumeCurse(CurseInfo info) {
        CURSES.put(info.id, info.curse);
    }

    /**
     * Puts a curse object into the registry
     * @param curse Curse to register
     */
    public void registerCurse(String id, Class<? extends ICurse> curse) {
        LOGGER.info("Incoming curse - {}", id);
        REGISTRY.accept(new CurseInfo(id, curse));
    }

    /**
     * Gets a specific curse from the registry
     */
    public Class<? extends ICurse> getCurse(String id) {
        return CURSES.get(id);
    }

    /**
     * Gets a specific curse's identifier from the registry
     */
    public String getIdentifier(Class<? extends ICurse> curse) {
        return CURSES.inverse().get(curse);
    }

    /**
     * Gets a random curse from the registry
     * @param rng Random number generator
     * @return A random curse
     */
    public Class<? extends ICurse> getRandomCurse(Random rng) {
        Object[] values = CURSES.values().toArray();
        return (Class<? extends ICurse>) values[rng.nextInt(values.length)];
    }

    public HashBiMap<String, Class<? extends ICurse>> getCurses() {
        return CURSES;
    }

    public List<String> getCurseNames() {
        return CURSES.keySet().stream().toList();
    }

    private record CurseInfo(String id, Class<? extends ICurse> curse) {}
}
