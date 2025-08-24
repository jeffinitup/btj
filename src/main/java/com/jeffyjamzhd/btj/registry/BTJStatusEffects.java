package com.jeffyjamzhd.btj.registry;

import btw.util.status.StatusEffect;
import btw.util.status.StatusEffectBuilder;
import com.jeffyjamzhd.btj.api.CurseManager;
import com.jeffyjamzhd.btj.api.curse.ICurse;
import com.jeffyjamzhd.btj.curse.CurseMITE;
import com.jeffyjamzhd.btj.status.BTJStatusCategory;
import net.minecraft.src.Potion;

import java.util.Optional;

import static com.jeffyjamzhd.btj.BetterThanJosh.LOGGER;

public class BTJStatusEffects {
    public static StatusEffect CRAVING;
    public static StatusEffect FIENDING;
    public static StatusEffect MALNOURISHED;

    public static void createStatuses() {
        LOGGER.info("Registering status effects");

        CRAVING = createSeedStatus(1, "craving", 0.8F).build();
        FIENDING = createSeedStatus(2, "fiending", 0.6F).build();
        MALNOURISHED = createSeedStatus(3, "malnourished", 0.35F).build();
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
                Optional<ICurse> curse = man.getCurse(BTJCurses.CURSE_MITE);
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
}
