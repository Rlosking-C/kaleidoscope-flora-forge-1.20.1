package com.rlosking.flora.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.rlosking.flora.init.ModEffects;
import java.util.function.BiPredicate;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public abstract class AirSwimLocalPlayerMixin {

    @WrapOperation(method = "aiStep()V",
            at = @At(value = "INVOKE", remap = false,
                    target = "Lnet/minecraft/client/player/LocalPlayer;canStartSwimming()Z"))
    private boolean flora$canStartSwimming(LocalPlayer instance, Operation<Boolean> original) {
        return ModEffects.isAirborne(instance) || original.call(instance);
    }

    @WrapOperation(method = "aiStep()V",
            at = @At(value = "INVOKE", remap = false,
                    target = "Lnet/minecraft/client/player/LocalPlayer;isInFluidType(Ljava/util/function/BiPredicate;)Z"))
    private boolean flora$isInFluidType(LocalPlayer instance, BiPredicate<FluidType, Double> predicate,
                                        Operation<Boolean> original) {
        return ModEffects.isAirborne(instance) || original.call(instance, predicate);
    }
}
