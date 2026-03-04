package com.YukiSato.KijinMod.item;

import com.YukiSato.KijinMod.entity.NonFireLB;
import com.YukiSato.KijinMod.regi.KijinModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Ruby extends Item {
    public Ruby() {
        super(new Properties().rarity(ExtraRarity.ULTIMATE).fireResistant().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> lists, TooltipFlag tooltip) {
        lists.add(Component.translatable("item.kijinmod.ruby.desc").withStyle(ChatFormatting.RED));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        Player player = (Player) entity;
        BlockPos origin = player.getOnPos();
        double radius = 10.0;
        AABB searchArea = new AABB (
                origin.getX() - radius, origin.getY() - radius, origin.getZ() - radius,
                origin.getX() + radius, origin.getY() + radius, origin.getZ() + radius
        );
        if (player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == KijinModItems.RUBY.get()) {
            List<Entity> entities = level.getEntitiesOfClass(Entity.class, searchArea);
            for (int i = 0; i < entities.size(); i++) {
                Entity e = entities.get(i);
                if (e.getClassification(true) == MobCategory.MONSTER &&
                        e.getType() != EntityType.ENDERMAN) {
                    BlockPos pos = e.blockPosition();
                    if (e instanceof Mob) {
                        ((Mob) e).setNoAi(true);
                        ((Mob) e).getNavigation().stop();
                    }
                    LightningBolt nonFire = new NonFireLB(level);
                    nonFire.moveTo(Vec3.atBottomCenterOf(pos));
                    nonFire.setCause(player instanceof ServerPlayer ? (ServerPlayer) player : null);
                    level.addFreshEntity(nonFire);
                }
            }
        }
    }
}
