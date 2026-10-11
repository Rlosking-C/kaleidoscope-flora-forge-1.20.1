package com.rlosking.flora.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.rlosking.flora.init.ModEffects;
import java.util.function.BiPredicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class AirSwimEntityMixin {

    @Inject(method = "getFluidTypeHeight", at = @At("RETURN"), cancellable = true, remap = false)
    private void flora$airHeight(FluidType type, CallbackInfoReturnable<Double> cir) {
        if (type == ForgeMod.EMPTY_TYPE.get()
                && cir.getReturnValueD() <= 0.0D
                && (Object) this instanceof Player player
                && ModEffects.isAirborne(player)) {
            cir.setReturnValue(1.0D);
        }
    }

    @WrapOperation(method = "updateSwimming()V",
            at = @At(value = "INVOKE", remap = false,
                    target = "Lnet/minecraft/world/entity/Entity;canStartSwimming()Z"))
    private boolean flora$canStartSwimming(Entity instance, Operation<Boolean> original) {
        return instance instanceof Player player && ModEffects.isAirborne(player)
                || original.call(instance);
    }

    @WrapOperation(method = {"updateSwimming()V", "isVisuallyCrawling()Z"},
            at = @At(value = "INVOKE", remap = false,
                    target = "Lnet/minecraft/world/entity/Entity;isInFluidType(Ljava/util/function/BiPredicate;)Z"))
    private boolean flora$isInFluidType(Entity instance, BiPredicate<FluidType, Double> predicate,
                                        Operation<Boolean> original) {
        return instance instanceof Player player && ModEffects.isAirborne(player)
                || original.call(instance, predicate);
    }

    @Inject(method = "isInFluidType()Z", at = @At("RETURN"), cancellable = true, remap = false)
    private void flora$isInAnyFluid(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ()
                && (Object) this instanceof Player player
                && ModEffects.isAirborne(player)) {
            cir.setReturnValue(true);
        }
    }
}
