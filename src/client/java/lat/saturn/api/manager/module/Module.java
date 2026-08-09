package lat.saturn.api.manager.module;

import lat.saturn.SaturnClient;
import lat.saturn.api.setting.Setting;
import lat.saturn.api.util.IMinecraft;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public abstract class Module implements IMinecraft {
    @Getter
    final List<Setting<?, ?>> settings = new ArrayList<>();
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final Category category;
    @Getter
    private final boolean toggleInScreens;
    @Getter
    @Setter
    private BindMode bindMode;
    @Getter
    @Setter
    private int bind;

    private boolean alwaysActive;
    @Getter
    private boolean toggled;

    public Module() {
        RegisterModule annotation = getClass().getAnnotation(RegisterModule.class);

        name = annotation.name();
        description = annotation.description();
        category = annotation.category();
        bind = annotation.bind();
        alwaysActive = annotation.alwaysActive();
        toggled = annotation.toggled();
        toggleInScreens = annotation.toggleInScreens();
        bindMode = annotation.bindMode();


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
        if(alwaysActive) return;

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

    public enum BindMode {
        TOGGLE, HOLD
    }
}
