package com.YukiSato.KijinMod.item;

import com.YukiSato.KijinMod.entity.KijinAmmoEntity;
import com.YukiSato.KijinMod.entity.LazerEntity;
import com.YukiSato.KijinMod.keybind.KijinKeyBind;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class MagicWand extends BowItem {
    int we;
    public static final Predicate<ItemStack> AMMO = stack -> stack.getItem() == Items.AIR;

    public MagicWand() {
        super(new Properties().fireResistant().rarity(ExtraRarity.ULTIMATE));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ItemStack itemStack = player.getProjectile(stack);
        Vec3 vec3 = player.getLookAngle();
        if (!level.isClientSide && itemStack.getItem() == Items.AIR) {
            ItemStack ammo = new ItemStack(Items.AIR);
            KijinAmmoEntity ammoEntity = new KijinAmmoEntity(level, player, ammo);
            ammoEntity.setNoGravity(true);
            ammoEntity.shoot(vec3.x, vec3.y, vec3.z, 45F, 0F);
            level.addFreshEntity(ammoEntity);
            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
            stack.hurtAndBreak(1, player, (b) ->{
                b.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        Player player1 = (Player) entity;
        if (KijinKeyBind.kijinKey[7].consumeClick()) {
            we = 1;
            player1.displayClientMessage(Component.literal("Jodragirum : " + MagicName()), true);
        } else if (KijinKeyBind.kijinKey[5].consumeClick()) {
            we = 0;
            player1.displayClientMessage(Component.literal("Jodragirum : " + MagicName()), true);
        }
        if (we == 1) {
            double reach = 20.0; // 見る距離
            Vec3 eyePos = player1.getEyePosition();
            Vec3 look = player1.getLookAngle();
            Vec3 endPos = eyePos.add(look.scale(reach));

            AABB box1 = player1.getBoundingBox()
                    .expandTowards(look.scale(reach))
                    .inflate(1.0);

            EntityHitResult result = ProjectileUtil.getEntityHitResult(
                    player1.level(),
                    player1,
                    eyePos,
                    endPos,
                    box1,
                    entity1 -> entity1 instanceof Mob && entity1 != player1
            );
            if (result != null && !level.isClientSide) {
                Mob target = (Mob) result.getEntity();
                target.setNoAi(true);
            }
        }
    }
    public String MagicName() {
        return switch (we) {
            case 0 -> "OFF";
            case 1 -> "ON";
            default -> "Unknown";
        };
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        components.add(Component.literal("Jodragirum : " + MagicName()).withStyle(ChatFormatting.DARK_PURPLE));
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return AMMO;
    }
}
