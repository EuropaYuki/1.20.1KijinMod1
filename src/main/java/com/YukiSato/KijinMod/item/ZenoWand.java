package com.YukiSato.KijinMod.item;

import com.YukiSato.KijinMod.regi.KijinModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class ZenoWand extends Item {
    public ZenoWand() {
        super(new Properties().rarity(ExtraRarity.ULTIMATE).fireResistant().durability(2));
    }
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        Player player = (Player) entity;
        if (player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == KijinModItems.ZENO_WAND.get()) {
            if (!level.isClientSide && stack.getDamageValue() == 1) {
                player.getCooldowns().addCooldown(this, 200);
                stack.setDamageValue(stack.getDamageValue() - 2);
            }
        }
    }
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (!world.isClientSide) {
            int radius = 7; // 消去する範囲の半径

            // ブロックを消去、ただしダイヤモンド鉱石と鉄鉱石は除外
            BlockPos.betweenClosedStream(pos.offset(-radius, -radius, -radius), pos.offset(radius, radius, radius))
                    .forEach(blockPos -> {
                        if (world.getBlockState(blockPos).getBlock() != Blocks.DIAMOND_ORE && world.getBlockState(blockPos).getBlock() != Blocks.IRON_ORE && world.getBlockState(blockPos).getBlock() != Blocks.DEEPSLATE_DIAMOND_ORE && world.getBlockState(blockPos).getBlock() != Blocks.DEEPSLATE_IRON_ORE) {
                            world.removeBlock(blockPos, false);
                        }
                    });

            // エンティティを消去
            List<Entity> entities = world.getEntities(context.getPlayer(), context.getPlayer().getBoundingBox().inflate(radius));
            for (Entity entity : entities) {
                if (!(entity instanceof Player)) { // プレイヤーは除外
                    entity.remove(Entity.RemovalReason.DISCARDED);
                }
            }

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

}
