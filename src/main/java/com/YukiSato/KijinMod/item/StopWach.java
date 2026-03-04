package com.YukiSato.KijinMod.item;

import com.YukiSato.KijinMod.regi.KijinModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class StopWach extends Item {
    private static boolean timeStopped = false;
    private static InteractionHand hand;
    private static long timeStoppedStart = 0;
    private static final long TIME_STOP_DURATION = Integer.MAX_VALUE; // 10 seconds in ticks (20 ticks per second)
    public StopWach() {
        super(new Properties().rarity(ExtraRarity.ULTIMATE).fireResistant().stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        Player player = context.getPlayer();

        if (!world.isClientSide && player != null) {
            timeStopped = true;
            timeStoppedStart = world.getGameTime();
            player.sendSystemMessage(Component.literal("Time has stopped!"));
            world.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        return InteractionResult.SUCCESS;
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.LevelTickEvent event) {
        Player player = Minecraft.getInstance().player;
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() == KijinModItems.STOP_WACH.get()) {
            if (timeStopped && event.phase == TickEvent.Phase.START && player.isSteppingCarefully()) {
                player.startUsingItem(hand);
                Level world = event.level;

                if (world.getGameTime() - timeStoppedStart > TIME_STOP_DURATION) {
                    timeStopped = false;
                    return;
                }

                // エンティティの動きを停止
                List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, new AABB(
                        world.getWorldBorder().getMinX(), world.getMinBuildHeight(), world.getWorldBorder().getMinZ(),
                        world.getWorldBorder().getMaxX(), world.getMaxBuildHeight(), world.getWorldBorder().getMaxZ()));

                for (LivingEntity entity : entities) {
                    if (entity instanceof Mob) {
                        ((Mob) entity).setNoAi(true);
                        ((Mob) entity).getNavigation().stop();
                    }
                }

                // ブロックの更新を停止
                event.setCanceled(true);
            }
        }

    }
}
