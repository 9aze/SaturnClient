package lat.saturn.feature.module.client;

import lat.saturn.api.event.world.EventTick;
import lat.saturn.api.manager.element.HudCategory;
import lat.saturn.api.manager.module.Category;
import lat.saturn.api.manager.module.Module;
import lat.saturn.api.manager.module.RegisterModule;
import lat.saturn.api.setting.settings.BoolSetting;
import lat.saturn.gui.click.ClickGuiScreen;
import lat.saturn.gui.hud.HudEditorScreen;
import meteordevelopment.orbit.EventHandler;
import org.lwjgl.glfw.GLFW;

@RegisterModule(name="HudEditor", description = "The client's hud editor", category = Category.CLIENT, toggled = false, alwaysActive = false, bind= GLFW.GLFW_KEY_COMMA)
public class HudEditor extends Module {
    public static HudEditor INSTANCE = null;

    public BoolSetting customFont = new BoolSetting("Custom Font", "Use custom font in the HUD", false);

    @Override
    public void onEnable() {
        if (!(mc.currentScreen instanceof HudEditorScreen)) {
            mc.setScreen(new HudEditorScreen());
        }
    }

    @EventHandler
    public void onTick(EventTick event) {
        if (mc.currentScreen instanceof HudEditorScreen && !this.isToggled()) {
            this.setToggled(true);
        } else if (!(mc.currentScreen instanceof HudEditorScreen) && this.isToggled()) {
            this.setToggled(false);
        }
    }

    @Override
    public void onDisable() {
        if (mc.currentScreen instanceof HudEditorScreen) {
            mc.setScreen(null);
        }
    }

    public HudEditor() {
        INSTANCE = this;
    }
}
