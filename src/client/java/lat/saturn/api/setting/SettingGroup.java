package lat.saturn.api.setting;

public record SettingGroup(String name, String description) {
    public SettingGroup(String name) {
        this(name, name + " settings");
    }
}