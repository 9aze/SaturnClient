package lat.saturn.feature.module.misc;

import lat.saturn.api.event.render.EventRender2D;
import lat.saturn.api.manager.module.Category;
import lat.saturn.api.manager.module.Module;
import lat.saturn.api.manager.module.RegisterModule;
import lat.saturn.api.setting.settings.BoolSetting;
import lat.saturn.api.setting.settings.StringSetting;
import meteordevelopment.orbit.EventHandler;

import java.awt.*;

@RegisterModule(name="Test", description = "Yes", category = Category.MISC, toggled = true)
public class TestModule extends Module {

    BoolSetting boolSetting = new BoolSetting("Bool Test", "Bool test setting", false);
    StringSetting stringSetting = new StringSetting("Test", "string test setting", "hello", 10, false);



}
