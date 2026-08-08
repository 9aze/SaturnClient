package lat.saturn.api.manager.element;

import lat.saturn.SaturnClient;
import lat.saturn.api.manager.module.Category;
import lat.saturn.api.setting.Setting;
import lat.saturn.api.util.IMinecraft;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public abstract class Element implements IMinecraft {
    @Getter
    final List<Setting<?, ?>> settings = new ArrayList<>();
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final HudCategory category;

    @Getter
    private boolean toggled;

    public Element() {
        RegisterElement annotation = getClass().getAnnotation(RegisterElement.class);

        name = annotation.name();
        description = annotation.description();
        category = HudCategory.HUD;
        toggled = annotation.toggled();

        if(toggled) {
            SaturnClient.EVENT_BUS.subscribe(this);
        } else {
            SaturnClient.EVENT_BUS.unsubscribe(this);
        }
    }

    public void toggle() {
        setToggled(!this.toggled);
    }

    public void setToggled(boolean toggled) {
       // if(alwaysActive) return;

        this.toggled = toggled;

        if (this.toggled) {
            SaturnClient.EVENT_BUS.subscribe(this);
            onEnable();
        } else {
            SaturnClient.EVENT_BUS.unsubscribe(this);
            onDisable();
        }
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onSettingChange(Setting<?, ?> setting) {}

 }