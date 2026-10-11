package com.rlosking.flora.client.creativetab;

import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.TeacupRegistry;
import com.rlosking.flora.KaleidoscopeFlora;
import com.rlosking.flora.init.BlossomMooncakes;
import com.rlosking.flora.init.FloraAdvancements;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public enum FloraTabCategory {

    DRINKS("drinks", () -> new ItemStack(TeacupRegistry.getItem(
            new ResourceLocation(KaleidoscopeFlora.MOD_ID, "lullaby")))),

    OTHER("other", () -> new ItemStack(BlossomMooncakes.ITEM.get()));

    private final String key;
    private final Supplier<ItemStack> icon;

    FloraTabCategory(String key, Supplier<ItemStack> icon) {
        this.key = key;
        this.icon = icon;
    }

    public ItemStack icon() {
        return icon.get();
    }

    public Component label() {
        return Component.translatable("tooltip.kaleidoscope_flora.tab." + key);
    }

    public boolean accepts(ItemStack stack) {
        boolean drink = FloraAdvancements.isFloraDrink(stack);
        return this == DRINKS ? drink : !drink;
    }
}
