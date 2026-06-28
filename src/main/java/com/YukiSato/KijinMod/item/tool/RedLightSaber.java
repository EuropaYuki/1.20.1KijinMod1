package com.YukiSato.KijinMod.item.tool;

import com.YukiSato.KijinMod.item.ExtraRarity;
import com.YukiSato.KijinMod.keybind.KijinKeyBind;
import com.YukiSato.KijinMod.regi.KijinModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RedLightSaber extends SwordItem {

    public RedLightSaber() {
        super(KijinModTiers.SAKURITE, Integer.MAX_VALUE, -1.7F, new Properties().fireResistant().rarity(ExtraRarity.ULTIMATE));
    }

    public UseAnim getUseAnimation(ItemStack p_43105_) {
        return UseAnim.SPEAR;
    }

    public int getUseDuration(ItemStack p_43107_) {
        return 72000;
    }

    public InteractionResultHolder<ItemStack> use(Level p_43099_, Player p_43100_, InteractionHand p_43101_) {
        ItemStack itemstack = p_43100_.getItemInHand(p_43101_);
        p_43100_.startUsingItem(p_43101_);
        return InteractionResultHolder.consume(itemstack);
    }
    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (level.isClientSide) {
            return;
        }

        if (livingEntity instanceof Player player) {
            // 右クリック中、毎tickスピードを更新する
            player.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SPEED,
                    7,      // 効果時間。短めでOK。右クリックを離すとすぐ切れる
                    9,      // 強さ。2なら移動速度 III
                    false,
                    false
            ));
        }
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return ToolActions.DEFAULT_SHIELD_ACTIONS.contains(toolAction);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        Player player = (Player)entity;
        Vec3 vec31 = player.getLookAngle();
        player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 60 * 100, 9, false, false));
        if (KijinKeyBind.kijinKey[0].isDown()) {
            player.setDeltaMovement(vec31.x * 2, vec31.y * 2, vec31.z * 2);
            AABB area = new AABB(
                    player.getX() - 1, player.getEyeY() - 1, player.getZ() - 1,
                    player.getX() + 1, player.getEyeY() + 1, player.getZ() + 1
            );

            // ■ 敵取得
            List<LivingEntity> targets = level.getEntitiesOfClass(
                    LivingEntity.class,
                    area,
                    entity1 -> entity1 != player && entity1.isAlive()
            );

            // ■ ダメージ処理
            for (LivingEntity target : targets) {

                target.hurt(player.damageSources().playerAttack(player), Float.MAX_VALUE);

                // ノックバック（斬撃っぽさ）
                Vec3 knockback = target.position().subtract(player.position()).normalize().scale(0.1);
                target.setDeltaMovement(target.getDeltaMovement().add(knockback));
            }

            // ■ 既存のパーティクル（そのまま）
            int[] a = {-2, -1, 0, 1, 2};
            for (int ax = 0; ax < 5; ax++) {
                for (int az = 0; az < 5; az++) {
                    BlockPos aPos = new BlockPos(
                            (int) player.getX() + a[ax],
                            (int) player.getEyeY(),
                            (int) player.getZ() + a[az]
                    );

                    if (level.getBlockState(aPos).isAir() && level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(
                                ParticleTypes.LAVA,
                                aPos.getX(),
                                aPos.getY(),
                                aPos.getZ(),
                                1,
                                0.2, 0.2, 0.2,
                                0.01
                        );
                        serverLevel.sendParticles(
                                ParticleTypes.SWEEP_ATTACK,
                                aPos.getX(),
                                aPos.getY(),
                                aPos.getZ(),
                                1,
                                0, 0, 0,
                                0
                        );
                    }
                }
            }
        }
        if (player.isSteppingCarefully()
                && !player.getCooldowns().isOnCooldown(KijinModItems.RED_LIGHT_SABER.get())) {
            player.addEffect(new MobEffectInstance(MobEffects.JUMP, 20, 7, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 240, 4, false, false));
            player.getCooldowns().addCooldown(KijinModItems.RED_LIGHT_SABER.get(), 380);
        }
        if (KijinKeyBind.kijinKey[4].consumeClick()) {
            modeChange(stack);
            player.displayClientMessage(Component.literal("Mode : " + modeName(stack)), true);
        }
        if (KijinKeyBind.kijinKey[5].isDown() && modeNumber(stack) == 0) {
            double reach = 12; // 見る距離
            Vec3 eyePos = player.getEyePosition();
            Vec3 look = player.getLookAngle();
            Vec3 endPos = eyePos.add(look.scale(reach));
            Vec3 vec3 = player.getLookAngle();

            AABB box1 = player.getBoundingBox()
                    .expandTowards(look.scale(reach))
                    .inflate(3.0);
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
                entity1.setDeltaMovement(vec3.x * -1.4, vec3.y * 2, vec3.z * -1.4);
            }
        } else if (KijinKeyBind.kijinKey[5].isDown() && modeNumber(stack) == 1) {
            BlockPos origin = player.getOnPos();
            double radius = 3.0;
            AABB searchArea = new AABB (
                    origin.getX() - radius, origin.getY() - radius, origin.getZ() - radius,
                    origin.getX() + radius, origin.getY() + radius, origin.getZ() + radius
            );
            List<Entity> entities = level.getEntitiesOfClass(Entity.class, searchArea);
            for (int i = 0; i < entities.size(); i++) {
                Entity e = entities.get(i);
                if (e != player) {
                    e.push(0.5, 0.2, 0.5);
                }
            }
        }
        if (player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == KijinModItems.RED_LIGHT_SABER.get()) {

            if (KijinKeyBind.kijinKey[7].consumeClick()) {
                double reach = 3.5; // 見る距離
                Vec3 eyePos = player.getEyePosition();
                Vec3 look = player.getLookAngle();
                Vec3 endPos = eyePos.add(look.scale(reach));

                AABB box1 = player.getBoundingBox()
                        .expandTowards(look.scale(reach))
                        .inflate(3.0);
                EntityHitResult result = ProjectileUtil.getEntityHitResult(
                        player.level(),
                        player,
                        eyePos,
                        endPos,
                        box1,
                        entity1 -> entity1 instanceof LivingEntity
                );
                if (result != null && !level.isClientSide) {
                    Entity target = result.getEntity();
                    ServerLevel serverLevel = (ServerLevel) level;
                    if (target instanceof LivingEntity) {
                        long startTime = System.currentTimeMillis();
                        Timer timer = new Timer();
                        timer.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                // サーバースレッドで実行
                                serverLevel.getServer().execute(() -> {
                                    if (System.currentTimeMillis() - startTime >= 3000 || !target.isAlive()) {
                                        this.cancel();
                                        timer.cancel();
                                        return;
                                    }
                                    ((LivingEntity) target).addEffect(new MobEffectInstance(MobEffects.LEVITATION, 160, 0, false, false));
                                    ((LivingEntity) target).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 160, 50, false, false));
                                    target.hurt(level.damageSources().freeze(), 5F);

                                });
                            }
                        }, 0, 500);
                    }
                }
            }
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        if (modeNumber(stack) == 0) {
            return false;
        }
        return true;
    }

    private void modeChange(ItemStack stack) {
        if (stack.getTag() == null) {
            stack.setTag(new CompoundTag());
        }
        stack.getTag().putInt("mode", modeNumber(stack) < 1 ? modeNumber(stack) + 1 : 0);
    }

    public int modeNumber(ItemStack stack) {
        if (stack.getTag() == null) {
            return 0;
        }
        return stack.getTag().getInt("mode");
    }

    public String modeName(ItemStack stack) {
        return switch (modeNumber(stack)) {
            case 0 -> "Pull";
            case 1 -> "Push";
            default -> "Unknown";
        };
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(Component.literal("Mode : " + modeName(stack)).withStyle(ChatFormatting.DARK_RED));
    }
}
