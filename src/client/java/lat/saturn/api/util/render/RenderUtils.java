package lat.saturn.api.util.render;

import lat.saturn.api.util.IMinecraft;
import me.x150.renderer.render.Renderer2d;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class RenderUtils implements IMinecraft {
    public static void drawRect(MatrixStack matrices, Color color, double x, double y, double width, double height) {
        Renderer2d.renderQuad(matrices, color, x, y, x+width, y+height);
    }

    public static void drawString(DrawContext context, String text, Color color, int x, int y, boolean shadow) {
        context.drawText(mc.textRenderer, text, x, y, color.getRGB(), shadow);
    }

    public static Color setAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}
