package lat.saturn.gui.click.components.settings;

import lat.saturn.api.setting.settings.DoubleSetting;
import lat.saturn.api.util.render.RenderUtils;
import lat.saturn.feature.module.client.ColorModule;
import lat.saturn.feature.module.client.OldSettingsModule;
import lat.saturn.gui.click.components.ModuleSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Formatting;

import java.awt.*;

public class DoubleSettingComponent extends ModuleSetting<DoubleSetting> {
    private final DoubleSetting setting;
    private boolean dragging = false;

    public DoubleSettingComponent(DoubleSetting setting, double x, double y, double width) {
        super(setting, x, y, width);
        this.setting = setting;
        this.height = mc.textRenderer.fontHeight + 4;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        double min = setting.getMin();
        double max = setting.getMax();
        double current = setting.getValue();
        double ratio = (current - min) / (max - min);
        ratio = Math.max(0, Math.min(1, ratio));
        int filledWidth = (int)(width * ratio);

        if (!OldSettingsModule.INSTANCE.isToggled()) { // in my defense for this one i copied it from the other one
            RenderUtils.drawRect(context.getMatrices(), ColorModule.INSTANCE.clientColor.getValue(), x, y + height - 1, filledWidth, 1);
        } else {
            RenderUtils.drawRect(context.getMatrices(), ColorModule.INSTANCE.clientColor.getValue().brighter(), x, y, filledWidth, height);
        }
        RenderUtils.drawCustomString(context, Formatting.WHITE + setting.getName() + ": " + Formatting.GRAY + current, Color.WHITE, (int)(x + 4),
                (int)(y + 0.5f + (height / 2) - (RenderUtils.customFontHeight(11) / 2)), 11);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver((int) mouseX, (int) mouseY) && button == 0) {
            dragging = true;
            updateValue((int) mouseX);
        }
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button) {
        if (dragging) {
            updateValue((int) mouseX);
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
    }

    private void updateValue(int mouseX) {
        double ratio = (mouseX - x) / width;
        ratio = Math.max(0, Math.min(1, ratio));
        double newValue = setting.getMin() + ratio * (setting.getMax() - setting.getMin());
        setting.setValue(newValue);
    }

    private boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
