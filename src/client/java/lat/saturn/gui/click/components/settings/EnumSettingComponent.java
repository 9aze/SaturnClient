package lat.saturn.gui.click.components.settings;

import lat.saturn.api.setting.settings.BoolSetting;
import lat.saturn.api.setting.settings.EnumSetting;
import lat.saturn.api.util.render.RenderUtils;
import lat.saturn.feature.module.client.ColorModule;
import lat.saturn.gui.click.components.ModuleSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Formatting;

import java.awt.*;

public class EnumSettingComponent extends ModuleSetting {
    private final EnumSetting setting;

    public EnumSettingComponent(EnumSetting setting, double x, double y, double width) {
        super(setting, x, y, width);
        this.setting = setting;
        this.height = mc.textRenderer.fontHeight + 4;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        RenderUtils.drawCustomString(context, Formatting.WHITE + setting.getName() + ": " + Formatting.GRAY + setting.getValue(), Color.WHITE, (int)(x + 4), (int)(y + 0.5f + (height / 2) - (RenderUtils.customFontHeight(11) / 2)), 11);
    }


    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovering(mouseX, mouseY) && button == 0) {
            setting.setNextValue();
        }
    }
}
