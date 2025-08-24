package com.jeffyjamzhd.btj.api.extend;

import btw.util.sounds.AddonSoundRegistryEntry;
import btw.util.sounds.BTWSoundManager;

import java.util.HashSet;

public interface IBTWSoundManager {
    /**
     * Returns sound manager
     */
    default BTWSoundManager btj$get() {
        return null;
    }

    /**
     * Registers a sound in registry
     */
    default void btj$registerSound(AddonSoundRegistryEntry entry) {
    }
}
