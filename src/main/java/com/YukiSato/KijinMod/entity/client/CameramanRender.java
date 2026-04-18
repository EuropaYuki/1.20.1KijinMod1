package com.YukiSato.KijinMod.entity.client;

import com.YukiSato.KijinMod.entity.custom.CameramanEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CameramanRender extends MobRenderer<CameramanEntity, CameraManModel<CameramanEntity>> {
    public CameramanRender(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new CameraManModel<>(p_174304_.bakeLayer(MobModelLayers.CAMERAMAN_LAYER)), 0.5f);
    }

    @Override
    public void render(CameramanEntity entity, float ni, float san, PoseStack stack, MultiBufferSource mo, int g) {
        if (entity.isBaby()) {
            stack.scale(0.5f, 0.5f, 0.5f);
        }
        super.render(entity, ni, san, stack, mo, g);
    }

    @Override
    public ResourceLocation getTextureLocation(CameramanEntity p_114482_) {
        return new ResourceLocation("kijinmod", "textures/entity/cameraman.png");
    }
}
