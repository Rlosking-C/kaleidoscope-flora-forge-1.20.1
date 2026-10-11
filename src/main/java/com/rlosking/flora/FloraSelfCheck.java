package com.rlosking.flora;

import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

/**
 * Startup self-check: turns silent failures into loud ones.
 *
 * <p>Forge discovers event handlers by scanning for
 * {@code @Mod.EventBusSubscriber}. Lose that annotation - a file moved, a
 * merge gone wrong - and the handlers simply never run: no error, no
 * warning, the feature is just gone.</p>
 *
 * <p>This class names every subscriber explicitly and verifies at startup
 * that each still exists and still carries the annotation. Problems are
 * logged at ERROR level, and in development the check throws.</p>
 */
public final class FloraSelfCheck {

    private static final List<String> SUBSCRIBER_CLASSES = List.of(
            "com.rlosking.flora.client.DynamicLightIntegration",
            "com.rlosking.flora.client.VampiricHeartFlash",
            "com.rlosking.flora.client.creativetab.FloraTabSidebar",
            "com.rlosking.flora.event.CakeEvents",
            "com.rlosking.flora.event.CombatEvents",
            "com.rlosking.flora.event.DrinkEvents",
            "com.rlosking.flora.event.HarvestEvents",
            "com.rlosking.flora.event.PlayerEvents",
            "com.rlosking.flora.event.VisualEvents");

    private FloraSelfCheck() {
    }

    public static void verify(Logger log) {
        List<String> broken = new ArrayList<>();
        for (String name : SUBSCRIBER_CLASSES) {
            try {
                Class<?> type = Class.forName(name);
                if (!type.isAnnotationPresent(Mod.EventBusSubscriber.class)) {
                    broken.add(name + " - missing @Mod.EventBusSubscriber");
                }
            } catch (ClassNotFoundException e) {
                broken.add(name + " - class not found");
            }
        }
        if (broken.isEmpty()) {
            log.info("[self-check] {} event classes verified", SUBSCRIBER_CLASSES.size());
            return;
        }
        log.error("[self-check] SILENT FAILURE RISK - {} event class(es) would never run: {}",
                broken.size(), broken);
        if (!FMLEnvironment.production) {
            throw new IllegalStateException("Flora self-check failed: " + broken);
        }
    }
}
