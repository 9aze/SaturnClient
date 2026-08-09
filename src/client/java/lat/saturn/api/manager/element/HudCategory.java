package lat.saturn.api.manager.element;

import lombok.Getter;

@Getter
public enum HudCategory {
    HUD("HUD", "hud.png");

    private final String name;
    private final String icon;

    HudCategory(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }
}