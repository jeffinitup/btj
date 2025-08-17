package com.jeffyjamzhd.btj.api;

import com.jeffyjamzhd.btj.api.curse.ICurse;
import net.minecraft.src.ResourceLocation;

import java.util.*;
import java.util.function.Consumer;

import static com.jeffyjamzhd.btj.BetterThanJosh.LOGGER;

/**
 * Holds all possible curses
 */
public class CurseRegistry {
    public static final CurseRegistry INSTANCE = new CurseRegistry();
    private static final HashMap<String, ICurse> CURSES = new HashMap<>();
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
    public void registerCurse(String id, ICurse curse) {
        LOGGER.info("Incoming curse - {}", id);
        curse.setIdentifier(id);
        REGISTRY.accept(new CurseInfo(id, curse));
    }

    /**
     * Gets a specific curse from the registry
     */
    public ICurse getCurse(String id) {
        return CURSES.get(id);
    }

    /**
     * Gets a random curse from the registry
     * @param rng Random number generator
     * @return A random curse
     */
    public ICurse getRandomCurse(Random rng) {
        Object[] values = CURSES.values().toArray();
        return (ICurse) values[rng.nextInt(values.length)];
    }

    public HashMap<String, ICurse> getCurses() {
        return CURSES;
    }

    public List<String> getCurseNames() {
        return CURSES.keySet().stream().toList();
    }

    private record CurseInfo(String id, ICurse curse) {}
}
