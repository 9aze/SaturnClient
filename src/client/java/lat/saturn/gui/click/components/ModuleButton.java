package lat.saturn.gui.click.components;

import lat.saturn.api.manager.module.Module;
import lat.saturn.api.setting.settings.*;
import lat.saturn.api.util.IMinecraft;
import lat.saturn.api.util.render.RenderUtils;
import lat.saturn.feature.module.client.ClickGUIModule;
import lat.saturn.feature.module.client.ColorModule;
import lat.saturn.gui.click.components.settings.*;
import lombok.Getter;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.util.ArrayList;

public class ModuleButton implements IMinecraft {
    private final Module module;
    private double x, y, width;
    private boolean open = false;
    private final ArrayList<ModuleSetting<?>> moduleSettings = new ArrayList<>();

    @Getter
    private double height;

    private double titleHeight = mc.textRenderer.fontHeight + 4;

    public ModuleButton(Module module, double x, double y, double width) {
        this.module = module;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = titleHeight;

        for (var setting : module.getSettings()) {
            ModuleSetting<?> component = switch (setting) {
                case ColorSetting colorSetting -> new ColorSettingComponent(colorSetting, x, y, width);
                case BoolSetting boolSetting -> new BoolSettingComponent(boolSetting, x, y, width);
                case DoubleSetting doubleSetting -> new DoubleSettingComponent(doubleSetting, x, y, width);
                case IntSetting intSetting -> new IntSettingComponent(intSetting, x, y, width);
                case EnumSetting enumSetting -> new EnumSettingComponent(enumSetting, x, y, width);
                case null, default -> null;
            };

            if (component != null) {
                moduleSettings.add(component);
            }
        }
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Color bgColor = module.isToggled() ? ColorModule.INSTANCE.clientColor.getValue() : new Color(17, 17, 17);

        if(!open) {
            RenderUtils.drawRoundedRect(context.getMatrices(), bgColor, x, y, width, titleHeight, ClickGUIModule.INSTANCE.moduleRadius.getValue(), ClickGUIModule.INSTANCE.moduleRadius.getValue(), ClickGUIModule.INSTANCE.moduleRadius.getValue(), ClickGUIModule.INSTANCE.moduleRadius.getValue(), 12);
        } else {
            RenderUtils.drawRoundedRect(context.getMatrices(), bgColor, x+1, y+1, width-2, titleHeight-1, ClickGUIModule.INSTANCE.moduleRadius.getValue(), ClickGUIModule.INSTANCE.moduleRadius.getValue(), ClickGUIModule.INSTANCE.moduleRadius.getValue(), ClickGUIModule.INSTANCE.moduleRadius.getValue(), 12);
        }
        height = titleHeight;

        if (open && !moduleSettings.isEmpty()) {
            double settingsY = y + titleHeight;

            for (ModuleSetting<?> setting : moduleSettings) {
                setting.setPosition(x, settingsY);
                setting.render(context, mouseX, mouseY, delta);
                settingsY += setting.getHeight();
            }

            height = settingsY - y + 3;

            RenderUtils.drawRoundedOutline(context.getMatrices(), ColorModule.INSTANCE.clientColor.getValue(), x+1, y+1, width-2, height-2, ClickGUIModule.INSTANCE.moduleRadius.getValue(), ClickGUIModule.INSTANCE.moduleRadius.getValue(), ClickGUIModule.INSTANCE.moduleRadius.getValue(), ClickGUIModule.INSTANCE.moduleRadius.getValue(), 1.0f, 12.0f);
        }



        RenderUtils.drawCustomString(
                context,
                module.getName(),
                new Color(255, 255, 255, 255),
                (int) x + 2,
                (int) (y - 1),
                11
        );
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHoveringTitle(mouseX, mouseY)) {
            if (button == 0) {
                module.toggle();
            } else if (button == 1) {
                open = !open;
            }
        }

        if (open) {
            for (ModuleSetting<?> setting : moduleSettings) {
                if (setting.isHovering(mouseX, mouseY)) {
                    setting.mouseClicked((int) mouseX, (int) mouseY, button);
                    break;
                }
            }
        }
    }

    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if(open) {
            for (ModuleSetting<?> setting : moduleSettings) {
                setting.mouseDragged(mouseX, mouseY, button);
            }
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        if(open) {
            for (ModuleSetting<?> setting : moduleSettings) {
                setting.mouseReleased(mouseX, mouseY, button);
            }
        }
    }

    public void setPos(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean isHoveringTitle(double mx, double my) {
        return mx >= x && mx <= x + width && my >= y && my <= y + titleHeight;
    }
}
