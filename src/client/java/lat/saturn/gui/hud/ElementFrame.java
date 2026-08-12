package lat.saturn.gui.hud;

import lat.saturn.api.manager.element.Element;
import lat.saturn.api.util.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class ElementFrame {
    public final Element element;
    private boolean dragging;
    private double offsetX, offsetY;

    public ElementFrame(Element element) {
        this.element = element;
    }

    public void render(DrawContext context, int mouseX, int mouseY) {
        if(!element.isToggled()) return;
        RenderUtils.drawRoundedOutline(
                context.getMatrices(),
                Color.WHITE,
                element.getScreenX(),
                element.getScreenY(),
                element.getWidth(),
                element.getHeight(),
                0, 0, 0, 0, 1, 12
        );
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(!element.isToggled()) return false;

        if(button != 0) return false;
        double x = element.getScreenX();
        double y = element.getScreenY();
        if(mouseX < x || mouseX > x + element.getWidth() ||
                mouseY < y || mouseY > y + element.getHeight())
            return false;
        dragging = true;
        offsetX = mouseX - x;
        offsetY = mouseY - y;
        return true;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button) {
        if(!element.isToggled()) return false;

        if(!dragging || button != 0) return false;
        int screenWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
        int screenHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
        double maxX = screenWidth - element.getWidth();
        double maxY = screenHeight - element.getHeight();
        if(maxX > 0)
            element.setX(Math.max(0, Math.min(1, (mouseX - offsetX) / maxX)));
        if(maxY > 0)
            element.setY(Math.max(0, Math.min(1, (mouseY - offsetY) / maxY)));
        return true;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if(!element.isToggled()) return false;

        if(button != 0) return false;
        boolean wasDragging = dragging;
        dragging = false;
        return wasDragging;
    }
}