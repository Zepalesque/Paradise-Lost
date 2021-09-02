package net.id.aether.entities.projectile;

import io.netty.buffer.Unpooled;
import net.id.aether.Aether;
import net.id.aether.entities.AetherEntityTypes;
import net.id.aether.entities.util.AetherPoisonMovement;
import net.id.aether.items.AetherItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PoisonDartEntity extends DartEntity {
    private LivingEntity victim;
    private AetherPoisonMovement poison;

    public PoisonDartEntity(EntityType<? extends DartEntity> entityType, World world) {
        super(entityType, world);
        this.setDamage(0);
    }

    protected PoisonDartEntity(EntityType<? extends DartEntity> entityType, double x, double y, double z, World world) {
        super(entityType, x, y, z, world);
        this.setDamage(0);
    }

    protected PoisonDartEntity(EntityType<? extends DartEntity> entityType, LivingEntity owner, World world) {
        super(entityType, owner, world);
        this.setDamage(0);
    }

    public PoisonDartEntity(double x, double y, double z, World world) {
        this(AetherEntityTypes.POISON_DART, x, y, z, world);
    }

    public PoisonDartEntity(LivingEntity owner, World world) {
        this(AetherEntityTypes.POISON_DART, owner, world);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.victim != null) {
            if (!this.victim.isAlive() || this.poison.ticks == 0) {
                this.tickInVoid();
                return;
            }

            if (this.getOwner() != null && this.getOwner().world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(Items.RED_DYE)), this.victim.getX(), this.victim.getBoundingBox().minY + this.victim.getHeight() * 0.8D, this.victim.getZ(), 2, 0.0D, 0.0D, 0.0D, 0.0625D);
            }

            this.unsetRemoved();
            this.poison.onUpdate();
            this.setInvisible(true);
            this.setPosition(this.victim.getX(), this.victim.getY(), this.victim.getZ());
        }
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);

        if (target instanceof ServerPlayerEntity playerTarget) {
            //AetherAPI.get((PlayerEntity) entityIn).inflictPoison(500);
            PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer());
            byteBuf.writeInt(500);
            playerTarget.networkHandler.sendPacket(new CustomPayloadC2SPacket(Aether.locate("poison"), byteBuf));
        } else {
            this.victim = target;
            this.poison = new AetherPoisonMovement(this.victim);
            this.poison.inflictPoison(500);
            this.unsetRemoved();
        }
    }

    @Override
    protected EntityHitResult getEntityCollision(Vec3d start, Vec3d end) {
        return this.victim == null ? super.getEntityCollision(start, end) : null;
    }

    @Override
    public void onPlayerCollision(PlayerEntity playerIn) {
        if (this.victim == null) {
            super.onPlayerCollision(playerIn);
        }
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(AetherItems.POISON_DART);
    }
}