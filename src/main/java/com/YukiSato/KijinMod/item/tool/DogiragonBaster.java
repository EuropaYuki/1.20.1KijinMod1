package com.YukiSato.KijinMod.item.tool;

import com.YukiSato.KijinMod.entity.NonFireLB;
import com.YukiSato.KijinMod.entity.custom.CameramanEntity;
import com.YukiSato.KijinMod.entity.custom.NinjaEntity;
import com.YukiSato.KijinMod.item.ExtraRarity;
import com.YukiSato.KijinMod.item.tool.KijinModTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.FollowMobGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;

import java.util.EnumSet;
import java.util.List;

public class DogiragonBaster extends SwordItem {
    public DogiragonBaster() {
        super(KijinModTiers.SAKURITE, -1, -2.4F, new Properties().fireResistant().rarity(ExtraRarity.ULTIMATE));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Mob mob = (Mob) target;
        mob.goalSelector.addGoal(0, new NearestAttackableTargetGoal<>(mob, Mob.class, 0, false, false,
                (entity) -> {
            return entity instanceof Monster;
            }));
        return super.hurtEnemy(stack, target, attacker);
    }

    /**
     * 右クリック（メインハンド使用）したときの必殺技演出
     */
//    @Override
//    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
//        ItemStack stack = player.getItemInHand(hand);
//
//        // 近くの敵を探して攻撃
//        AABB area = player.getBoundingBox().inflate(10);
//        List<LivingEntity> enemies = level.getEntitiesOfClass(LivingEntity.class, area, e -> e != player);
//
//        for (LivingEntity enemy : enemies) {
//            if (enemy instanceof Monster) {
//                Mob mob = (Monster) enemy;
//                enemy.setCustomName(Component.literal("鎮魂の頂　暴走龍魔神轟怒"));
//                player.setCustomName(Component.literal("X二世　ジョバンニ"));
//                mob.targetSelector.getAvailableGoals().removeIf(priorityGoal -> {
//                    return priorityGoal.getGoal() instanceof NearestAttackableTargetGoal<?>
//                            // ほか、不要なゴールならtrue
//                            || priorityGoal.getGoal() instanceof HurtByTargetGoal;
//                });

//
//                mob.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 60 * 100000, 7));
//                mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 60 * 100000, 3));
//                mob.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 60 * 1000000, 20));
//            }
//        }
//        player.displayClientMessage(Component.literal("§c§l=== ファイナル革命 ==="), true);
//        player.getCooldowns().addCooldown(this, 1000);
//
//        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
//    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // 近くの敵を探して攻撃
        AABB area = player.getBoundingBox().inflate(10);
        List<LivingEntity> enemies = level.getEntitiesOfClass(LivingEntity.class, area, e -> e != player);

        for (LivingEntity enemy : enemies) {
            if (enemy instanceof Monster) {
                Mob mob = (Monster) enemy;
                mob.setCustomName(Component.literal("鎮魂の頂　暴走龍魔神轟怒"));
                player.setCustomName(Component.literal("X二世　ジョバンニ"));

                // 既存のターゲット関連のGoalを消去
                mob.targetSelector.getAvailableGoals().removeIf(priorityGoal ->
                        priorityGoal.getGoal() instanceof NearestAttackableTargetGoal<?>
                                || priorityGoal.getGoal() instanceof HurtByTargetGoal
                );

                // モブが“鎮魂の頂　暴走龍魔神轟怒” かつ プレイヤーが “X二世　ジョバンニ” の時、他のモンスターを攻撃するターゲットGoalを追加
                mob.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(mob, Monster.class, 0, false, false,
                        target -> {
                            Component name = mob.getCustomName(); // `mob` の名前を取得
                            Component jobannni = player.getCustomName();
                            return target instanceof Monster && name != null && name.getString().contains("鎮魂の頂　暴走龍魔神轟怒") && jobannni != null && jobannni.getString().contains("X二世　ジョバンニ");
                        }
                ));

                // ==== ここから追従用のゴールを追加する例 ====
                mob.goalSelector.addGoal(0, new Goal() {
                    /** 追従対象（ここでは呼び出し元のプレイヤー） */
                    private final Player owner = player;
                    /** 移動速度修正値 */
                    private final double speedModifier = 1.0D;
                    /** 一定距離まで近づいたら移動を止める距離 */
                    private final float stopDistance = 2.0F;

                    {
                        // 移動AIを使うよ、というフラグ設定（AI制御のヒント）
                        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
                    }

                    @Override
                    public boolean canUse() {
                        // ownerがnullでない & 距離が近すぎないなら実行
                        if (this.owner == null) {
                            return false;
                        }
                        // 追従を始めるかどうか(近いと追従しなくていいためfalse)
                        return mob.distanceToSqr(this.owner) > (this.stopDistance * this.stopDistance);
                    }

                    @Override
                    public boolean canContinueToUse() {
                        if (this.owner == null) {
                            return false;
                        }
                        // まだ近くない & ナビゲーションが終了していなければ継続
                        return mob.distanceToSqr(this.owner) > (this.stopDistance * this.stopDistance)
                                && !mob.getNavigation().isDone();
                    }

                    @Override
                    public void start() {
                        // ゴール開始時にナビゲーションをセット
                        mob.getNavigation().moveTo(this.owner, this.speedModifier);
                    }

                    @Override
                    public void stop() {
                        // ゴール終了時に移動を止める
                        mob.getNavigation().stop();
                    }

                    @Override
                    public void tick() {
                        // 毎フレームごとに目的地をアップデート
                        mob.getNavigation().moveTo(this.owner, this.speedModifier);
                    }
                });
                // ==== 追従ゴール追加ここまで ====

                // ステータス効果
                mob.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 60 * 100000, 7));
                mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 60 * 100000, 4));
                mob.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 60 * 1000000, 20));
            }
        }
        player.displayClientMessage(Component.literal("§c§l=== ファイナル革命 ==="), true);
        player.getCooldowns().addCooldown(this, 1000);

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }


}
