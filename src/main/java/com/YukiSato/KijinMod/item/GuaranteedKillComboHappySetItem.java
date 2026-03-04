package com.YukiSato.KijinMod.item;

import com.YukiSato.KijinMod.regi.KijinModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class GuaranteedKillComboHappySetItem extends Item {
    public GuaranteedKillComboHappySetItem() {
        super(new Properties().fireResistant().rarity(ExtraRarity.ULTIMATE));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        Player player = (Player) entity;
        if (!level.isClientSide && player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == KijinModItems.GUARANTEED_KILL_COMBO_HAPPY_SET_ITEM.get()) {
            // プレイヤーの周囲のモブを取得
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, new AABB(
                    level.getWorldBorder().getMinX(), level.getMinBuildHeight(), level.getWorldBorder().getMinZ(),
                    level.getWorldBorder().getMaxX(), level.getMaxBuildHeight(), level.getWorldBorder().getMaxZ()));

            for (LivingEntity mob : entities) {
                if (!(mob instanceof Player)) { // プレイヤー自身は殺さない
                    mob.setNoGravity(true);
                    mob.setHealth(0.01F);
                }
            }
        }
    }
}
