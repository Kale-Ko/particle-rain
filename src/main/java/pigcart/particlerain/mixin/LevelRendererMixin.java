package pigcart.particlerain.mixin;

import java.util.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.Heightmap;
import pigcart.particlerain.WeatherParticleSpawner;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    private int ticks;

    @Shadow
    private int rainSoundTime;

    @Inject(method = "tickRain", at = @At("HEAD"), cancellable = true)
    public void tickRain(Camera camera, CallbackInfo ci) {
        float f = this.minecraft.level.getRainLevel(1.0f);

        if (f > 0.0f) {
            Random random = new Random((long) this.ticks * 312987231L);
            LevelReader level = this.minecraft.level;
            BlockPos blockPos = new BlockPos(camera.getPosition());
            BlockPos blockPos2 = null;

            for (int j = 0; j < 100.0f * f * f; ++j) {
                int k = random.nextInt(21) - 10;
                int l = random.nextInt(21) - 10;
                BlockPos blockPos3 = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockPos.offset(k, 0, l));

                if (blockPos3.getY() > level.getMinBuildHeight() && blockPos3.getY() <= blockPos.getY() + 10 && blockPos3.getY() >= blockPos.getY() - 10) {
                    blockPos2 = blockPos3.below();
                }
            }

            if (blockPos2 != null && random.nextInt(3) < this.rainSoundTime++) {
                this.rainSoundTime = 0;

                if (blockPos2.getY() > blockPos.getY() + 1 && level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockPos).getY() > Mth.floor((float) blockPos.getY())) {
                    SoundEvent sound = WeatherParticleSpawner.getBiomeSound(level.getBiome(blockPos2), true);

                    if (sound != null) {
                        this.minecraft.level.playLocalSound(blockPos2, sound, SoundSource.WEATHER, 0.1f, 0.5f, false);
                    }
                } else {
                    SoundEvent sound = WeatherParticleSpawner.getBiomeSound(level.getBiome(blockPos2), false);

                    if (sound != null) {
                        this.minecraft.level.playLocalSound(blockPos2, sound, SoundSource.WEATHER, 0.2f, 1.0f, false);
                    }
                }
            }
        }

        ci.cancel();
    }

    @Inject(method = "renderSnowAndRain", at = @At("HEAD"), cancellable = true)
    public void renderWeather(LightTexture lightTexture, float partialTicks, double x, double y, double z, CallbackInfo ci) {
        ci.cancel();
    }
}