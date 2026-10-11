package com.rlosking.flora.init;

import com.rlosking.flora.KaleidoscopeFlora;
import com.rlosking.flora.block.BlossomMooncakeBlock;
import com.rlosking.flora.item.BlossomMooncakeItem;
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

public final class BlossomMooncakes {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, KaleidoscopeFlora.MOD_ID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, KaleidoscopeFlora.MOD_ID);

    public static final RegistryObject<Block> MOONCAKE_BLOCK =
            BLOCKS.register("blossom_mooncake", () -> new BlossomMooncakeBlock(mooncakeProperties()));

    public static final RegistryObject<Item> ITEM =
            ITEMS.register("blossom_mooncake", () -> new BlossomMooncakeItem(MOONCAKE_BLOCK.get(),
                    new Item.Properties().food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.6F)
                            .build())));

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
    }

    private static BlockBehaviour.Properties mooncakeProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(0.5F)
                .sound(SoundType.WOOD)
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY);
    }

    private BlossomMooncakes() {
    }
}
