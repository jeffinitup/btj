package com.jeffyjamzhd.btj.api.extend;

import api.util.AddonSoundRegistryEntry;
import btw.util.BTWSounds;

public interface IBTWSoundManager {
    /**
     * Returns sound manager
     */
    default BTWSounds btj$get() {
        return null;
    }

    /**
     * Registers a sound in registry
     */
    default void btj$registerSound(AddonSoundRegistryEntry entry) {
    }
}
