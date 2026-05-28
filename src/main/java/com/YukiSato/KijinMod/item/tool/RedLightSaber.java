package com.YukiSato.KijinMod.item.tool;

import com.YukiSato.KijinMod.item.ExtraRarity;
import com.YukiSato.KijinMod.keybind.KijinKeyBind;
import com.YukiSato.KijinMod.regi.KijinModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RedLightSaber extends SwordItem {

    public RedLightSaber() {
        super(KijinModTiers.SAKURITE, Integer.MAX_VALUE, -1.7F, new Properties().fireResistant().rarity(ExtraRarity.ULTIMATE));
    }

    public UseAnim getUseAnimation(ItemStack p_43105_) {
        return UseAnim.BLOCK;
    }

    public int getUseDuration(ItemStack p_43107_) {
        return 72000;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        if (player.isSteppingCarefully()) {
            modeChange(itemstack);
            player.displayClientMessage(Component.literal("Mode : " + modeName(itemstack)), true);
        }
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_SHIELD_ACTIONS.contains(toolAction);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        Player player = (Player)entity;

        if (player.isSteppingCarefully()) {
            player.addEffect(new MobEffectInstance(MobEffects.JUMP, 20, 7, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 240, 4, false, false));
            player.getCooldowns().addCooldown(KijinModItems.RED_LIGHT_SABER.get(), 380);
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
                entity1.setDeltaMovement(vec3.x * -2, vec3.y * 2, vec3.z * -2);
            }
        } else if (KijinKeyBind.kijinKey[5].isDown() && modeNumber(stack) == 1) {
            BlockPos origin = player.getOnPos();
            double radius = 5.0;
            AABB searchArea = new AABB (
                    origin.getX() - radius, origin.getY() - radius, origin.getZ() - radius,
                    origin.getX() + radius, origin.getY() + radius, origin.getZ() + radius
            );
            List<Entity> entities = level.getEntitiesOfClass(Entity.class, searchArea);
            for (int i = 0; i < entities.size(); i++) {
                Entity e = entities.get(i);
                e.push(2, 1, 2);
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
                                    if (System.currentTimeMillis() - startTime >= 3000) {
                                        this.cancel();
                                        timer.cancel();
                                        return;
                                    }
                                    ((LivingEntity) target).addEffect(new MobEffectInstance(MobEffects.LEVITATION, 160, 0, false, false));
                                    ((LivingEntity) target).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 160, 50, false, false));
                                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 160, 3, false, false));
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
