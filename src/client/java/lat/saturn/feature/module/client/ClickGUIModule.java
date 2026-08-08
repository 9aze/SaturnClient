package lat.saturn.feature.module.client;

import lat.saturn.api.event.world.EventTick;
import lat.saturn.api.manager.module.Category;
import lat.saturn.api.manager.module.Module;
import lat.saturn.api.manager.module.RegisterModule;
import lat.saturn.api.setting.settings.BoolSetting;
import lat.saturn.api.setting.settings.DoubleSetting;
import lat.saturn.api.setting.settings.IntSetting;
import lat.saturn.gui.click.ClickGuiScreen;
import meteordevelopment.orbit.EventHandler;
import org.lwjgl.glfw.GLFW;

@RegisterModule(name="Click GUI", description = "The client's Click GUI", category = Category.CLIENT, toggled = true, bind= GLFW.GLFW_KEY_RIGHT_SHIFT)
public class ClickGUIModule extends Module {
    public static ClickGUIModule INSTANCE;
    public BoolSetting thickSliders = new BoolSetting("Thick Sliders", "Toggles thick sliders", false);
    public IntSetting categoryRadius = new IntSetting("Category Radius", "Radius of category pane", 2, 0, 30);
    public IntSetting moduleRadius = new IntSetting("Module Radius", "Radius of module button", 2, 0, 30);

    @Override
    public void onEnable() {
        if (!(mc.currentScreen instanceof ClickGuiScreen)) {
            mc.setScreen(new ClickGuiScreen());
        }
    }

    @EventHandler
    public void onTick(EventTick event) {
        if (mc.currentScreen instanceof ClickGuiScreen && !this.isToggled()) {
            this.setToggled(true);
        } else if (!(mc.currentScreen instanceof ClickGuiScreen) && this.isToggled()) {
            this.setToggled(false);
        }
    }

    @Override
    public void onDisable() {
        if (mc.currentScreen instanceof ClickGuiScreen) {
            mc.setScreen(null);
        }
    }

    public ClickGUIModule() { INSTANCE = this; }
}
