package com.rlosking.flora.client;

import com.rlosking.flora.KaleidoscopeFlora;
import com.rlosking.flora.init.ModEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = KaleidoscopeFlora.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DynamicLightIntegration {

    private static final int SUNWARD_LUMINANCE = 15;

    private static final String LEGACY_LBL_HANDLERS =
            "dev.lambdaurora.lambdynlights.api.DynamicLightHandlers";
    private static final String RYOAMIC_HANDLERS =
            "org.thinkingstudio.ryoamiclights.api.DynamicLightHandlers";

    private DynamicLightIntegration() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        boolean registered = false;

        if (classPresent(LEGACY_LBL_HANDLERS)) {
            try {
                registerLegacyLight();
                registered = true;
                KaleidoscopeFlora.LOGGER.info(
                        "Registered The Sunward dynamic light for a LambDynamicLights-compatible engine (level {}).",
                        SUNWARD_LUMINANCE);
            } catch (Throwable t) {
                KaleidoscopeFlora.LOGGER.warn("Dynamic-light registration failed: {}", t.toString());
            }
        }
        if (classPresent(RYOAMIC_HANDLERS)) {
            try {
                registerRyoamicLight();
                registered = true;
                KaleidoscopeFlora.LOGGER.info(
                        "Registered The Sunward dynamic light for RyoamicLights (level {}).",
                        SUNWARD_LUMINANCE);
            } catch (Throwable t) {
                KaleidoscopeFlora.LOGGER.warn("RyoamicLights registration failed: {}", t.toString());
            }
        }

        if (!registered) {
            KaleidoscopeFlora.LOGGER.info(
                    "No dynamic-lighting engine found - The Sunward will only glow via its outline.");
        }
    }

    private static boolean classPresent(String name) {
        try {
            Class.forName(name, false, DynamicLightIntegration.class.getClassLoader());
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    private static void registerLegacyLight() {
        dev.lambdaurora.lambdynlights.api.DynamicLightHandlers.registerDynamicLightHandler(EntityType.PLAYER,
                dev.lambdaurora.lambdynlights.api.DynamicLightHandler.makeHandler(
                        player -> {
                            if (player.hasEffect(ModEffects.SUNWARD.get()) && !player.level().isDay()) {
                                return SUNWARD_LUMINANCE;
                            }
                            return 0;
                        },
                        player -> false));
    }

    private static void registerRyoamicLight() {
        org.thinkingstudio.ryoamiclights.api.DynamicLightHandlers.registerDynamicLightHandler(EntityType.PLAYER,
                org.thinkingstudio.ryoamiclights.api.DynamicLightHandler.makeHandler(
                        player -> {
                            if (player.hasEffect(ModEffects.SUNWARD.get()) && !player.level().isDay()) {
                                return SUNWARD_LUMINANCE;
                            }
                            return 0;
                        },
                        player -> false));
    }
}
