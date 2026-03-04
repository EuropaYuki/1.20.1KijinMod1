package com.YukiSato.KijinMod.item;

import com.YukiSato.KijinMod.item.tool.KijinModTiers;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Timer;
import java.util.TimerTask;

public class Hammer extends AxeItem {
    public Hammer() {
        super(KijinModTiers.SAKURITE, 6F, -3.0F, new Properties().rarity(Rarity.EPIC).fireResistant());
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity entity, LivingEntity attacker) {
        Level level = Minecraft.getInstance().level;
        entity.level().explode(entity, entity.getX(), entity.getY(), entity.getZ(), 10F, Level.ExplosionInteraction.TNT);
        return super.hurtEnemy(stack, entity, attacker);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        double reach = 30.0; // 見る距離
        Vec3 eyePos = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        Vec3 endPos = eyePos.add(look.scale(reach));

        AABB box1 = player.getBoundingBox()
                .expandTowards(look.scale(reach))
                .inflate(1.0);

        EntityHitResult result = ProjectileUtil.getEntityHitResult(
                player.level(),
                player,
                eyePos,
                endPos,
                box1,
                entity1 -> entity1 instanceof LivingEntity && entity1 != player
        );
        if (result != null) {
            LivingEntity entity = (LivingEntity) result.getEntity();
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (entity == null || !entity.isAlive() || entity.isRemoved()) {
                        timer.cancel();
                        return;
                    }
                    entity.push(1, 1.5, 1);
                    player.randomTeleport(entity.getX(), player.getY(), entity.getZ(), true);
                    player.setDeltaMovement(entity.getDeltaMovement());
                    entity.hurt(entity.damageSources().magic(), entity.getHealth() / 5);

                }
            }, 0, 800);

        }
        return InteractionResultHolder.consume(stack);
    }
}
