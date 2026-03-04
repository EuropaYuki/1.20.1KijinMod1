package com.YukiSato.KijinMod.item.tool;

import com.YukiSato.KijinMod.item.ExtraRarity;
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
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KijinPickaxe extends PickaxeItem {
    public KijinPickaxe() {
        super(KijinModTiers.SAKURITE, 2, -2.8F, new Properties().fireResistant().rarity(ExtraRarity.ULTIMATE));
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
    public boolean isCorrectToolForDrops(BlockState blockState) {
        // ここで岩盤を掘れるようにします
        return blockState.is(Blocks.BEDROCK) || super.isCorrectToolForDrops(blockState);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return switch (modeNumber(stack)){
            case 0 -> state.is(BlockTags.MINEABLE_WITH_PICKAXE) ? KijinModTiers.SAKURITE.getSpeed() : 1.0F;
            case 1 -> state.is(BlockTags.MINEABLE_WITH_PICKAXE) ? KijinModTiers.SAKURITE.getSpeed() * 3 : 1.0F;
            default -> 0F;
        };
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
            case 0 -> "Normal";
            case 1 -> "Tactical";
            default -> "Unknown";
        };
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(Component.literal("Mode : " + modeName(stack)).withStyle(ChatFormatting.AQUA));
    }
}
