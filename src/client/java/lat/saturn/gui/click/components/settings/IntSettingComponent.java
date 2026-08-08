package lat.saturn.gui.click.components.settings;

import lat.saturn.api.setting.settings.IntSetting;
import lat.saturn.api.util.render.RenderUtils;
import lat.saturn.feature.module.client.ColorModule;
import lat.saturn.gui.click.components.ModuleSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Formatting;

import java.awt.*;

public class IntSettingComponent extends ModuleSetting<IntSetting> {
    private final IntSetting setting;
    private boolean dragging = false;

    public IntSettingComponent(IntSetting setting, double x, double y, double width) {
        super(setting, x, y, width);
        this.setting = setting;
        this.height = mc.textRenderer.fontHeight + 2;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        int min = setting.getMin();
        int max = setting.getMax();
        int current = setting.getValue();
        float ratio = (current - min) / (float)(max - min);
        ratio = Math.max(0, Math.min(1, ratio));
        int filledWidth = (int)(width * ratio);

        RenderUtils.drawRect(context.getMatrices(), ColorModule.INSTANCE.clientColor.getValue(), x, y, filledWidth, height);
        RenderUtils.drawRect(context.getMatrices(), new Color(0, 0, 0, 0), x, y, width, height);

        RenderUtils.drawString(context, Formatting.WHITE + setting.getName() + ": " + Formatting.GRAY + current, Color.WHITE, (int)(x + 4),
                (int)(y + 0.5f + (height / 2) - (mc.textRenderer.fontHeight / 2)), true);
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
        float ratio = (float) ((mouseX - x) / width);
        ratio = Math.max(0, Math.min(1, ratio));
        int newValue = setting.getMin() + Math.round(ratio * (setting.getMax() - setting.getMin()));
        setting.setValue(newValue);
    }

    private boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
