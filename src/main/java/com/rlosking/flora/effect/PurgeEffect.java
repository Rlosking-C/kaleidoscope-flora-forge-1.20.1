package com.rlosking.flora.effect;

import java.util.List;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class PurgeEffect extends MobEffect {

    static final int PURGE_MAX_EFFECTS = 3;

    public PurgeEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xF5F0C8);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide) {
            return;
        }
        int cleared = 0;
        for (MobEffectInstance instance : List.copyOf(entity.getActiveEffects())) {
            if (cleared >= PURGE_MAX_EFFECTS) {
                break;
            }
            if (instance.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                entity.removeEffect(instance.getEffect());
                cleared++;
            }
        }
    }
}
