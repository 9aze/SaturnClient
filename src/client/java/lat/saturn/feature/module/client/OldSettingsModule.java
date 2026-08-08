package lat.saturn.feature.module.client;

import lat.saturn.api.manager.module.Category;
import lat.saturn.api.manager.module.Module;
import lat.saturn.api.manager.module.RegisterModule;
import lat.saturn.api.setting.settings.BoolSetting;
import lat.saturn.api.setting.settings.ColorSetting;
import lat.saturn.api.setting.settings.DoubleSetting;

import java.awt.*;

@RegisterModule(name="OldSettings", description = "Reverts the Saturn Client module configuration settings to a very early point in development.", category = Category.CLIENT, toggled = true)
public class OldSettingsModule extends Module {
    public static OldSettingsModule INSTANCE = null;
    public OldSettingsModule() {
        INSTANCE = this;
    }
}
