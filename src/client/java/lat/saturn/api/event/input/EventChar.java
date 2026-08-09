package lat.saturn.api.event.input;

import lombok.Getter;

public class EventChar {
    private static final EventChar INSTANCE = new EventChar();

    @Getter
    public char chr;
    public int modifiers;
    public boolean cancel = false;

    private EventChar() {}

    public static EventChar get(char chr, int modifiers) {
        INSTANCE.chr = chr;
        INSTANCE.modifiers = modifiers;
        INSTANCE.cancel = false;
        return INSTANCE;
    }

    public boolean shouldCancel() {
        return this.cancel;
    }
}