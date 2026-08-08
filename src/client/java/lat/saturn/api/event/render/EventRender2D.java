package lat.saturn.api.event.render;

import lat.saturn.api.util.render.FrameUtils;
import net.minecraft.client.gui.DrawContext;

public class EventRender2D {
    private static final EventRender2D INSTANCE = new EventRender2D();

    public DrawContext context;
    public int screenWidth, screenHeight;
    public double frameTime;
    public float tickDelta;

    public static EventRender2D get(DrawContext context, int screenWidth, int screenHeight, float tickDelta) {
        INSTANCE.context = context;
        INSTANCE.screenWidth = screenWidth;
        INSTANCE.screenHeight = screenHeight;
        INSTANCE.frameTime = FrameUtils.frameTime;
        INSTANCE.tickDelta = tickDelta;
        return INSTANCE;
    }
}