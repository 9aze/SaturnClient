package lat.saturn.feature.module.misc;

import lat.saturn.api.event.render.EventRender2D;
import lat.saturn.api.manager.module.Category;
import lat.saturn.api.manager.module.Module;
import lat.saturn.api.manager.module.RegisterModule;
import lat.saturn.api.setting.settings.BoolSetting;
import meteordevelopment.orbit.EventHandler;

import java.awt.*;

@RegisterModule(name="Test", description = "Yes", category = Category.MISC, toggled = true)
public class TestModule extends Module {

    BoolSetting boolSetting = new BoolSetting("Bool Test", "Bool test setting", false);

    @EventHandler
    public void onRender2D(EventRender2D event) {
        event.context.drawText(mc.textRenderer, "SaturnClient", 0, 0, new Color(0,0,0,255).getRGB(), true);
    }

}
