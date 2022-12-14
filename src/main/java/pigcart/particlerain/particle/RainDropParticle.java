package pigcart.particlerain.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.Fluids;
import pigcart.particlerain.ParticleRainClient;

public class RainDropParticle extends WeatherParticle {
    private final BlockPos.MutableBlockPos fluidPos;

    protected RainDropParticle(ClientLevel clientWorld, double x, double y, double z, float red, float green, float blue, SpriteSet provider) {
        super(clientWorld, x, y, z, red, green, blue, provider, ParticleRainClient.INSTANCE.config.rain);

        this.quadSize = 0.5f;

        this.fluidPos = new BlockPos.MutableBlockPos();
    }

    @Override
    public void tick() {
        super.tick();

        this.fluidPos.set(this.x, this.y - 0.1, this.z);

        if (this.shouldRemove() || this.onGround || this.y < this.level.getMinBuildHeight() || this.level.getFluidState(this.pos).getType() != Fluids.EMPTY) {
            if (this.onGround || this.level.getFluidState(this.fluidPos).is(FluidTags.WATER)) {
                Minecraft minecraft = Minecraft.getInstance(); // So the resource isn't leaked
                minecraft.particleEngine.createParticle(ParticleTypes.RAIN, this.x, this.y, this.z, 0, 0, 0);
            }

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
            return new RainDropParticle(level, x, y, z, (float) red, (float) green, (float) blue, this.provider);
        }
    }
}