package com.YukiSato.KijinMod.item.tool;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KijinAxe extends AxeItem {
    public KijinAxe() {
        super(KijinModTiers.SAKURITE, 7F, -2.9F, new Properties().fireResistant().rarity(Rarity.EPIC));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (player.isSteppingCarefully()) {
            player.startUsingItem(hand);
            modeChange(itemstack);
            player.displayClientMessage(Component.literal("Mode : " + modeName(itemstack)), true);
            level.playSound(player, player, SoundEvents.IRON_DOOR_OPEN, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        return InteractionResultHolder.fail(itemstack);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        Block block = state.getBlock();
        int a [] = {-3, -2, -1, 0, 1, 2, 3};
        for (int ax = 0; ax < 7; ax++) {
            for (int az = 0; az < 7; az++) {
                for (int y = 0; y < 50; y++) {
                    BlockPos aPos = new BlockPos(pos.getX() + a[ax], pos.getY() + y, pos.getZ() + a[az]);
                    if (level.getBlockState(aPos).getBlock() == block && modeNumber(stack) == 1) {
                        level.destroyBlock(aPos, true);
                    }
                }
            }
        }
        return true;
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
            case 0 -> "Normal Mode";
            case 1 -> "Bunyan Mode";
            default -> "Unknown";
        };
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(Component.literal("Mode : " + modeName(stack)).withStyle(ChatFormatting.LIGHT_PURPLE));
    }
}
