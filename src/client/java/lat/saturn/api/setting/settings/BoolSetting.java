package lat.saturn.api.setting.settings;

import lat.saturn.api.setting.Setting;

public class BoolSetting extends Setting<Boolean, BoolSetting> {
    public BoolSetting(String name, String description, Boolean defaultValue) {
        super(name, description, defaultValue);
    }
}