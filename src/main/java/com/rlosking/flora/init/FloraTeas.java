package com.rlosking.flora.init;

import com.rlosking.flora.KaleidoscopeFlora;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class FloraTeas {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, KaleidoscopeFlora.MOD_ID);

    private static final List<String> FLOWERS = List.of(
            "dandelion", "poppy", "blue_orchid", "allium", "azure_bluet",
            "red_tulip", "orange_tulip", "white_tulip", "pink_tulip", "oxeye_daisy",
            "cornflower", "lily_of_the_valley", "wither_rose", "torchflower", "sunflower",
            "lilac", "rose_bush", "peony", "pitcher_plant", "pink_petals",
            "chorus_flower", "spore_blossom",
            "wildflowers", "cactus_flower", "eyeblossom", "golden_dandelion");

    private static final List<String> VB_FLOWERS =
            List.of("wildflowers", "cactus_flower", "eyeblossom", "golden_dandelion");

    private static final List<RegistryObject<Item>> TEAS = new ArrayList<>();

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
        boolean backport = ModList.get().isLoaded("vanillabackport");

        for (String flower : available(backport)) {
            TEAS.add(ITEMS.register(flower + "_tea_bag", () -> new Item(new Item.Properties())));
        }
        KaleidoscopeFlora.LOGGER.info("Flower tea bags: {} items ({} flowers{})",
                TEAS.size(), available(backport).size(),
                backport ? ", VanillaBackport loaded" : "");
    }

    private static List<String> available(boolean backport) {
        return backport ? FLOWERS : FLOWERS.stream().filter(f -> !VB_FLOWERS.contains(f)).toList();
    }

    public static List<RegistryObject<Item>> teaItems() {
        return List.copyOf(TEAS);
    }

    private FloraTeas() {
    }
}
