package lat.saturn.gui.click.components.settings;

import lat.saturn.api.setting.settings.IntSetting;
import lat.saturn.api.util.render.RenderUtils;
import lat.saturn.feature.module.client.ClickGUIModule;
import lat.saturn.feature.module.client.ColorModule;
import lat.saturn.gui.click.components.ModuleSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Formatting;

import java.awt.*;

public class IntSettingComponent extends ModuleSetting {
    private final IntSetting setting;
    private boolean dragging = false;

    public IntSettingComponent(IntSetting setting, double x, double y, double width) {
        super(setting, x, y, width);
        this.setting = setting;
        this.height = mc.textRenderer.fontHeight + 4;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        int min = setting.getMin(), max = setting.getMax(), current = setting.getValue();
        float ratio = Math.max(0, Math.min(1, (current - min) / (float) (max - min)));
        int sliderWidth = (int) width - 4, filledWidth = (int) (sliderWidth * ratio);

        if (!ClickGUIModule.INSTANCE.thickSliders.getValue())
            RenderUtils.drawRect(context.getMatrices(), ColorModule.INSTANCE.clientColor.getValue(), x + 2, y + height - 1, filledWidth, 1);
        else
            RenderUtils.drawRect(context.getMatrices(), ColorModule.INSTANCE.clientColor.getValue(), x + 2, y, filledWidth, height);

        RenderUtils.drawCustomString(context, Formatting.WHITE + setting.getName() + ": " + Formatting.GRAY + current, Color.WHITE, (int) (x + 4), (int) (y + 0.5f + height / 2 - RenderUtils.customFontHeight(11) / 2), 11);
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
        if (dragging) updateValue((int) mouseX);
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
    }

    private void updateValue(int mouseX) {
        double ratio = Math.max(0, Math.min(1, (mouseX - x - 2) / (width - 4)));
        setting.setValue(setting.getMin() + (int) Math.round(ratio * (setting.getMax() - setting.getMin())));
    }

    private boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}