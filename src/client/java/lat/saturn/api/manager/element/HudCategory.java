package lat.saturn.api.manager.element;

import lombok.Getter;

@Getter
public enum HudCategory {
    HUD("HUD");

    private final String name;

    HudCategory(String name) {
        this.name = name;
    }
}