package com.aether.client.model;

import com.aether.items.AetherItems;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.resources.ResourceLocation;

public class AetherModelPredicates {

    public static void init() {
        FabricModelPredicateProviderRegistry.register(AetherItems.PHOENIX_BOW, new ResourceLocation("pull"), ((stack, world, entity, seed) -> {
            if (entity == null) {
                return 0F;
            }
            return entity.getUseItem() != stack ? 0F : (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20F;
        }));

        FabricModelPredicateProviderRegistry.register(AetherItems.PHOENIX_BOW, new ResourceLocation("pulling"), (itemStack, clientWorld, livingEntity, seed) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
        });
    }
}
