package com.jeffyjamzhd.btj.api.extend;

import com.jeffyjamzhd.btj.api.CurseManager;

public interface IEntityPlayer {
    /**
     * Gets the curse manager object assigned to player
     */
    default CurseManager btj$getCurseManager() {
        return null;
    }
}
