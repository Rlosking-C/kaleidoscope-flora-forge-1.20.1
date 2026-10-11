package com.rlosking.flora.event;

import com.rlosking.flora.init.FlowerCakes;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public final class FloraCommonSetup {

    public static void register(IEventBus modBus) {
        modBus.addListener(FloraCommonSetup::onCommonSetup);
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> DispenserBlock.registerBehavior(FlowerCakes.RAW.get(),
                new AbstractProjectileDispenseBehavior() {
                    @Override
                    protected Projectile getProjectile(Level level, Position pos, ItemStack stack) {
                        Snowball snowball = new Snowball(level, pos.x(), pos.y(), pos.z());
                        snowball.setItem(stack);
                        return snowball;
                    }
                }));
    }

    private FloraCommonSetup() {
    }
}
