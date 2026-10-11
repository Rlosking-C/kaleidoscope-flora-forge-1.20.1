package com.rlosking.flora.init;

import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.TeacupRegistry;
import com.rlosking.flora.KaleidoscopeFlora;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class FloraCreativeTab {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KaleidoscopeFlora.MOD_ID);

    public static final RegistryObject<CreativeModeTab> FLORA_TAB =
            TABS.register("flora", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.kaleidoscope_flora.flora"))
                    .icon(() -> new ItemStack(TeacupRegistry.getItem(
                            new ResourceLocation(KaleidoscopeFlora.MOD_ID, "when_the_wind_rises"))))
                    .displayItems(FloraCreativeTab::fill)
                    .build());

    public static void register(IEventBus modBus) {
        TABS.register(modBus);
        modBus.addListener(FloraCreativeTab::removeFromCookeryTab);
    }

    private static void fill(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        for (RegistryObject<Item> item : FloraTeas.teaItems()) {
            output.accept(item.get());
        }
        output.accept(BlossomMooncakes.ITEM.get());
        for (RegistryObject<Item> item : FlowerCakes.cakeItems()) {
            output.accept(item.get());
        }
        for (Item item : BuiltInRegistries.ITEM) {
            if (FloraAdvancements.isFloraDrink(new ItemStack(item))) {
                output.accept(item);
            }
        }
    }

    private static void removeFromCookeryTab(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            int before = TeacupRegistry.TEACUP_DATA_MAP.size();
            TeacupRegistry.TEACUP_DATA_MAP.keySet()
                    .removeIf(id -> id.getNamespace().equals(KaleidoscopeFlora.MOD_ID));
            KaleidoscopeFlora.LOGGER.info("Moved {} drinks out of Cookery's food tab into the Flora tab",
                    before - TeacupRegistry.TEACUP_DATA_MAP.size());
        });
    }

    private FloraCreativeTab() {
    }
}
