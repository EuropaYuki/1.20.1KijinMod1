package com.YukiSato.KijinMod.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GodItem extends Item {
    public GodItem() {
        super(new Properties().rarity(ExtraRarity.ULTIMATE).fireResistant().stacksTo(1));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // スイング時：火の粉を発生
        if (attacker.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.FLAME, attacker.getX(), attacker.getY() + 1, attacker.getZ(),
                    20, 0.5, 0.5, 0.5, 0.01);
        }
        stack.hurtAndBreak(1, attacker, e -> e.broadcastBreakEvent(InteractionHand.MAIN_HAND));

        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            // 火の粉エフェクト
            ((ServerLevel) level).sendParticles(ParticleTypes.FLAME,
                    player.getX(), player.getY() + 1, player.getZ(),
                    40, 0.5, 0.5, 0.5, 0.02);

            // プレイヤーを前方にpush（突進）
            Vec3 look = player.getLookAngle();
            player.setDeltaMovement(look.x * 5, look.y * 2, look.z * 5);

            // 爆発を後で起こす（突進後の位置に）
        }

        player.getCooldowns().addCooldown(this, 40); // クールダウン（2秒）
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    // プレイヤーの移動先で爆発（スケジュールされたTickイベントに対応）
    // 爆発の場所は `use()` 内で覚えておくと良いけど、簡略化するなら↓を使用
}
