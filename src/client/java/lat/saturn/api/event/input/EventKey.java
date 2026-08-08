package lat.saturn.api.event.input;

import lombok.Getter;

public class EventKey {
    private static final EventKey INSTANCE = new EventKey();

    @Getter
    public int key;
    public int scancode;
    /**
     * 0 - release
     * 1 - press
     * 2 - repeat
     */
    public int action;
    public int modifiers;
    public boolean cancel = false;

    private EventKey() {}
    public static EventKey get(int key, int scancode, int action, int modifiers) {
        INSTANCE.key = key;
        INSTANCE.scancode = scancode;
        INSTANCE.action = action;
        INSTANCE.modifiers = modifiers;
        INSTANCE.cancel = false;

        return INSTANCE;
    }

    public boolean shouldCancel() { return this.cancel; }
}