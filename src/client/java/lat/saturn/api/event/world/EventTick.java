package lat.saturn.api.event.world;

import lombok.Getter;

@Getter
public class EventTick {
    private final Phase phase;

    public EventTick(Phase phase) {
        this.phase = phase;
    }

    public enum Phase {
        START,
        END
    }
}
