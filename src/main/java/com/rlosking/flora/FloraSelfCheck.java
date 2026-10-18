package com.rlosking.flora;

import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

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
            if (!FMLEnvironment.dist.isClient() && name.startsWith("com.rlosking.flora.client.")) {
                continue;
            }
            try {
                Class<?> type = Class.forName(name);
                if (!type.isAnnotationPresent(Mod.EventBusSubscriber.class)) {
                    broken.add(name + " - missing @Mod.EventBusSubscriber");
                }
            } catch (Throwable e) {
                broken.add(name + " - " + e.getClass().getSimpleName());
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
