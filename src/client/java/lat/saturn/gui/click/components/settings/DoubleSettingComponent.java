package lat.saturn.gui.click.components.settings;

import lat.saturn.api.setting.settings.DoubleSetting;
import lat.saturn.api.util.render.RenderUtils;
import lat.saturn.feature.module.client.ClickGUIModule;
import lat.saturn.feature.module.client.ColorModule;
import lat.saturn.gui.click.components.ModuleSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Formatting;

import java.awt.*;

public class DoubleSettingComponent extends ModuleSetting {
    private final DoubleSetting setting;
    private boolean dragging = false;

    public DoubleSettingComponent(DoubleSetting setting, double x, double y, double width) {
        super(setting, x, y, width);
        this.setting = setting;
        this.height = mc.textRenderer.fontHeight + 4;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        double min = setting.getMin(), max = setting.getMax(), current = setting.getValue();
        double ratio = Math.max(0, Math.min(1, (current - min) / (max - min)));
        int sliderWidth = (int) width - 4, filledWidth = (int) (sliderWidth * ratio);

        if (!ClickGUIModule.INSTANCE.thickSliders.getValue())
            RenderUtils.drawRect(context.getMatrices(), ColorModule.INSTANCE.clientColor.getValue(), x + 2, y + height - 1, filledWidth, 1);
        else
            RenderUtils.drawRect(context.getMatrices(), ColorModule.INSTANCE.clientColor.getValue(), x + 2, y, filledWidth, height);

        int textX = (int) (x+4);
        int textY =  (int)(y + 0.5f + (height / 2) - (RenderUtils.customFontHeight(11) / 2));

        if (isMouseOver(mouseX, mouseY)) {
            switch (ClickGUIModule.INSTANCE.hoverEffect.getValue()) {
                case ClickGUIModule.HoverEffect.Right -> textX = (int) (x+6);
                case ClickGUIModule.HoverEffect.Up -> textY = (int)(y + 0.5f + (height / 2) - (RenderUtils.customFontHeight(11) / 2)) - 2;
                case ClickGUIModule.HoverEffect.Highlight -> RenderUtils.drawRoundedRect(context.getMatrices(), new Color(255, 255, 255, 50), x, y, width, height, ClickGUIModule.INSTANCE.moduleRadius.getValue(), ClickGUIModule.INSTANCE.moduleRadius.getValue(), ClickGUIModule.INSTANCE.moduleRadius.getValue(), ClickGUIModule.INSTANCE.moduleRadius.getValue(), 12);
                default -> {}
            }
        }

        RenderUtils.drawCustomString(context, Formatting.WHITE + setting.getName() + ": " + Formatting.GRAY + Math.floor(current * 100) / 100, Color.WHITE, textX, textY, 11);
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
        setting.setValue(setting.getMin() + ratio * (setting.getMax() - setting.getMin()));
    }

    private boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}