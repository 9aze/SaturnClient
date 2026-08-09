package lat.saturn.api.util;

import net.minecraft.client.util.InputUtil;

public final class KeyUtils {

    private KeyUtils() {}

    public static String getKeyName(int key) {
        if (key <= 0) return "None";

        try {
            String name = InputUtil.fromKeyCode(key, 0).getLocalizedText().getString();
            if (name == null || name.isBlank() || name.contains(".key.") || name.contains(".action."))
                return "None";

            return switch (name.toLowerCase()) {
                case "right shift" -> "RShift";
                case "left shift" -> "LShift";
                case "right control" -> "RCtrl";
                case "left control" -> "LCtrl";
                case "right alt" -> "RAlt";
                case "left alt" -> "LAlt";
                case "caps lock" -> "Caps";
                case "backspace" -> "Back";
                case "escape" -> "Esc";
                case "enter" -> "Enter";
                case "space" -> "Space";
                case "page up" -> "PgUp";
                case "page down" -> "PgDn";
                case "insert" -> "Ins";
                case "delete" -> "Del";
                case "home" -> "Home";
                case "end" -> "End";
                case "up" -> "Up";
                case "down" -> "Down";
                case "left" -> "Left";
                case "right" -> "Right";
                default -> name;
            };
        } catch (Exception e) {
            return "None";
        }
    }
}