package lat.saturn.api.manager.module;

import lombok.Getter;

@Getter
public enum Category {
    COMBAT("Combat"),
    MISC("Misc"),
    PLAYER("Player"),
    MOVEMENT("Movement"),
    WORLD("World"),
    RENDER("Render"),
    CLIENT("Client");

    private final String name;

    Category(String name) {
        this.name = name;
    }
}
