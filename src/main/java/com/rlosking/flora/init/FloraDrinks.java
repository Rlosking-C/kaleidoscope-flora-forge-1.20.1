package com.rlosking.flora.init;

import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.TeacupRegistry;
import com.rlosking.flora.FloraConfig;
import com.rlosking.flora.KaleidoscopeFlora;
import com.rlosking.flora.init.ModEffects;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public final class FloraDrinks {
    private static final String COOKERY_ID = "kaleidoscope_cookery";

    private static final TeacupRegistry REGISTRY = new TeacupRegistry();

    private FloraDrinks() {
    }

    private static int ticks(int seconds) {
        double multiplier = FloraConfig.SPEC.isLoaded()
                ? FloraConfig.effectDurationMultiplier()
                : 1.0;
        return Math.max(1, (int) (seconds * 20 * multiplier));
    }

    public static void registerAll() {

        register("when_the_wind_rises", TeacupRegistry.TeacupData.create(4)
                .addEffect(() -> new MobEffectInstance(ModEffects.PURGE.get(), 1, 0))
                .addEffect(() -> new MobEffectInstance(MobEffects.SLOW_FALLING, ticks(90))));

        register("lullaby", TeacupRegistry.TeacupData.create(4)
                .addEffect(() -> new MobEffectInstance(ModEffects.DROWSY.get(), ticks(180))));

        register("first_bloom", TeacupRegistry.TeacupData.create(4)
                .addEffect(() -> new MobEffectInstance(ModEffects.TASTEBLOOM.get(), ticks(300))));

        register("fire_waltz", TeacupRegistry.TeacupData.create(4)
                .addEffect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, ticks(180)))
                .addEffect(() -> new MobEffectInstance(ModEffects.FIREBRAND.get(), ticks(180))));

        register("the_unnoticed", TeacupRegistry.TeacupData.create(4)
                .addEffect(() -> new MobEffectInstance(MobEffects.INVISIBILITY, ticks(60)))
                .addEffect(() -> new MobEffectInstance(ModEffects.FADEAWAY.get(), 1, 0)));

        register("crimson_heartbeat", TeacupRegistry.TeacupData.create(4)
                .addEffect(() -> new MobEffectInstance(ModEffects.VAMPIRIC.get(), ticks(180))));

        register("autumn_serenade", TeacupRegistry.TeacupData.create(4)
                .addEffect(() -> new MobEffectInstance(ModEffects.HARVEST.get(), ticks(180))));

        register("absolution", TeacupRegistry.TeacupData.create(4)
                .addEffect(() -> new MobEffectInstance(ModEffects.ABSOLVE.get(), ticks(180))));

        register("rosy_stride", TeacupRegistry.TeacupData.create(4)
                .addEffect(() -> new MobEffectInstance(ModEffects.PETALWALK.get(), ticks(90))));

        register("loves_me_not", TeacupRegistry.TeacupData.create(4)
                .addEffect(() -> new MobEffectInstance(MobEffects.REGENERATION, ticks(90)))
                .addEffect(() -> new MobEffectInstance(ModEffects.DIVINATION.get(), ticks(180))));

        register("prussian_leap", TeacupRegistry.TeacupData.create(4)
                .addEffect(() -> new MobEffectInstance(MobEffects.JUMP, ticks(90), 1))
                .addEffect(() -> new MobEffectInstance(ModEffects.FEATHERFALL.get(), ticks(90))));

        register("may_kiss", TeacupRegistry.TeacupData.create(4)
                .addEffect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, ticks(135), 1))
                .addEffect(() -> new MobEffectInstance(ModEffects.KISS.get(), ticks(135))));

        register("fleurs_du_mal", TeacupRegistry.TeacupData.create(4)
                .addEffect(() -> new MobEffectInstance(ModEffects.WITHER_AURA.get(), ticks(180)))
                .addEffect(() -> new MobEffectInstance(MobEffects.WITHER, ticks(2))));

        register("breath_of_ancients", TeacupRegistry.TeacupData.create(4)
                .addEffect(() -> new MobEffectInstance(ModEffects.SNIFFER_SOUL.get(), ticks(300)))
                .addEffect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, ticks(180))));

        register("the_sunward", TeacupRegistry.TeacupData.create(4)
                .addEffect(() -> new MobEffectInstance(ModEffects.SUNWARD.get(), ticks(180))));

        register("spring_waltz", TeacupRegistry.TeacupData.create(4)
                .addEffect(cookeryEffect("warmth", 240, 0)));

        register("tender_thorns", TeacupRegistry.TeacupData.create(4)
                .addEffect(() -> new MobEffectInstance(ModEffects.THORNS.get(), ticks(180))));

        register("coronation", TeacupRegistry.TeacupData.create(4)
                .addEffect(() -> new MobEffectInstance(MobEffects.LUCK, ticks(180)))
                .addEffect(() -> new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, ticks(60), 0)));

        register("voracious_urn", TeacupRegistry.TeacupData.create(4)
                .addEffect(() -> new MobEffectInstance(ModEffects.DIGESTION.get(), ticks(240))));

        register("hanami_tale", TeacupRegistry.TeacupData.create(4)
                .addEffect(() -> new MobEffectInstance(ModEffects.PETAL_VEIL.get(), ticks(180))));

        register("echo_of_the_end", TeacupRegistry.TeacupData.create(4)
                .addEffect(() -> new MobEffectInstance(ModEffects.ECHO.get(), ticks(300))));

        register("vernal_awakening", TeacupRegistry.TeacupData.create(4)
                .addEffect(() -> new MobEffectInstance(ModEffects.SPROUT.get(), ticks(180))));

        if (ModList.get().isLoaded("vanillabackport")) {

            register("the_gaze", TeacupRegistry.TeacupData.create(4)
                    .addEffect(() -> new MobEffectInstance(ModEffects.GAZE.get(), ticks(180))));

            register("as_you_wish", TeacupRegistry.TeacupData.create(4)
                    .addEffect(() -> new MobEffectInstance(ModEffects.WISH.get(), 1, 0)));

            register("springtime_stroll", TeacupRegistry.TeacupData.create(4)
                    .addEffect(() -> new MobEffectInstance(ModEffects.FLOWER_PATH.get(), ticks(180))));

            register("fleeting_bloom", TeacupRegistry.TeacupData.create(4)
                    .addEffect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, ticks(60)))
                    .addEffect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, ticks(60)))
                    .addEffect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, ticks(60)))
                    .addEffect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, ticks(60))));
        }
    }

    private static void register(String name, TeacupRegistry.TeacupData data) {
        REGISTRY.registerTeacupData(
                new ResourceLocation(KaleidoscopeFlora.MOD_ID, name), data);
    }

    private static Supplier<MobEffectInstance> cookeryEffect(String path, int seconds, int amplifier) {
        return () -> {
            MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(
                    new ResourceLocation(COOKERY_ID, path));
            if (effect == null) {
                throw new IllegalStateException(
                        "Kaleidoscope Cookery effect not found: " + COOKERY_ID + ":" + path);
            }
            return new MobEffectInstance(effect, ticks(seconds), amplifier);
        };
    }
}
