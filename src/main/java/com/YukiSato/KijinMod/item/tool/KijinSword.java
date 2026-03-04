package com.YukiSato.KijinMod.item.tool;

import com.YukiSato.KijinMod.entity.NonFireLB;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
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
        Vec3 vec3 = player.getViewVector(1.0F);
        double d5 = vec3.x;
        double d6 = vec3.y;
        double d7 = vec3.z;
        for (int i = 0; i < 1; ++i) {
            WitherSkull smallfireball = new WitherSkull(player.level(), player, d5, d6, d7);
            smallfireball.setPos(smallfireball.getX(), player.getY(), smallfireball.getZ());
            player.level().addFreshEntity(smallfireball);
        }
        for (int i = 0; i < 1; ++i) {
            LargeFireball smallfireball = new LargeFireball(player.level(), player, d5, d6, d7, Level.ExplosionInteraction.TNT.ordinal());
            smallfireball.setPos(player.blockPosition().getX() + 1, player.getY(), player.blockPosition().getZ() + 1);
            player.level().addFreshEntity(smallfireball);
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
