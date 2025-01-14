package pigcart.particlerain;

import java.util.Random;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import pigcart.particlerain.ModConfig.ParticleConfig;

public final class WeatherParticleSpawner {
    private static Vec3 pos = new Vec3(0, 0, 0);

    public static void update(ClientLevel level, Entity entity, float partialTicks) {
        if (level.isRaining()) {
            Holder<Biome> biome = level.getBiome(entity.blockPosition());
            ParticleConfig particleConfig = null;

            if (biome.value().getPrecipitation() == Precipitation.RAIN) {
                particleConfig = ParticleRainClient.INSTANCE.config.rain;
            } else if (biome.value().getPrecipitation() == Precipitation.SNOW) {
                particleConfig = ParticleRainClient.INSTANCE.config.snow;
            } else if (biome.is(Biomes.DESERT) || biome.is(BiomeTags.IS_BADLANDS)) {
                particleConfig = ParticleRainClient.INSTANCE.config.sand;
            }

            if (particleConfig != null && particleConfig.enabled) {
                int density = (int) ((!level.isThundering() ? particleConfig.density : particleConfig.stormDensity) * level.getRainLevel(partialTicks));

                Random rand = new Random();

                for (int pass = 0; pass < density; pass++) {
                    double radius = 2.5d + rand.nextFloat(ParticleRainClient.INSTANCE.config.radius);
                    double theta = rand.nextFloat((float) (Math.PI * 2));
                    double phi = Math.acos((rand.nextFloat() * 2) - 1);
                    double x = radius * Math.sin(phi) * Math.cos(theta);
                    double y = radius * Math.sin(phi) * Math.sin(theta);
                    double z = radius * Math.cos(phi);

                    pos = new Vec3(x + entity.getX(), y + entity.getY(), z + entity.getZ());

                    if (level.getHeight(Heightmap.Types.MOTION_BLOCKING, Math.round((float) pos.x), Math.round((float) pos.z)) > pos.y) {
                        continue;
                    }

                    if (biome.value().getPrecipitation() == Precipitation.RAIN) {
                        level.addParticle(ParticleRainClient.INSTANCE.RAIN_DROP, pos.x, pos.y, pos.z, 1, 1, 1);
                    } else if (biome.value().getPrecipitation() == Precipitation.SNOW) {
                        level.addParticle(ParticleRainClient.INSTANCE.SNOW_FLAKE, pos.x, pos.y, pos.z, 1, 1, 1);
                    } else {
                        if (biome.is(Biomes.DESERT)) {
                            level.addParticle(ParticleRainClient.INSTANCE.DESERT_DUST, pos.x, pos.y, pos.z, 0.9f, 0.8f, 0.6f);
                        } else if (biome.is(BiomeTags.IS_BADLANDS)) {
                            level.addParticle(ParticleRainClient.INSTANCE.DESERT_DUST, pos.x, pos.y, pos.z, 0.8f, 0.4f, 0);
                        }
                    }
                }
            }
        }
    }

    public static SoundEvent getBiomeSound(Holder<Biome> biome, boolean above) {
        if (ParticleRainClient.INSTANCE.config.rain.enabled && biome.value().getPrecipitation() == Precipitation.RAIN) {
            return !above ? SoundEvents.WEATHER_RAIN : SoundEvents.WEATHER_RAIN_ABOVE;
        } else if (ParticleRainClient.INSTANCE.config.snow.enabled && biome.value().getPrecipitation() == Precipitation.SNOW) {
            return !above ? ParticleRainClient.INSTANCE.WEATHER_SNOW : ParticleRainClient.INSTANCE.WEATHER_SNOW_ABOVE;
        } else if (ParticleRainClient.INSTANCE.config.sand.enabled) {
            return !above ? ParticleRainClient.INSTANCE.WEATHER_SANDSTORM : ParticleRainClient.INSTANCE.WEATHER_SANDSTORM_ABOVE;
        }

        return null;
    }
}