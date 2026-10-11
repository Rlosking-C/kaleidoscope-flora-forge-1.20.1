package com.rlosking.flora.client;

import com.rlosking.flora.KaleidoscopeFlora;
import com.rlosking.flora.init.ModEffects;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KaleidoscopeFlora.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class VampiricHeartFlash {

    private static float lastHealth = Float.NaN;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if (!(event.player instanceof LocalPlayer self)) {
            return;
        }
        float health = self.getHealth();
        if (!Float.isNaN(lastHealth)
                && health > lastHealth
                && self.hasEffect(ModEffects.VAMPIRIC.get())) {
            Gui gui = Minecraft.getInstance().gui;
            int gained = Math.max(1, Mth.ceil(health) - Mth.ceil(lastHealth));
            gui.healthBlinkTime = gui.tickCount + 10L;
            gui.lastHealthTime = Util.getMillis();
            gui.displayHealth = Math.min(20, Mth.ceil(health) + gained);
        }
        lastHealth = health;
    }

    private VampiricHeartFlash() {
    }
}
