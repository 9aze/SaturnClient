package lat.saturn.api.setting.settings;

import lat.saturn.api.setting.Setting;
import lat.saturn.api.util.render.RenderUtils;
import lat.saturn.feature.module.client.ColorModule;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class ColorSetting extends Setting<Color, ColorSetting> {
    @Getter
    private final boolean allowRainbow;
    @Getter
    private final boolean allowAlpha;
    @Getter
    private final boolean allowSync;
    @Setter
    private boolean rainbow;
    @Setter
    private boolean sync;

    public ColorSetting(String name, String description, Color defaultValue, boolean allowRainbow, boolean allowAlpha, boolean rainbow, boolean allowSync, boolean sync) {
        super(name, description, defaultValue);
        this.allowRainbow = allowRainbow;
        this.allowAlpha = allowAlpha;
        this.rainbow = rainbow;
        this.allowSync = allowSync;
        this.sync = sync;
    }

    public ColorSetting(String name, String description, Color defaultValue, boolean allowRainbow, boolean allowAlpha, boolean rainbow, boolean allowSync) {
        this(name, description, defaultValue, allowRainbow, allowAlpha, rainbow, allowSync, false);
    }

    public ColorSetting(String name, String description, Color defaultValue, boolean allowRainbow, boolean allowAlpha, boolean rainbow) {
        this(name, description, defaultValue, allowRainbow, allowAlpha, rainbow, true);
    }

    public ColorSetting(String name, String description, Color defaultValue, boolean allowRainbow, boolean allowAlpha) {
        this(name, description, defaultValue, allowRainbow, allowAlpha, false);
    }

    public ColorSetting(String name, String description, Color defaultValue, boolean allowRainbow) {
        this(name, description, defaultValue, allowRainbow, true);
    }

    public ColorSetting(String name, String description, Color defaultValue) {
        this(name, description, defaultValue, true);
    }

    @Override
    public Color getValue() {
        if(allowSync && sync && ColorModule.INSTANCE != null) return ColorModule.INSTANCE.clientColor.getValue();
        return rainbow ? getRainbow(value.getAlpha()) : super.getValue();
    }

    public Color getValue(int alpha) {
        if (allowSync && sync && ColorModule.INSTANCE != null) {
            Color color = ColorModule.INSTANCE.clientColor.getValue();
            return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
        }

        Color color = rainbow ? getRainbow(alpha) : super.getValue();
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    private Color getRainbow(int alpha) {
        if(ColorModule.INSTANCE == null) return super.getValue();
        double speed = ColorModule.INSTANCE.rainbowSpeed.getValue();
        double brightness = ColorModule.INSTANCE.rainbowBrightness.getValue();
        double saturation = ColorModule.INSTANCE.rainbowSaturation.getValue();

        long currentTime = System.currentTimeMillis();
        double cycleDuration = 20000.0f / speed;
        float hue = (float) ((currentTime % (long) cycleDuration) / cycleDuration);

        Color rainbowColor = new Color(Color.HSBtoRGB(hue, (float) saturation, (float) brightness));
        return allowAlpha ? RenderUtils.setAlpha(rainbowColor, alpha) : rainbowColor;
    }


}
