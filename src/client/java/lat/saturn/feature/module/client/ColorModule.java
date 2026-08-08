package lat.saturn.feature.module.client;

import lat.saturn.api.manager.module.Category;
import lat.saturn.api.manager.module.Module;
import lat.saturn.api.manager.module.RegisterModule;
import lat.saturn.api.setting.settings.ColorSetting;
import lat.saturn.api.setting.settings.DoubleSetting;

import java.awt.*;

@RegisterModule(name="Color", description = "The client's color settings.", category = Category.CLIENT, toggled = true, alwaysActive = true)
public class ColorModule extends Module {
    public static ColorModule INSTANCE = null;
    public ColorSetting clientColor = new ColorSetting("Accent Color", "Color used when syncing other color settings", new Color(255, 64, 129), true, false, false, false);
    public DoubleSetting rainbowSpeed = new DoubleSetting("Rainbow Speed", "Speed of rainbow", 1, 0, 15);
    public DoubleSetting rainbowBrightness = new DoubleSetting("Rainbow Brightness", "Brightness of rainbow", 0.8, 0, 1);
    public DoubleSetting rainbowSaturation = new DoubleSetting("Rainbow Saturation", "Saturation of rainbow", 0.8, 0, 1);

    public ColorModule() {
        INSTANCE = this;
    }
}
