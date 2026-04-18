package com.YukiSato.KijinMod.entity.client;

import com.YukiSato.KijinMod.entity.custom.NinjaEntity;
import com.YukiSato.KijinMod.main.KijinMod;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class NinjaRender extends MobRenderer<NinjaEntity, NinjaModel<NinjaEntity>> {
    public NinjaRender(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new NinjaModel<>(p_174304_.bakeLayer(MobModelLayers.NINJA_LAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(NinjaEntity p_114482_) {
        return new ResourceLocation(KijinMod.MOD_ID, "textures/entity/ninja.png");
    }

    @Override
    public void render(NinjaEntity entity, float ni, float san, PoseStack stack, MultiBufferSource mo, int g) {

        if (entity.isBaby()) {
            stack.scale(0.5f, 0.5f, 0.5f);
        }
        super.render(entity, ni, san, stack, mo, g);
    }
}
