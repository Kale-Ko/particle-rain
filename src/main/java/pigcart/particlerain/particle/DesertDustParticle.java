package pigcart.particlerain.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.material.Fluids;
import pigcart.particlerain.ParticleRainClient;

public class DesertDustParticle extends WeatherParticle {
    protected float beenOnGround = -1;

    private DesertDustParticle(ClientLevel clientWorld, double x, double y, double z, float red, float green, float blue, SpriteSet provider) {
        super(clientWorld, x, y, z, red, green, blue, provider, ParticleRainClient.INSTANCE.config.sand);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.shouldRemove() || this.xo == this.x || this.y < this.level.getMinBuildHeight() || this.level.getFluidState(this.pos).getType() != Fluids.EMPTY) {
            this.remove();
        }

        if (this.onGround) {
            this.beenOnGround = 1;
        }

        if (this.beenOnGround > -1) {
            if (this.beenOnGround >= 0) {
                this.yd = this.beenOnGround * 0.25;
            } else {
                this.yd = -(!this.level.isThundering() ? particleConfig.gravity : particleConfig.stormGravity) * -this.beenOnGround;
            }

            this.beenOnGround -= 0.1;
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class DefaultFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet provider;

        public DefaultFactory(SpriteSet provider) {
            this.provider = provider;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientLevel level, double x, double y, double z, double red, double green, double blue) {
            return new DesertDustParticle(level, x, y, z, (float) red, (float) green, (float) blue, this.provider);
        }
    }
}