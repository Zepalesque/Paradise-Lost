package net.id.paradiselost.mixin.entity;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.effect.ParadiseLostStatusEffects;
import net.id.paradiselost.world.ParadiseLostGameRules;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Redirect(method = "getVelocityMultiplier", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getVelocityMultiplier()F"), require = 2)
    private float getVelocityMultiplier(Block target) {
        //TODO: reimplement if we re-add slippery blocks
//        if (target == ParadiseLostBlocks.QUICKSOIL || target == ParadiseLostBlocks.QUICKSOIL_GLASS || target == ParadiseLostBlocks.QUICKSOIL_GLASS_PANE) {
//            Entity entity = ((Entity) (Object) this);
//            boolean isVehicle = entity instanceof BoatEntity || entity instanceof MinecartEntity;
//
//            double maxSpeed = entity.world.getGameRules().get(ParadiseLostGameRules.MAX_QUICKSOIL_SPEED).get();
//            maxSpeed = isVehicle ? maxSpeed * 0.16D : maxSpeed;
//            float calculatedChange = (float) ((maxSpeed - entity.getVelocity().horizontalLength()) / maxSpeed * 0.102);
//
//            if (isVehicle) {
//                return Math.min(1.0F, 1.0F + calculatedChange);
//            } else {
//                return (1 + Math.max(calculatedChange, 0));
//            }
//        }
        return target.getVelocityMultiplier();
    }

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;updateSwimming()V"))
    private void springWaterEffects(CallbackInfo ci) {
        Entity entity = ((Entity) (Object) this);
        
        if (entity instanceof LivingEntity livingEntity) {
            if (entity.world.getStatesInBoxIfLoaded(entity.getBoundingBox().contract(1.0E-6D)).anyMatch(
                    (state) -> state.getBlock().equals(ParadiseLostBlocks.SPRING_WATER))
            ) {
                livingEntity.addStatusEffect(new StatusEffectInstance(ParadiseLostStatusEffects.SIMMERING, 6000000, 0, true, false, true));
            } else if (livingEntity.hasStatusEffect(ParadiseLostStatusEffects.SIMMERING)) {
                livingEntity.removeStatusEffect(ParadiseLostStatusEffects.SIMMERING);
            }
        }
    }
}
