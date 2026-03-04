package com.YukiSato.KijinMod.item.tool;

import com.YukiSato.KijinMod.item.ExtraRarity;
import com.YukiSato.KijinMod.regi.KijinModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KijinHoe extends HoeItem {
    public KijinHoe() {
        super(KijinModTiers.SAKURITE, -5, 6, new Properties().fireResistant().rarity(ExtraRarity.ULTIMATE));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (player.isSteppingCarefully()) {
            player.startUsingItem(hand);
            modeChange(itemstack);
            player.displayClientMessage(Component.literal("Mode : " + modeName(itemstack)), true);
            if (itemstack.getTag().getInt("mode") == 1) {
                itemstack.getEnchantmentTags().clear();
                itemstack.enchant(Enchantments.SHARPNESS, attackLevel(itemstack));
            } else {
                itemstack.getEnchantmentTags().clear();
            }
            level.playSound(player, player, SoundEvents.IRON_DOOR_OPEN, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        return InteractionResultHolder.fail(itemstack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int n, boolean f) {
        if (stack.getTag().getInt("mode") == 0 && entity.tickCount % 200 == 0) {
            stack.setDamageValue(stack.getDamageValue() - 10);
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
            case 0 -> "Charge Mode";
            case 1 -> "Anima Mode";
            default -> "Unknown";
        };
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (stack.getTag().getInt("mode") == 1) {
            ItemEntity dropItem = new ItemEntity (
                    target.level(),
                    target.getX(),
                    target.getY(),
                    target.getZ(),
                    new ItemStack(KijinModItems.KIJIN_BURGER.get(), target.getRandom().nextInt(3) + 1)
            );


            target.level().addFreshEntity(dropItem);
        } else {
            saveHitCount(stack);
        }
        stack.hurtAndBreak(1, attacker, (entity -> {
            entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        }));
        return true;
    }

    private void saveHitCount(ItemStack stack) {
        if (stack.getTag() == null) {
            stack.setTag(new CompoundTag());
        }
        stack.getTag().putInt("hit", hitNumber(stack) + 1);
    }

    public int hitNumber(ItemStack stack) {
        if (stack.getTag() == null) {
            return 0;
        }
        return stack.getTag().getInt("hit");
    }

    public int attackLevel(ItemStack stack) {
        if (stack.getTag() == null) {
            return 0;
        }
        double a = hitNumber(stack) / 10;

        double b = Math.sqrt(a);
        double c = Math.floor(b);

        return c < 20 ? (int) c : 20;
    }

    public int nextLevel(ItemStack stack) {
        int a = attackLevel(stack) + 1;
        int b = a * a;
        int c = b * 10;
        int d = c - hitNumber(stack);
        return attackLevel(stack) < 20 ? d : 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(Component.literal("Mode : " + modeName(stack)).withStyle(ChatFormatting.AQUA));
        list.add(Component.literal("Level : " + attackLevel(stack)).withStyle(ChatFormatting.LIGHT_PURPLE));
        list.add(Component.literal("Next : " + nextLevel(stack)).withStyle(ChatFormatting.RED));
    }

}
