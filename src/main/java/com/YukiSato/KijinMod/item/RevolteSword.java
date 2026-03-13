package com.YukiSato.KijinMod.item;

import com.YukiSato.KijinMod.item.tool.KijinModTiers;
import com.YukiSato.KijinMod.keybind.KijinKeyBind;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.ibm.icu.impl.locale.XCldrStub;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class RevolteSword extends SwordItem {

    private int mode = 0;
    public RevolteSword() {
        super(KijinModTiers.SAKURITE, -1, -1.5F, new Properties().rarity(Rarity.EPIC).fireResistant());
    }



    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        if (KijinKeyBind.kijinKey[11].consumeClick()) {
            mode = 1;
        } else if (KijinKeyBind.kijinKey[12].consumeClick()) {
            mode = 0;
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(
            EquipmentSlot slot) {

        Multimap<Attribute, AttributeModifier> map = HashMultimap.create();

        if (slot == EquipmentSlot.MAINHAND) {
            if (mode == 1) {
                map.put(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_UUID,
                                "Weapon modifier",
                                18.0D,
                                AttributeModifier.Operation.ADDITION));

                map.put(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_UUID,
                                "Weapon modifier",
                                -3.5F,
                                AttributeModifier.Operation.ADDITION));
            } else if (mode == 0) {
                map.put(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_UUID,
                                "Weapon modifier",
                                5.0D,
                                AttributeModifier.Operation.ADDITION));

                map.put(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_UUID,
                                "Weapon modifier",
                                -1.5F,
                                AttributeModifier.Operation.ADDITION));
            }

        }

        return map;
    }


}
