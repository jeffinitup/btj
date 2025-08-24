package com.jeffyjamzhd.btj.status;

import btw.util.status.StatusCategory;

public enum BTJStatusCategory implements StatusCategory {
    CRAVING("craving"),
    THIRSTY("thirsty"),
    POOP("poop"),
    WRIST("wrist"),
    SUNBURN("sunburn");

    private String name;

    BTJStatusCategory(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
