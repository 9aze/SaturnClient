package lat.saturn.api.setting.settings;

import lat.saturn.api.setting.Setting;

public class StringSetting extends Setting<String, StringSetting> {
    private final int maxLen;
    private final boolean numbersOnly;

    public StringSetting(String name, String description, String defaultValue, int maxLength, boolean numbersOnly) {
        super(name, description, defaultValue);
        this.maxLen = maxLength;
        this.numbersOnly = numbersOnly;
    }

    @Override
    public void setValue(String value) {
        if (value.length() <= maxLen) {
            if (numbersOnly) {
                if (isNumeric(value)) {
                    super.setValue(value);
                }
            } else {
                super.setValue(value);
            }
        }
    }

    private boolean isNumeric(String str) {
        if (str.isEmpty()) return true;
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}