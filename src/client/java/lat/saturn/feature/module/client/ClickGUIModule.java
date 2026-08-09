package lat.saturn.feature.module.client;

import lat.saturn.api.event.world.EventTick;
import lat.saturn.api.manager.module.Category;
import lat.saturn.api.manager.module.Module;
import lat.saturn.api.manager.module.RegisterModule;
import lat.saturn.api.setting.settings.BoolSetting;
import lat.saturn.api.setting.settings.ColorSetting;
import lat.saturn.api.setting.settings.DoubleSetting;
import lat.saturn.api.setting.settings.EnumSetting;
import lat.saturn.api.setting.settings.IntSetting;
import lat.saturn.gui.click.ClickGuiScreen;
import lat.saturn.gui.click.ToolTip;
import meteordevelopment.orbit.EventHandler;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

@RegisterModule(name="ClickGUI", description="The client's Click GUI", category=Category.CLIENT, toggled=true, bind=GLFW.GLFW_KEY_RIGHT_SHIFT)
public class ClickGUIModule extends Module {
    public static ClickGUIModule INSTANCE;

    public enum TooltipPosition {
        TopLeft,
        TopRight,
        BottomLeft,
        BottomRight,
        Cursor
    }

    public enum HoverEffect {
        Highlight,
        Up,
        Right,
        None
    }

    public BoolSetting thickSliders = new BoolSetting("Thick Sliders", "Toggles thick sliders", false);
    public BoolSetting moduleBinds = new BoolSetting("Module Binds", "Show binds on the module button", true);
    public IntSetting categoryRadius = new IntSetting("Category Radius", "Radius of category pane", 2, 0, 30);
    public IntSetting moduleRadius = new IntSetting("Module Radius", "Radius of module button", 2, 0, 30);
    public ColorSetting bgColor = new ColorSetting("Bg Color", "Color of the background", new Color(0, 0, 0, 120));
    public EnumSetting<TooltipPosition> tooltipPosition = new EnumSetting<>("Tooltip Position", "Position of tooltips", TooltipPosition.BottomLeft);
    public EnumSetting<HoverEffect> hoverEffect = new EnumSetting<>("Hover Effect", "Hover effect on the module buttons", HoverEffect.Right);


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

    public ClickGUIModule() {
        INSTANCE = this;
    }
}