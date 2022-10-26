package pigcart.particlerain.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import pigcart.particlerain.ModConfig.ParticleConfig;
import pigcart.particlerain.ParticleRainClient;

public abstract class WeatherParticle extends TextureSheetParticle {
    protected ParticleConfig particleConfig;

    protected final BlockPos.MutableBlockPos pos;

    protected WeatherParticle(ClientLevel level, double x, double y, double z, float red, float green, float blue, SpriteSet provider, ParticleConfig particleConfig) {
        super(level, x, y, z, red, green, blue);

        this.particleConfig = particleConfig;

        this.setSprite(provider.get(level.getRandom()));

        this.xd = (!this.level.isThundering() ? particleConfig.wind : particleConfig.stormWind);
        this.yd = -(!this.level.isThundering() ? particleConfig.gravity : particleConfig.stormGravity);
        this.zd = 0.0f;

        this.rCol = red;
        this.gCol = green;
        this.bCol = blue;

        this.quadSize = 0.1f;

        this.pos = new BlockPos.MutableBlockPos();
    }

    @Override
    public void tick() {
        this.age = 0;
        this.lifetime = Integer.MAX_VALUE;

        this.xd = (!this.level.isThundering() ? particleConfig.wind : particleConfig.stormWind);
        this.yd = -(!this.level.isThundering() ? particleConfig.gravity : particleConfig.stormGravity);
        this.zd = 0.0f;

        super.tick();

        this.pos.set(this.x, this.y, this.z);
    }

    protected boolean shouldRemove() {
        Entity cameraEntity = Minecraft.getInstance().getCameraEntity();

        return cameraEntity == null || cameraEntity.distanceToSqr(this.x, this.y, this.z) > Math.pow(ParticleRainClient.INSTANCE.config.radius + 2, 2);
    }
}