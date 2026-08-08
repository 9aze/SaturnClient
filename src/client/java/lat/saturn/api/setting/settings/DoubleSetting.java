package lat.saturn.api.setting.settings;


import lat.saturn.api.setting.Setting;

public class DoubleSetting extends Setting<Double, DoubleSetting> {
    private final double min;
    private final double max;

    public DoubleSetting(String name, String description, double defaultValue, double min, double max) {
        super(name, description, defaultValue);
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
}