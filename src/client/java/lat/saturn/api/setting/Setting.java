package lat.saturn.api.setting;

import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public abstract class Setting<T, S extends Setting<T, S>> {
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Setter
    @Getter
    protected T value;
    @Getter
    protected final T defaultValue;
    protected SettingGroup group = null;
    protected Supplier<Boolean> visibility = () -> true;

    public Setting(String name, String description, T defaultValue) {
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.name = name;
        this.description = description;
    }

    public S group(SettingGroup group) {
        this.group = group;
        return (S) this;
    }

    public S visible(Supplier<Boolean> supplier) {
        this.visibility = supplier;
        return (S) this;
    }


    public boolean isVisible() {
        return visibility.get();
    }

    //for config shit later on
    @Override
    public String toString() {
        return "Setting{name='" + name + "', description='" + description + "', value='" + value + "', group='" + group + "'}";
    }
}
