package pigcart.particlerain;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = ParticleRainClient.MOD_ID)
public class ModConfig implements ConfigData {
    public static class ParticleConfig {
        @ConfigEntry.Gui.Tooltip
        public boolean enabled = false;

        @ConfigEntry.Gui.Tooltip
        public int density = 200;

        @ConfigEntry.Gui.Tooltip
        public int stormDensity = 600;

        @ConfigEntry.Gui.Tooltip
        public float gravity = 1;

        @ConfigEntry.Gui.Tooltip
        public float stormGravity = 1;

        @ConfigEntry.Gui.Tooltip
        public float wind = 0;

        @ConfigEntry.Gui.Tooltip
        public float stormWind = 0;

        public ParticleConfig(boolean enabled, int density, int stormDensity, float gravity, float stormGravity, float wind, float stormWind) {
            this.enabled = enabled;

            this.density = density;
            this.stormDensity = stormDensity;

            this.gravity = gravity;
            this.stormGravity = stormGravity;

            this.wind = wind;
            this.stormWind = stormWind;
        }
    }

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public ParticleConfig rain = new ParticleConfig(true, 500, 1400, 1.5f, 1.5f, 0, 0.2f);

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public ParticleConfig snow = new ParticleConfig(true, 500, 1600, 0.15f, 0.125f, 0.15f, 0.6f);

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public ParticleConfig sand = new ParticleConfig(true, 300, 1000, 0.2f, 0.15f, 0.4f, 0.9f);

    @ConfigEntry.Gui.Tooltip
    public int radius = 24;
}