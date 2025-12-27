package com.jeffyjamzhd.btj.registry;

import api.util.status.StatusEffect;
import api.util.status.StatusEffectBuilder;
import com.jeffyjamzhd.btj.api.CurseManager;
import com.jeffyjamzhd.btj.api.curse.AbstractCurse;
import com.jeffyjamzhd.btj.curse.CurseMITE;
import com.jeffyjamzhd.btj.curse.CurseThirst;
import com.jeffyjamzhd.btj.status.BTJStatusCategory;
import net.minecraft.src.Potion;

import java.util.Optional;

import static com.jeffyjamzhd.btj.BetterThanJosh.LOGGER;

public class BTJStatusEffects {
    /**
     * MiTE
     */
    public static StatusEffect CRAVING;
    public static StatusEffect FIENDING;
    public static StatusEffect MALNOURISHED;

    /**
     * Thirst
     */
    public static StatusEffect PARCHED;
    public static StatusEffect THIRSTY;
    public static StatusEffect DEHYDRATED;

    public static void createStatuses() {
        LOGGER.info("Registering status effects");

        CRAVING = createSeedStatus(1, "craving", 0.8F).build();
        FIENDING = createSeedStatus(2, "fiending", 0.6F).build();
        MALNOURISHED = createSeedStatus(3, "malnourished", 0.35F).build();

        PARCHED = createThirstStatus(1, "parched", 0.9F).build();
        THIRSTY = createThirstStatus(2, "thirsty", 0.7F).build();
        DEHYDRATED = createThirstStatus(3, "dehydrated", 0.5F).build();
    }

    private static StatusEffectBuilder createSeedStatus(int level, String name, float effectiveness) {
        StatusEffectBuilder effect = (new StatusEffectBuilder(level, BTJStatusCategory.CRAVING))
                .setEffectivenessMultiplier(effectiveness)
                .setAffectsMovement()
                .setAffectsMiningSpeed()
                .setUnlocalizedName(BTJStatusCategory.CRAVING.getName(), name);

        if (level > 1)
            effect.setPreventsSprinting();

        if (level > 2)
            effect.setPreventsJumping()
                    .setPotionEffect(Potion.confusion.id, 0);

        // Evaluates seed status
        effect.setEvaluator(player -> {
            if (!player.capabilities.isCreativeMode && player.btj$getCurseManager() != null) {
                CurseManager man = player.btj$getCurseManager();
                Optional<AbstractCurse> curse = man.getCurse(BTJCurses.CURSE_MITE);
                if (curse.isPresent()) {
                    CurseMITE seedCurse = (CurseMITE) curse.get();
                    int value = seedCurse.getValue();
                    return value <= 4000 - (2000 * (level - 1));
                }
            }
            return false;
        });
        return effect;
    }

    private static StatusEffectBuilder createThirstStatus(int level, String name, float effectiveness) {
        StatusEffectBuilder effect = (new StatusEffectBuilder(level, BTJStatusCategory.THIRSTY))
                .setEffectivenessMultiplier(effectiveness)
                .setAffectsMovement()
                .setAffectsMiningSpeed()
                .setUnlocalizedName(BTJStatusCategory.THIRSTY.getName(), name);

        if (level > 1)
            effect.setPreventsSprinting()
                  .setAffectsAttackDamage();

        if (level > 2)
            effect.setPreventsJumping();

        // Evaluates seed status
        effect.setEvaluator(player -> {
            if (!player.capabilities.isCreativeMode && player.btj$getCurseManager() != null) {
                CurseManager man = player.btj$getCurseManager();
                Optional<AbstractCurse> curse = man.getCurse(BTJCurses.CURSE_THIRST);
                if (curse.isPresent()) {
                    CurseThirst thirstCurse = (CurseThirst) curse.get();
                    int value = thirstCurse.getValue();
                    return value <= 4000 - (2000 * (level - 1));
                }
            }
            return false;
        });
        return effect;
    }
}
