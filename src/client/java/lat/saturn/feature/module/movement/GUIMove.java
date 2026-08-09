package lat.saturn.feature.module.movement;

import lat.saturn.api.event.input.EventKey;
import lat.saturn.api.manager.module.Category;
import lat.saturn.api.manager.module.Module;
import lat.saturn.api.manager.module.RegisterModule;
import lat.saturn.api.setting.settings.BoolSetting;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.text.Text;

@RegisterModule(name="GuiMove", description="Allows you to move without closing GUI(s)", category= Category.MOVEMENT, toggled = false)
public class GUIMove extends Module {

    BoolSetting boolPrintKey = new BoolSetting("Print key", "Shows the pressed key", false);

    @EventHandler
    public void onInput(EventKey event)
    {
        if (!this.isToggled()) return;
        if (this.boolPrintKey.getValue()) {
            mc.player.sendMessage(Text.literal(String.valueOf(event.key)), true);
        }
        switch (event.key) {
            case 87:
                mc.options.forwardKey.setPressed(true);
                break;
            case 65:
                mc.options.leftKey.setPressed(true);
                break;
            case 83:
                mc.options.backKey.setPressed(true);
                break;
            case 68:
                mc.options.rightKey.setPressed(true);
                break;
            case 32:
                mc.options.jumpKey.setPressed(true);
                break;
            case 340:
                mc.options.sneakKey.setPressed(true);
                break;
        }
    }

    @Override
    public void onDisable() {
        mc.options.forwardKey.setPressed(false);
        mc.options.leftKey.setPressed(false);
        mc.options.backKey.setPressed(false);
        mc.options.rightKey.setPressed(false);
        mc.options.jumpKey.setPressed(false);
        mc.options.sneakKey.setPressed(false);
    }
}
