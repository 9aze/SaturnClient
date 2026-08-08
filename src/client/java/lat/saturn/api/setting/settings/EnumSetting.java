package lat.saturn.api.setting.settings;

import lat.saturn.api.setting.Setting;

public class EnumSetting<T extends Enum<T>> extends Setting<Enum<T>, EnumSetting<T>> {

    final T[] values;
    int index = 0;

    public EnumSetting(String name, String description, Enum<T> defaultValue) {
        super(name, description, defaultValue);
        values = defaultValue.getDeclaringClass().getEnumConstants();
    }

    @Override
    public void setValue(Enum<T> value) {
        for (int i = 0; i < values.length; i++) {
            if(values[i] == value) {
                index = i;
                this.value = value;
                return;
            }
        }
        throw new IllegalArgumentException("enum not in list!");
    }

    public boolean setValue(String value) {
        for (int i = 0; i < values.length; i++) {
            if(values[i].name().equals(value)) {
                index = i;
                this.value = values[i];
                return true;
            }
        }
        return false;
    }
}
