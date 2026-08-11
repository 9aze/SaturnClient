package lat.saturn.api.util.render;

import lat.saturn.api.util.IMinecraft;
import lat.saturn.feature.module.client.HudEditor;
import me.x150.renderer.font.FontRenderer;
import me.x150.renderer.render.Renderer2d;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class RenderUtils implements IMinecraft {

    private static final Font FLUX_MEDIUM_BASE = loadAwtFont("assets/saturnclient/fonts/flux-medium.ttf");
    private static final Map<Integer, FontRenderer> FLUX_MEDIUM_CACHE = new HashMap<>();

    private static Font loadAwtFont(String resourcePath) {
        try (InputStream stream = RenderUtils.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (stream == null) throw new IOException("Font resource not found: " + resourcePath);
            return Font.createFont(Font.TRUETYPE_FONT, stream);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException("Failed to load font: " + resourcePath, e);
        }
    }

    private static FontRenderer fluxMedium(float sizePx) {
        int key = Math.round(sizePx);
        return FLUX_MEDIUM_CACHE.computeIfAbsent(key, k -> {
            FontRenderer fr = new FontRenderer(FLUX_MEDIUM_BASE, sizePx);
            fr.roundCoordinates(true);
            return fr;
        });
    }

    public static void drawRect(MatrixStack matrices, Color color, double x, double y, double width, double height) {
        Renderer2d.renderQuad(matrices, color, x, y, x + width, y + height);
    }

    public static void drawRoundedRect(MatrixStack matrices, Color color, double x, double y, double width, double height, float tl, float tr, float bl, float br, float samples) {
        Renderer2d.renderRoundedQuad(matrices, color, x, y, x + width, y + height, tl, tr, bl, br, samples);
    }

    public static void drawRoundedOutline(MatrixStack matrices, Color color, double x, double y, double width, double height, float tl, float tr, float bl, float br, float thickness, float samples) {
        Renderer2d.renderRoundedOutline(matrices, color, x, y, x + width, y + height, tl, tr, bl, br, thickness, samples);
    }

    public static void drawString(DrawContext context, String text, Color color, int x, int y, boolean shadow) {
        context.drawText(mc.textRenderer, text, x, y, color.getRGB(), shadow);
    }

    public static void drawHUDString(DrawContext context, Text text, float x, float y, float size) {
        drawHUDString(context, text, x, y, size, 1f);
    }

    public static void drawHUDString(DrawContext context, Text text, float x, float y, float size, float alpha) {
        if (HudEditor.INSTANCE.customFont.getValue()) {
            fluxMedium(size).drawText(context.getMatrices(), text, x, y - 2, alpha);
        } else {
            context.drawText(mc.textRenderer, text, (int) x, (int) y, 0xFFFFFF, true);
        }
    }

    public static void drawCustomString(DrawContext context, String text, Color color, float x, float y, float size) {
        Style style = Style.EMPTY.withColor(TextColor.fromRgb(color.getRGB() & 0xFFFFFF));
        fluxMedium(size).drawText(context.getMatrices(), Text.literal(text).setStyle(style), x, y, color.getAlpha() / 255f);
    }

    public static float customTextWidth(Text text, float size) {
        return fluxMedium(size).getTextWidth(text);
    }

    public static float customTextWidth(String text, float size) {
        return fluxMedium(size).getTextWidth(Text.literal(text));
    }

    public static float customFontHeight(float size) {
        return fluxMedium(size).getFontHeight();
    }

    public static Color setAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}