package com.rlosking.flora;

import com.mojang.logging.LogUtils;
import com.rlosking.flora.client.FloraConfigScreen;
import com.rlosking.flora.event.FloraCommonSetup;
import com.rlosking.flora.init.BlossomMooncakes;
import com.rlosking.flora.init.FloraAdvancements;
import com.rlosking.flora.init.FloraCreativeTab;
import com.rlosking.flora.init.FloraDrinks;
import com.rlosking.flora.init.FloraTeas;
import com.rlosking.flora.init.FlowerCakes;
import com.rlosking.flora.init.ModEffects;
import com.rlosking.flora.network.FloraNetwork;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(KaleidoscopeFlora.MOD_ID)
public class KaleidoscopeFlora {
    public static final String MOD_ID = "kaleidoscope_flora";
    public static final Logger LOGGER = LogUtils.getLogger();

    public KaleidoscopeFlora() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, FloraConfig.SPEC);

        FloraSelfCheck.verify(LOGGER);
        ModEffects.register(modBus);

        FloraAdvancements.register();

        FloraTeas.register(modBus);

        BlossomMooncakes.register(modBus);

        FlowerCakes.register(modBus);

        FloraCommonSetup.register(modBus);

        FloraNetwork.register();

        FloraCreativeTab.register(modBus);

        if (net.minecraftforge.fml.loading.FMLEnvironment.dist.isClient()) {
            com.rlosking.flora.client.FloraConfigScreen.register();
        }

        FloraDrinks.registerAll();
        com.rlosking.flora.compat.PatchouliGuide.applyConfigFlags();
    }
}
