package lat.saturn.feature.module.client;

import lat.saturn.api.event.world.EventTick;
import lat.saturn.api.manager.module.Category;
import lat.saturn.api.manager.module.Module;
import lat.saturn.api.manager.module.RegisterModule;
import lat.saturn.gui.click.ClickGuiScreen;
import meteordevelopment.orbit.EventHandler;
import org.lwjgl.glfw.GLFW;

@RegisterModule(name="Click GUI", description = "The client's Click GUI", category = Category.CLIENT, toggled = true, bind= GLFW.GLFW_KEY_RIGHT_SHIFT)
public class ClickGUIModule extends Module {

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
}
