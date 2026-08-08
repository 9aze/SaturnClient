package lat.saturn.gui.click.components.settings;

import lat.saturn.api.setting.settings.ColorSetting;
import lat.saturn.api.setting.settings.IntSetting;
import lat.saturn.api.util.render.RenderUtils;
import lat.saturn.gui.click.components.ModuleSetting;
import net.minecraft.client.gui.DrawContext;

import java.awt.Color;

public class ColorSettingComponent extends ModuleSetting<ColorSetting> {
    private final ColorSetting setting;
    private final IntSetting red;
    private final IntSetting green;
    private final IntSetting blue;
    private final IntSetting alpha;
    private final IntSettingComponent redComponent;
    private final IntSettingComponent greenComponent;
    private final IntSettingComponent blueComponent;
    private final IntSettingComponent alphaComponent;
    private boolean expanded = false;

    public ColorSettingComponent(ColorSetting setting, double x, double y, double width) {
        super(setting, x, y, width);
        this.setting = setting;
        this.expanded = false;
        this.red = new IntSetting("R", "R", 0, 0, 255);
        this.green = new IntSetting("G", "G", 0, 0, 255);
        this.blue = new IntSetting("B", "B", 0, 0, 255);
        this.alpha = new IntSetting("A", "A", 0, 0, 255);

        this.red.setValue(setting.getValue().getRed());
        this.green.setValue(setting.getValue().getGreen());
        this.blue.setValue(setting.getValue().getBlue());
        this.alpha.setValue(setting.getValue().getAlpha());

        this.redComponent = new IntSettingComponent(red, x, y, width);
        this.greenComponent = new IntSettingComponent(green, x, y, width);
        this.blueComponent = new IntSettingComponent(blue, x, y, width);
        this.alphaComponent = new IntSettingComponent(alpha, x, y, width);

        updateHeight();
        updateComponentPositions();
    }

    private double getRowHeight() {
        return mc.textRenderer.fontHeight + 4;
    }

    private void updateHeight() {
        double rowHeight = getRowHeight();
        height = expanded ? rowHeight * 5 : rowHeight;
    }

    private void updateComponentPositions() {
        double rowHeight = getRowHeight();

        redComponent.x = x;
        redComponent.y = y + rowHeight;
        redComponent.width = width;

        greenComponent.x = x;
        greenComponent.y = y + rowHeight * 2;
        greenComponent.width = width;

        blueComponent.x = x;
        blueComponent.y = y + rowHeight * 3;
        blueComponent.width = width;

        alphaComponent.x = x;
        alphaComponent.y = y + rowHeight * 4;
        alphaComponent.width = width;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        updateComponentPositions();

        double rowHeight = getRowHeight();
        Color color = setting.getValue();

        RenderUtils.drawCustomString(context, setting.getName(), Color.WHITE, (int)(x + 4),
                (int)(y + 0.5f + (rowHeight / 2) - (RenderUtils.customFontHeight(11) / 2)), 11);

        int previewWidth = 18;
        int previewHeight = (int) rowHeight - 2;
        int previewX = (int) (x + width - previewWidth);
        int previewY = (int) y + 1;

        RenderUtils.drawRoundedRect(context.getMatrices(), color, previewX, previewY, previewWidth, previewHeight, 2f, 2f, 2f, 2f, 12);

        if (!expanded)
            return;

        redComponent.render(context, mouseX, mouseY, partialTicks);
        greenComponent.render(context, mouseX, mouseY, partialTicks);
        blueComponent.render(context, mouseX, mouseY, partialTicks);
        alphaComponent.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        updateComponentPositions();

        if (button == 1 && mouseX >= x && mouseX <= x + width
                && mouseY >= y && mouseY <= y + getRowHeight()) {
            expanded = !expanded;
            updateHeight();
            return;
        }

        if (!expanded)
            return;

        redComponent.mouseClicked(mouseX, mouseY, button);
        greenComponent.mouseClicked(mouseX, mouseY, button);
        blueComponent.mouseClicked(mouseX, mouseY, button);
        alphaComponent.mouseClicked(mouseX, mouseY, button);
        updateColor();
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button) {
        if (!expanded)
            return;

        updateComponentPositions();

        redComponent.mouseDragged(mouseX, mouseY, button);
        greenComponent.mouseDragged(mouseX, mouseY, button);
        blueComponent.mouseDragged(mouseX, mouseY, button);
        alphaComponent.mouseDragged(mouseX, mouseY, button);
        updateColor();
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (!expanded)
            return;

        updateComponentPositions();

        redComponent.mouseReleased(mouseX, mouseY, button);
        greenComponent.mouseReleased(mouseX, mouseY, button);
        blueComponent.mouseReleased(mouseX, mouseY, button);
        alphaComponent.mouseReleased(mouseX, mouseY, button);
        updateColor();
    }

    private void updateColor() {
        setting.setValue(new Color(
                red.getValue(),
                green.getValue(),
                blue.getValue(),
                alpha.getValue()
        ));
    }

    @Override
    public boolean isHovering(double mouseX, double mouseY) {
        double totalHeight = expanded ? getRowHeight() * 5 : getRowHeight();

        return mouseX >= x && mouseX <= x + width
                && mouseY >= y && mouseY <= y + totalHeight;
    }
}
