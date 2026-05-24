package com.YukiSato.KijinMod.item.tool;

import com.YukiSato.KijinMod.entity.NonFireLB;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Timer;
import java.util.TimerTask;

public class KijinSword extends SwordItem {
    public KijinSword() {
        super(KijinModTiers.SAKURITE, 5, -2.4F, new Properties().fireResistant().rarity(Rarity.EPIC));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        Vec3 vec3 = player.getLookAngle();
        player.setDeltaMovement(vec3.x * 0.6, vec3.y * 0.6, vec3.z * 0.6);
        double reach = 2; // 見る距離
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
                entity1 -> entity1 instanceof LivingEntity
        );
        if (result != null) {
            Entity entity1 = result.getEntity();
            entity1.hurt(level.damageSources().playerAttack(player), 8.0F);
            player.setDeltaMovement(vec3.x * -1.1, vec3.y * 1.1, vec3.z * -1.1);
        }

        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity enemy, LivingEntity attacker) {
        Vec3 vec3 = attacker.getLookAngle();
        enemy.setDeltaMovement(vec3.x * 1.5, 5.0, vec3.z * 1.5);
        Player player = (Player) attacker;
        UseOnContext context = new UseOnContext(player, player.getUsedItemHand(), null);
        Level level = context.getLevel();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                BlockPos pos = enemy.blockPosition();
                LightningBolt nonFire = new NonFireLB(level);
                nonFire.moveTo(Vec3.atBottomCenterOf(pos));
                nonFire.setCause(player instanceof ServerPlayer ? (ServerPlayer) player : null);
                level.addFreshEntity(nonFire);
            }
        }, 1);

        return super.hurtEnemy(stack, enemy, attacker);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_SWORD_ACTIONS.contains(toolAction);
    }
}
