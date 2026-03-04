package com.YukiSato.KijinMod.item;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class MetalDetectorItem extends Item {
    public MetalDetectorItem() {
        super(new Properties().rarity(Rarity.EPIC).fireResistant().durability(500));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        if (!context.getLevel().isClientSide) {
            BlockPos pos = context.getClickedPos();
            Player player = context.getPlayer();
            boolean foundBlock = false;

            for (int i = 0; i <= pos.getY() + 64; i++) {
                BlockState blockState = context.getLevel().getBlockState(pos.below(i));
                if (isValuableBlock(blockState)) {
                    outputValuableCoordinates(pos.below(i), player, blockState.getBlock());
                    foundBlock = true;

                    break;
                }

            }
            if (!foundBlock) {
                player.sendSystemMessage(Component.literal("No valuables Found!"));
            }
        }

        context.getItemInHand().hurtAndBreak(1, context.getPlayer(),
                player -> player.broadcastBreakEvent(player.getUsedItemHand()));

        return super.useOn(context);
    }

    private void outputValuableCoordinates(BlockPos blockPos, Player player, Block block) {
        player.sendSystemMessage(Component.literal("Found" + I18n.get(block.getDescriptionId()) + " at " +
                "(" + blockPos.getX() + "," + blockPos.getY() + "," + blockPos.getZ() + ")"));
    }

    private boolean isValuableBlock(BlockState blockState) {
        return blockState.is(Blocks.DIAMOND_ORE) || blockState.is(Blocks.IRON_ORE);
    }
}
