package com.rlosking.flora.init;

import com.rlosking.flora.KaleidoscopeFlora;
import com.rlosking.flora.block.FlowerCakeBlock;
import com.rlosking.flora.item.FlowerCakeItem;
import com.rlosking.flora.item.RawFlowerCakeItem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class FlowerCakes {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, KaleidoscopeFlora.MOD_ID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, KaleidoscopeFlora.MOD_ID);

    private static BlockBehaviour.Properties cakeBlock() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .strength(0.5F)
                .sound(SoundType.CROP)
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY);
    }

    public static final RegistryObject<Block> RAW_BLOCK =
            BLOCKS.register("raw_flower_cake", () -> new FlowerCakeBlock(cakeBlock()));

    public static final RegistryObject<Block> TOASTED_BLOCK =
            BLOCKS.register("toasted_flower_cake", () -> new FlowerCakeBlock(cakeBlock()));

    public static final RegistryObject<Block> DEW_BLOCK =
            BLOCKS.register("dew_flower_cake", () -> new FlowerCakeBlock(cakeBlock()));

    public static final RegistryObject<Item> RAW =
            ITEMS.register("raw_flower_cake",
                    () -> new RawFlowerCakeItem(RAW_BLOCK.get(),
                            new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> TOASTED =
            ITEMS.register("toasted_flower_cake",
                    () -> new FlowerCakeItem(TOASTED_BLOCK.get(), new Item.Properties()
                            .food(new FoodProperties.Builder()
                                    .nutrition(4)
                                    .saturationMod(0.3F)
                                    .alwaysEat()
                                    .build())));

    public static final RegistryObject<Item> DEW =
            ITEMS.register("dew_flower_cake",
                    () -> new FlowerCakeItem(DEW_BLOCK.get(), new Item.Properties()
                            .food(new FoodProperties.Builder()
                                    .nutrition(4)
                                    .saturationMod(0.3F)
                                    .alwaysEat()
                                    .build())));

    private static final List<RegistryObject<Item>> CAKES =
            List.of(RAW, TOASTED, DEW);

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
    }

    public static List<RegistryObject<Item>> cakeItems() {
        return new ArrayList<>(CAKES);
    }

    private FlowerCakes() {
    }
}
