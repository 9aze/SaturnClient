package lat.saturn.gui.click.components.settings;

import lat.saturn.api.setting.settings.ColorSetting;
import lat.saturn.api.setting.settings.IntSetting;
import lat.saturn.api.util.render.RenderUtils;
import lat.saturn.feature.module.client.ColorModule;
import lat.saturn.gui.click.components.ModuleSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Formatting;

import java.awt.*;

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
    public ColorSettingComponent(ColorSetting setting, double x, double y, double width) {
        super(setting, x, y, width);
        this.setting = setting;
        this.height = mc.textRenderer.fontHeight + 2;
        this.red = new IntSetting("R", "R", 0, 0, 255);
        this.green = new IntSetting("G", "G", 0, 0, 255);
        this.blue = new IntSetting("B", "B", 0, 0, 255);
        this.alpha = new IntSetting("A", "A", 0, 0, 255);
        double rowHeight = mc.textRenderer.fontHeight + 2;
        this.height = rowHeight * 4;
        this.redComponent = new IntSettingComponent(red, x, y + rowHeight, width);
        this.greenComponent = new IntSettingComponent(green, x, y + rowHeight *2, width);
        this.blueComponent = new IntSettingComponent(blue, x, y + rowHeight *3, width);
        this.alphaComponent = new IntSettingComponent(alpha, x, y + rowHeight *4, width);
        // actually set the IntSettings to the values in ColorSetting
        this.red.setValue(setting.getValue().getRed());
        this.green.setValue(setting.getValue().getGreen());
        this.blue.setValue(setting.getValue().getBlue());
        this.alpha.setValue(setting.getValue().getAlpha());
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        redComponent.render(context, mouseX, mouseY, partialTicks);
        greenComponent.render(context, mouseX, mouseY, partialTicks);
        blueComponent.render(context, mouseX, mouseY, partialTicks);
        alphaComponent.render(context, mouseX, mouseY, partialTicks);
    }
    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        redComponent.mouseClicked(mouseX, mouseY, button);
        greenComponent.mouseClicked(mouseX, mouseY, button);
        blueComponent.mouseClicked(mouseX, mouseY, button);
        alphaComponent.mouseClicked(mouseX, mouseY, button);
        updateColor();
    }
    @Override
    public void mouseDragged(double mouseX, double mouseY, int button) {
        redComponent.mouseDragged(mouseX, mouseY, button);
        greenComponent.mouseDragged(mouseX, mouseY, button);
        blueComponent.mouseDragged(mouseX, mouseY, button);
        alphaComponent.mouseDragged(mouseX, mouseY, button);
        updateColor();
    }
    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        redComponent.mouseReleased(mouseX, mouseY, button);
        greenComponent.mouseReleased(mouseX, mouseY, button);
        blueComponent.mouseReleased(mouseX, mouseY, button);
        alphaComponent.mouseReleased(mouseX, mouseY, button);
        updateColor();
    }
    private void updateValue(int mouseX) {
        // no need
    }
    private void updateColor() {
        setting.setValue(new Color(
                red.getValue(),
                green.getValue(),
                blue.getValue(),
                alpha.getValue()
        ));
    }
    private boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + (height*3);
    }
}