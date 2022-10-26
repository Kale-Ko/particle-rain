package pigcart.particlerain.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.material.Fluids;
import pigcart.particlerain.ParticleRainClient;

public class SnowFlakeParticle extends WeatherParticle {
    private SnowFlakeParticle(ClientLevel level, double x, double y, double z, float red, float green, float blue, SpriteSet provider) {
        super(level, x, y, z, red, green, blue, provider, ParticleRainClient.INSTANCE.config.snow);

        this.setSize(0.1f, 0.1f);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.shouldRemove() || this.onGround || this.y < this.level.getMinBuildHeight() || this.level.getFluidState(this.pos).getType() != Fluids.EMPTY) {
            this.remove();
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class DefaultFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet provider;

        public DefaultFactory(SpriteSet provider) {
            this.provider = provider;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientLevel level, double x, double y, double z, double red, double green, double blue) {
            return new SnowFlakeParticle(level, x, y, z, (float) red, (float) green, (float) blue, this.provider);
        }
    }
}