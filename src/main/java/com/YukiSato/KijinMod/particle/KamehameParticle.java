package com.YukiSato.KijinMod.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;

public class KamehameParticle extends TextureSheetParticle{

    public KamehameParticle(ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
        super(world, x, y, z, mx, my, mz);
        this.setSize(0.1F, 0.1F); // サイズを調整可能
        this.gravity = 0.0F;      // 重力の影響を受けない
        this.lifetime = 8;        // 寿命（フレーム数）
        this.scale(2.0F);         // 爆発の大きさ

        // 色を設定
        this.rCol = 0.0F; // 赤成分
        this.gCol = 1.0F; // 緑成分
        this.bCol = 1.0F; // 青成分
    }

    @Override
    public void tick() {
        super.tick();
        this.alpha = 1.0F - ((float)this.age / (float)this.lifetime); // 寿命に合わせて透明度を設定
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;
        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
            KamehameParticle particle = new KamehameParticle(world, x, y, z, mx, my, mz);
            particle.setSpriteFromAge(spriteSet); // スプライトセットから適切なスプライトを選択
            return particle;
        }
    }
}
