package lat.saturn.feature.module.misc;

import lat.saturn.api.event.render.EventRender2D;
import lat.saturn.api.manager.module.Category;
import lat.saturn.api.manager.module.Module;
import lat.saturn.api.manager.module.RegisterModule;
import lat.saturn.api.setting.settings.BoolSetting;
import lat.saturn.api.setting.settings.StringSetting;
import meteordevelopment.orbit.EventHandler;

import java.awt.*;

@RegisterModule(name="AnarchyMod", description = "Enables features available in 6b6t's AnarchyMod", category = Category.MISC, toggled = true)
public class AnarchyMod extends Module {
    public static AnarchyMod INSTANCE;
    public BoolSetting unblockServers = new BoolSetting("Unblock Servers", "Unblocks all servers blacklisted by Mojang", true);
    public BoolSetting extraHome = new BoolSetting("Extra Home", "Gives you an extra home slot on 6b6t", true);

    public AnarchyMod() { INSTANCE = this; }
}
