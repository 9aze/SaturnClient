package lat.saturn.gui.click;

import lat.saturn.api.util.IMinecraft;
import lat.saturn.api.util.render.RenderUtils;
import lat.saturn.feature.module.client.ClickGUIModule;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

import static lat.saturn.feature.module.client.ClickGUIModule.TooltipPosition.*;

public class ToolTip implements IMinecraft {
    public static void render(DrawContext context, String description, int mouseX, int mouseY) {
        if (description == null || description.isEmpty()) return;

        int padding = 6;
        int textWidth = mc.textRenderer.getWidth(description);
        int textHeight = mc.textRenderer.fontHeight;
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();

        int x;
        int y;

        switch (ClickGUIModule.INSTANCE.tooltipPosition.getValue()) {
            case TopLeft -> {
                x = padding;
                y = padding;
            }
            case TopRight -> {
                x = screenWidth - textWidth - padding;
                y = padding;
            }
            case BottomLeft -> {
                x = padding;
                y = screenHeight - textHeight - padding;
            }
            case BottomRight -> {
                x = screenWidth - textWidth - padding;
                y = screenHeight - textHeight - padding;
            }
            case Cursor -> {
                x = mouseX + padding;
                y = mouseY + padding;
            }
            default -> {
                x = mouseX + padding;
                y = mouseY + padding;
            }
        }

        RenderUtils.drawCustomString(
                context,
                description,
                new Color(255, 255, 255, 255),
                x,
                y,
                11
        );
    }
}