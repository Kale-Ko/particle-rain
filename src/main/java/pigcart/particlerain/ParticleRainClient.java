package pigcart.particlerain;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import pigcart.particlerain.particle.DesertDustParticle;
import pigcart.particlerain.particle.RainDropParticle;
import pigcart.particlerain.particle.SnowFlakeParticle;

public class ParticleRainClient implements ClientModInitializer {
    public static final String MOD_ID = "particlerain";

    public static ParticleRainClient INSTANCE;

    public SimpleParticleType RAIN_DROP;
    public SimpleParticleType SNOW_FLAKE;
    public SimpleParticleType DESERT_DUST;

    public SoundEvent WEATHER_SNOW;
    public SoundEvent WEATHER_SNOW_ABOVE;
    public SoundEvent WEATHER_SANDSTORM;
    public SoundEvent WEATHER_SANDSTORM_ABOVE;

    public ModConfig config;

    @Override
    public void onInitializeClient() {
        ParticleRainClient.INSTANCE = this;

        RAIN_DROP = Registry.register(BuiltInRegistries.PARTICLE_TYPE, new ResourceLocation(MOD_ID, "rain_drop"), FabricParticleTypes.simple(true));
        SNOW_FLAKE = Registry.register(BuiltInRegistries.PARTICLE_TYPE, new ResourceLocation(MOD_ID, "snow_flake"), FabricParticleTypes.simple(true));
        DESERT_DUST = Registry.register(BuiltInRegistries.PARTICLE_TYPE, new ResourceLocation(MOD_ID, "desert_dust"), FabricParticleTypes.simple(true));

        WEATHER_SNOW = registerSound("weather.snow");
        WEATHER_SNOW_ABOVE = registerSound("weather.snow.above");
        WEATHER_SANDSTORM = registerSound("weather.sandstorm");
        WEATHER_SANDSTORM_ABOVE = registerSound("weather.sandstorm.above");

        ParticleFactoryRegistry.getInstance().register(RAIN_DROP, RainDropParticle.DefaultFactory::new);
        ParticleFactoryRegistry.getInstance().register(SNOW_FLAKE, SnowFlakeParticle.DefaultFactory::new);
        ParticleFactoryRegistry.getInstance().register(DESERT_DUST, DesertDustParticle.DefaultFactory::new);

        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void onTick(Minecraft client) {
        if (!client.isPaused() && client.level != null && client.getCameraEntity() != null) {
            WeatherParticleSpawner.update(client.level, client.getCameraEntity(), client.getFrameTime());
        }
    }

    private static SoundEvent registerSound(String name) {
        ResourceLocation id = new ResourceLocation(MOD_ID, name);

        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }
}