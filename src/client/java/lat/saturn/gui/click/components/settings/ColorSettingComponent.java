package lat.saturn.gui.click.components.settings;

import lat.saturn.api.setting.settings.ColorSetting;
import lat.saturn.api.util.render.RenderUtils;
import lat.saturn.feature.module.client.ColorModule;
import lat.saturn.gui.click.components.ModuleSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ColorSettingComponent extends ModuleSetting<ColorSetting> {

    private static Color clipboard = null;

    private final ColorSetting setting;
    private boolean expanded = false;

    private float hue;
    private float sat;
    private float bri;
    private int alpha;

    private int dragMode = 0; // 0 = none, 1 = sv box, 2 = hue strip, 3 = alpha strip

    private final MiniField hexField = new MiniField(6, true);
    private final MiniField rField = new MiniField(3, false);
    private final MiniField gField = new MiniField(3, false);
    private final MiniField bField = new MiniField(3, false);

    private static final int SV_HEIGHT = 50;
    private static final int STRIP_HEIGHT = 10;
    private static final int GAP = 3;
    private static final float LABEL_SIZE = 11f;
    private static final float BTN_TEXT_SIZE = 9f;
    private static final float FIELD_TEXT_SIZE = 9f;

    private class MiniField {
        String text = "";
        int cursor = 0;
        boolean focused = false;
        boolean selectAll = false;
        long lastBlink = 0;
        boolean blinkOn = true;
        final int maxLen;
        final boolean hexMode;
        double bx, by, bw, bh;

        MiniField(int maxLen, boolean hexMode) {
            this.maxLen = maxLen;
            this.hexMode = hexMode;
        }

        boolean acceptsChar(char c) {
            if (hexMode) return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
            return c >= '0' && c <= '9';
        }

        boolean inBox(double mx, double my) {
            return mx >= bx && mx <= bx + bw && my >= by && my <= by + bh;
        }
    }

    public ColorSettingComponent(ColorSetting setting, double x, double y, double width) {
        super(setting, x, y, width);
        this.setting = setting;
        syncFromSetting();
        syncFieldsFromColor(setting.getValue(), null);
        updateHeight();
    }

    private double rowHeight() {
        return mc.textRenderer.fontHeight + 4;
    }

    private double buttonRowHeight() {
        return mc.textRenderer.fontHeight + 6;
    }

    private double fieldRowHeight() {
        return mc.textRenderer.fontHeight + 6;
    }

    private double compWidth() {
        return width - 1;
    }

    private void syncFromSetting() {
        Color c = setting.getValue();
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        hue = hsb[0];
        sat = hsb[1];
        bri = hsb[2];
        alpha = c.getAlpha();
    }

    private void syncFieldsFromColor(Color c, MiniField skip) {
        if (hexField != skip && !hexField.focused) {
            hexField.text = String.format("%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
            hexField.cursor = hexField.text.length();
        }
        if (rField != skip && !rField.focused) {
            rField.text = String.valueOf(c.getRed());
            rField.cursor = rField.text.length();
        }
        if (gField != skip && !gField.focused) {
            gField.text = String.valueOf(c.getGreen());
            gField.cursor = gField.text.length();
        }
        if (bField != skip && !bField.focused) {
            bField.text = String.valueOf(c.getBlue());
            bField.cursor = bField.text.length();
        }
    }

    private void pushColor() {
        Color base = Color.getHSBColor(hue, sat, bri);
        int a = setting.isAllowAlpha() ? alpha : 255;
        setting.setValue(new Color(base.getRed(), base.getGreen(), base.getBlue(), a));
    }

    private void updateHeight() {
        if (!expanded) {
            height = rowHeight();
            return;
        }

        double h = rowHeight() + GAP + SV_HEIGHT + GAP + STRIP_HEIGHT;
        if (setting.isAllowAlpha()) h += GAP + STRIP_HEIGHT;
        h += GAP + fieldRowHeight();
        h += GAP + buttonRowHeight() + GAP;
        height = h;
    }

    private double svBoxY() {
        return y + rowHeight() + GAP;
    }

    private double hueStripY() {
        return svBoxY() + SV_HEIGHT + GAP;
    }

    private double alphaStripY() {
        return hueStripY() + STRIP_HEIGHT + GAP;
    }

    private double fieldRowY() {
        double base = hueStripY() + STRIP_HEIGHT;
        if (setting.isAllowAlpha()) base = alphaStripY() + STRIP_HEIGHT;
        return base + GAP;
    }

    private double buttonRowY() {
        return fieldRowY() + fieldRowHeight() + GAP;
    }

    private void layoutFields() {
        double rowY = fieldRowY();
        double gap = 2;
        double totalWidth = compWidth();
        double available = totalWidth - gap * 3;
        double hexWidth = available * 0.4;
        double rgbWidth = (available - hexWidth) / 3;

        hexField.bx = x;
        hexField.by = rowY;
        hexField.bw = hexWidth;
        hexField.bh = fieldRowHeight();

        rField.bx = x + hexWidth + gap;
        rField.by = rowY;
        rField.bw = rgbWidth;
        rField.bh = fieldRowHeight();

        gField.bx = rField.bx + rgbWidth + gap;
        gField.by = rowY;
        gField.bw = rgbWidth;
        gField.bh = fieldRowHeight();

        bField.bx = gField.bx + rgbWidth + gap;
        bField.by = rowY;
        bField.bw = rgbWidth;
        bField.bh = fieldRowHeight();
    }

    private List<String> buttonLabels() {
        List<String> labels = new ArrayList<>();
        if (setting.isAllowSync()) labels.add("Sync");
        labels.add("Copy");
        labels.add("Paste");
        return labels;
    }

    private double[] buttonBounds(int index, List<String> labels) {
        double totalWidth = compWidth();
        double gapTotal = GAP * (labels.size() - 1);
        double btnWidth = (totalWidth - gapTotal) / labels.size();
        double btnX = x + index * (btnWidth + GAP);
        return new double[] { btnX, buttonRowY(), btnWidth, buttonRowHeight() };
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        Color current = setting.getValue();

        RenderUtils.drawCustomString(context, setting.getName(), Color.WHITE, (float) (x + 4), (float) (y + 0.5f + (rowHeight() / 2) - (RenderUtils.customFontHeight(LABEL_SIZE) / 2)), LABEL_SIZE);

        int previewWidth = 18;
        int previewHeight = (int) rowHeight() - 4;
        int previewX = (int) (x + compWidth() - previewWidth);
        int previewY = (int) (y + 2);

        RenderUtils.drawRoundedRect(context.getMatrices(), current, previewX, previewY, previewWidth, previewHeight, 2f, 2f, 2f, 2f, 12);
        RenderUtils.drawRoundedOutline(context.getMatrices(), new Color(0, 0, 0, 120), previewX, previewY, previewWidth, previewHeight, 2f, 2f, 2f, 2f, 1f, 12f);

        if (!expanded) return;

        layoutFields();

        renderSvBox(context);
        renderHueStrip(context);
        if (setting.isAllowAlpha()) renderAlphaStrip(context, current);
        renderFields(context);
        renderButtons(context, mouseX, mouseY);
    }

    private void renderSvBox(DrawContext context) {
        double boxY = svBoxY();
        double bw = compWidth() - 2;
        double bx = x + 1;
        double bh = SV_HEIGHT - 2;
        double by = boxY + 1;

        int cols = 20;
        int rows = 10;
        double cellW = bw / cols;
        double cellH = bh / (double) rows;

        for (int cx = 0; cx < cols; cx++) {
            float s = cols == 1 ? 1f : cx / (float) (cols - 1);
            for (int cy = 0; cy < rows; cy++) {
                float b = rows == 1 ? 1f : 1f - (cy / (float) (rows - 1));
                Color cell = Color.getHSBColor(hue, s, b);
                RenderUtils.drawRect(context.getMatrices(), cell, bx + cx * cellW, by + cy * cellH, cellW + 1, cellH + 1);
            }
        }

        RenderUtils.drawRoundedOutline(context.getMatrices(), new Color(0, 0, 0, 150), bx, by, bw, bh, 2f, 2f, 2f, 2f, 1f, 12f);

        double markerX = bx + sat * bw;
        double markerY = by + (1f - bri) * bh;
        drawMarker(context, markerX, markerY);
    }

    private void renderHueStrip(DrawContext context) {
        double stripY = hueStripY();
        double bw = compWidth() - 2;
        double bx = x + 1;
        double bh = STRIP_HEIGHT - 2;
        double by = stripY + 1;

        int segments = 60;
        double segW = bw / segments;

        for (int i = 0; i < segments; i++) {
            float h = i / (float) segments;
            Color seg = Color.getHSBColor(h, 1f, 1f);
            RenderUtils.drawRect(context.getMatrices(), seg, bx + i * segW, by, segW + 1, bh);
        }

        RenderUtils.drawRoundedOutline(context.getMatrices(), new Color(0, 0, 0, 150), bx, by, bw, bh, 2f, 2f, 2f, 2f, 1f, 12f);

        double markerX = bx + hue * bw;
        drawStripPointer(context, markerX, by);
    }

    private void renderAlphaStrip(DrawContext context, Color current) {
        double stripY = alphaStripY();
        double bw = compWidth() - 2;
        double bx = x + 1;
        double bh = STRIP_HEIGHT - 2;
        double by = stripY + 1;

        int checkerSize = 4;
        int cols = (int) (bw / checkerSize) + 1;
        int rows = (int) (bh / checkerSize) + 1;

        for (int cx = 0; cx < cols; cx++) {
            for (int cy = 0; cy < rows; cy++) {
                Color tile = ((cx + cy) % 2 == 0) ? new Color(70, 70, 70) : new Color(100, 100, 100);
                RenderUtils.drawRect(context.getMatrices(), tile, bx + cx * checkerSize, by + cy * checkerSize, checkerSize, checkerSize);
            }
        }

        int segments = 40;
        double segW = bw / segments;
        for (int i = 0; i < segments; i++) {
            int a = (int) (255 * (i / (float) (segments - 1)));
            Color seg = new Color(current.getRed(), current.getGreen(), current.getBlue(), a);
            RenderUtils.drawRect(context.getMatrices(), seg, bx + i * segW, by, segW + 1, bh);
        }

        RenderUtils.drawRoundedOutline(context.getMatrices(), new Color(0, 0, 0, 150), bx, by, bw, bh, 2f, 2f, 2f, 2f, 1f, 12f);

        double markerX = bx + (alpha / 255f) * bw;
        drawStripPointer(context, markerX, by);
    }

    private void drawMarker(DrawContext context, double cx, double cy) {
        int size = 6;
        RenderUtils.drawRoundedOutline(context.getMatrices(), Color.WHITE, cx - size / 2f, cy - size / 2f, size, size, 3f, 3f, 3f, 3f, 1.5f, 10f);
        RenderUtils.drawRoundedOutline(context.getMatrices(), new Color(0, 0, 0, 180), cx - size / 2f - 1, cy - size / 2f - 1, size + 2, size + 2, 3f, 3f, 3f, 3f, 1f, 10f);
    }

    private void drawStripPointer(DrawContext context, double cx, double stripY) {
        RenderUtils.drawRect(context.getMatrices(), Color.WHITE, cx - 1, stripY - 1, 2, STRIP_HEIGHT + 2);
        RenderUtils.drawRect(context.getMatrices(), new Color(0, 0, 0, 180), cx - 2, stripY - 2, 1, STRIP_HEIGHT + 4);
        RenderUtils.drawRect(context.getMatrices(), new Color(0, 0, 0, 180), cx + 1, stripY - 2, 1, STRIP_HEIGHT + 4);
    }

    private void renderFields(DrawContext context) {
        renderField(context, hexField);
        renderField(context, rField);
        renderField(context, gField);
        renderField(context, bField);
    }

    private void renderField(DrawContext context, MiniField field) {
        Color border = field.focused ? ColorModule.INSTANCE.clientColor.getValue() : new Color(60, 60, 60);
        double fx = field.bx + 1;
        double fy = field.by + 1;
        double fw = field.bw - 2;
        double fh = field.bh - 2;

        RenderUtils.drawRoundedRect(context.getMatrices(), new Color(24, 24, 24), fx, fy, fw, fh, 2f, 2f, 2f, 2f, 12);
        RenderUtils.drawRoundedOutline(context.getMatrices(), border, fx, fy, fw, fh, 2f, 2f, 2f, 2f, 1f, 12f);

        float textY = (float) (fy + (fh - RenderUtils.customFontHeight(FIELD_TEXT_SIZE)) / 2);
        RenderUtils.drawCustomString(context, field.text, Color.WHITE, (float) (fx + 4), textY, FIELD_TEXT_SIZE);

        if (field.focused && field.selectAll && !field.text.isEmpty()) {
            float selWidth = RenderUtils.customTextWidth(field.text, FIELD_TEXT_SIZE);
            RenderUtils.drawRect(context.getMatrices(), new Color(80, 130, 200, 110), fx + 4, fy + 2, selWidth, fh - 4);
        }

        if (field.focused && !field.selectAll) {
            long now = System.currentTimeMillis();
            if (now - field.lastBlink > 500) {
                field.blinkOn = !field.blinkOn;
                field.lastBlink = now;
            }
            if (field.blinkOn) {
                float cursorX = (float) (fx + 4 + RenderUtils.customTextWidth(field.text.substring(0, field.cursor), FIELD_TEXT_SIZE));
                RenderUtils.drawRect(context.getMatrices(), Color.WHITE, cursorX, fy + 2, 1, fh - 4);
            }
        }
    }

    private void renderButtons(DrawContext context, int mouseX, int mouseY) {
        List<String> labels = buttonLabels();
        Color accent = ColorModule.INSTANCE.clientColor.getValue();

        for (int i = 0; i < labels.size(); i++) {
            String label = labels.get(i);
            double[] b = buttonBounds(i, labels);
            double bx = b[0] + 1;
            double by = b[1] + 1;
            double bw = b[2] - 2;
            double bh = b[3] - 2;

            boolean active = label.equals("Sync") && setting.isSync();
            boolean hovering = mouseX >= b[0] && mouseX <= b[0] + b[2] && mouseY >= b[1] && mouseY <= b[1] + b[3];

            Color bg = active ? accent : (hovering ? new Color(55, 55, 55) : new Color(35, 35, 35));
            RenderUtils.drawRoundedRect(context.getMatrices(), bg, bx, by, bw, bh, 2f, 2f, 2f, 2f, 12);

            float textWidth = RenderUtils.customTextWidth(label, BTN_TEXT_SIZE);
            float textX = (float) (bx + (bw - textWidth) / 2);
            float textY = (float) (by + (bh - RenderUtils.customFontHeight(BTN_TEXT_SIZE)) / 2);
            RenderUtils.drawCustomString(context, label, Color.WHITE, textX, textY, BTN_TEXT_SIZE);
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 1 && mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + rowHeight()) {
            expanded = !expanded;
            updateHeight();
            return;
        }

        if (!expanded) return;

        layoutFields();

        if (button == 0) {
            boolean hexHit = hexField.inBox(mouseX, mouseY);
            boolean rHit = rField.inBox(mouseX, mouseY);
            boolean gHit = gField.inBox(mouseX, mouseY);
            boolean bHit = bField.inBox(mouseX, mouseY);

            hexField.focused = hexHit;
            rField.focused = rHit;
            gField.focused = gHit;
            bField.focused = bHit;

            hexField.selectAll = false;
            rField.selectAll = false;
            gField.selectAll = false;
            bField.selectAll = false;

            if (hexHit) hexField.cursor = hexField.text.length();
            if (rHit) rField.cursor = rField.text.length();
            if (gHit) gField.cursor = gField.text.length();
            if (bHit) bField.cursor = bField.text.length();

            if (hexHit || rHit || gHit || bHit) return;

            double svY = svBoxY();
            if (mouseY >= svY && mouseY <= svY + SV_HEIGHT && mouseX >= x && mouseX <= x + compWidth()) {
                dragMode = 1;
                updateSv(mouseX);
                return;
            }

            double hueY = hueStripY();
            if (mouseY >= hueY && mouseY <= hueY + STRIP_HEIGHT && mouseX >= x && mouseX <= x + compWidth()) {
                dragMode = 2;
                updateHue(mouseX);
                return;
            }

            if (setting.isAllowAlpha()) {
                double alphaY = alphaStripY();
                if (mouseY >= alphaY && mouseY <= alphaY + STRIP_HEIGHT && mouseX >= x && mouseX <= x + compWidth()) {
                    dragMode = 3;
                    updateAlpha(mouseX);
                    return;
                }
            }

            List<String> labels = buttonLabels();
            for (int i = 0; i < labels.size(); i++) {
                double[] b = buttonBounds(i, labels);
                if (mouseX >= b[0] && mouseX <= b[0] + b[2] && mouseY >= b[1] && mouseY <= b[1] + b[3]) {
                    handleButton(labels.get(i));
                    return;
                }
            }
        }
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button) {
        if (!expanded || button != 0 || dragMode == 0) return;

        switch (dragMode) {
            case 1 -> updateSvDrag(mouseX, mouseY);
            case 2 -> updateHue(mouseX);
            case 3 -> updateAlpha(mouseX);
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        dragMode = 0;
    }

    private void updateSv(double mouseX) {
        updateSvDrag(mouseX, svBoxY());
    }

    private void updateSvDrag(double mouseX, double mouseY) {
        double boxY = svBoxY();
        double clampedX = clamp(mouseX, x, x + compWidth());
        double clampedY = clamp(mouseY, boxY, boxY + SV_HEIGHT);
        sat = (float) ((clampedX - x) / compWidth());
        bri = 1f - (float) ((clampedY - boxY) / SV_HEIGHT);
        pushColor();
        syncFieldsFromColor(setting.getValue(), null);
    }

    private void updateHue(double mouseX) {
        double clampedX = clamp(mouseX, x, x + compWidth());
        hue = (float) ((clampedX - x) / compWidth());
        pushColor();
        syncFieldsFromColor(setting.getValue(), null);
    }

    private void updateAlpha(double mouseX) {
        double clampedX = clamp(mouseX, x, x + compWidth());
        alpha = (int) (255 * ((clampedX - x) / compWidth()));
        pushColor();
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    private void handleButton(String label) {
        switch (label) {
            case "Sync" -> {
                setting.setSync(!setting.isSync());
                if (!setting.isSync()) {
                    syncFromSetting();
                    syncFieldsFromColor(setting.getValue(), null);
                }
            }
            case "Copy" -> clipboard = setting.getValue();
            case "Paste" -> {
                if (clipboard != null) {
                    setting.setValue(clipboard);
                    syncFromSetting();
                    syncFieldsFromColor(setting.getValue(), null);
                }
            }
        }
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        MiniField field = focusedField();
        if (field == null) return;

        boolean ctrl = Screen.hasControlDown();

        switch (keyCode) {
            case 259:
                if (field.selectAll) clearField(field);
                else if (field.cursor > 0) {
                    field.text = field.text.substring(0, field.cursor - 1) + field.text.substring(field.cursor);
                    field.cursor--;
                    applyFieldEdit(field);
                }
                break;

            case 261:
                if (field.selectAll) clearField(field);
                else if (field.cursor < field.text.length()) {
                    field.text = field.text.substring(0, field.cursor) + field.text.substring(field.cursor + 1);
                    applyFieldEdit(field);
                }
                break;

            case 263:
                field.selectAll = false;
                if (field.cursor > 0) field.cursor--;
                break;

            case 262:
                field.selectAll = false;
                if (field.cursor < field.text.length()) field.cursor++;
                break;

            case 268:
                field.selectAll = false;
                field.cursor = 0;
                break;

            case 269:
                field.selectAll = false;
                field.cursor = field.text.length();
                break;

            case 65:
                if (ctrl) field.selectAll = true;
                break;

            case 67:
                if (ctrl) mc.keyboard.setClipboard(field.text);
                break;

            case 86:
                if (ctrl) {
                    String clip = mc.keyboard.getClipboard();
                    if (clip != null && !clip.isEmpty()) {
                        StringBuilder filtered = new StringBuilder();
                        for (char c : clip.toCharArray()) {
                            if (field.acceptsChar(c)) filtered.append(c);
                        }
                        String clean = filtered.toString();
                        if (!clean.isEmpty()) {
                            if (field.selectAll) {
                                field.text = clean.length() > field.maxLen ? clean.substring(0, field.maxLen) : clean;
                                field.cursor = field.text.length();
                                field.selectAll = false;
                            } else {
                                String next = field.text.substring(0, field.cursor) + clean + field.text.substring(field.cursor);
                                if (next.length() > field.maxLen) next = next.substring(0, field.maxLen);
                                field.cursor = Math.min(field.cursor + clean.length(), next.length());
                                field.text = next;
                            }
                            applyFieldEdit(field);
                        }
                    }
                }
                break;

            case 256:
                field.focused = false;
                field.selectAll = false;
                break;
        }
    }

    public void charTyped(char chr, int modifiers) {
        MiniField field = focusedField();
        if (field == null || !field.acceptsChar(chr)) return;

        if (field.selectAll) {
            field.text = String.valueOf(chr);
            field.cursor = 1;
            field.selectAll = false;
            applyFieldEdit(field);
        } else if (field.text.length() < field.maxLen) {
            field.text = field.text.substring(0, field.cursor) + chr + field.text.substring(field.cursor);
            field.cursor++;
            applyFieldEdit(field);
        }
    }

    private MiniField focusedField() {
        if (hexField.focused) return hexField;
        if (rField.focused) return rField;
        if (gField.focused) return gField;
        if (bField.focused) return bField;
        return null;
    }

    private void clearField(MiniField field) {
        field.text = "";
        field.cursor = 0;
        field.selectAll = false;
        applyFieldEdit(field);
    }

    private void applyFieldEdit(MiniField field) {
        if (field == hexField) {
            if (hexField.text.length() == 6) {
                try {
                    Color c = new Color(Integer.parseInt(hexField.text, 16));
                    float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
                    hue = hsb[0];
                    sat = hsb[1];
                    bri = hsb[2];
                    pushColor();
                    syncFieldsFromColor(setting.getValue(), hexField);
                } catch (NumberFormatException ignored) {}
            }
            return;
        }

        Color base = Color.getHSBColor(hue, sat, bri);
        int r = base.getRed();
        int g = base.getGreen();
        int b = base.getBlue();

        int parsed = field.text.isEmpty() ? 0 : Math.min(255, safeParseInt(field.text));

        if (field == rField) r = parsed;
        else if (field == gField) g = parsed;
        else if (field == bField) b = parsed;

        float[] hsb = Color.RGBtoHSB(r, g, b, null);
        hue = hsb[0];
        sat = hsb[1];
        bri = hsb[2];
        pushColor();
        syncFieldsFromColor(setting.getValue(), field);
    }

    private int safeParseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public boolean isHovering(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}