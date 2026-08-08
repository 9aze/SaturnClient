package lat.saturn.gui.click.components.settings;

import lat.saturn.api.setting.settings.BoolSetting;
import lat.saturn.api.util.render.RenderUtils;
import lat.saturn.feature.module.client.ColorModule;
import lat.saturn.gui.click.components.ModuleSetting;
import lombok.Getter;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

@Getter
public class BoolSettingComponent extends ModuleSetting<BoolSetting> {
    private final BoolSetting setting;

    public BoolSettingComponent(BoolSetting setting, double x, double y, double width) {
        super(setting, x, y, width);
        this.setting = setting;
        this.height = mc.textRenderer.fontHeight + 2;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        if(setting.getValue()) {
            RenderUtils.drawRect(context.getMatrices(),  ColorModule.INSTANCE.clientColor.getValue(), x, y, width, height);
            RenderUtils.drawString(context, setting.getName(), Color.WHITE, (int) (x+4), (int) (y+1), true);
        } else {
            RenderUtils.drawRect(context.getMatrices(), new Color(20, 20 ,20 , 180), x, y, width, height);
            RenderUtils.drawString(context, setting.getName(), Color.LIGHT_GRAY, (int) (x+4), (int) (y+1), true);
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovering(mouseX, mouseY) && button == 0) {
            setting.setValue(!setting.getValue());
        }
    }
}
