package lat.saturn.api.manager.module;

import lombok.Getter;

@Getter
public enum Category {
    COMBAT("Combat", "combat.png"),
    MISC("Misc", "misc.png"),
    MOVEMENT("Movement", "movement.png"),
    WORLD("World", "world.png"),
    RENDER("Render", "render.png"),
    CLIENT("Client", "config.png");

    private final String name;
    private final String icon;

    Category(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }
}