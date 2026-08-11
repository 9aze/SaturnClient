package lat.saturn.gui.click.components;

import lat.saturn.api.manager.module.Module;
import lat.saturn.api.setting.settings.*;
import lat.saturn.api.util.IMinecraft;
import lat.saturn.api.util.KeyUtils;
import lat.saturn.api.util.render.RenderUtils;
import lat.saturn.feature.module.client.ClickGUIModule;
import lat.saturn.feature.module.client.ColorModule;
import lat.saturn.gui.click.ToolTip;
import lat.saturn.gui.click.components.settings.*;
import lombok.Getter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Formatting;

import java.awt.*;
import java.util.ArrayList;

public class ModuleButton implements IMinecraft, PaneButton {
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
                case StringSetting stringSetting -> new StringSettingComponent(stringSetting, x, y, width);
                case null, default -> null;
            };

            if (component != null) {
                moduleSettings.add(component);
            }
        }
        if(module.isAlwaysActive()) return;
        moduleSettings.add(new BindSettingComponent(module, x, y, width));
    }

    @Override
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
                if (!setting.isVisible()) continue;

                setting.setPosition(x, settingsY);
                setting.render(context, mouseX, mouseY, delta);
                settingsY += setting.getHeight();
            }

            height = settingsY - y + 3;

            RenderUtils.drawRoundedOutline(context.getMatrices(), ColorModule.INSTANCE.clientColor.getValue(), x+1, y+1, width-2, height-2, ClickGUIModule.INSTANCE.moduleRadius.getValue(), ClickGUIModule.INSTANCE.moduleRadius.getValue(), ClickGUIModule.INSTANCE.moduleRadius.getValue(), ClickGUIModule.INSTANCE.moduleRadius.getValue(), 1.0f, 12.0f);
        }

        String text = module.getName();

        if(ClickGUIModule.INSTANCE.moduleBinds.getValue() && !KeyUtils.getKeyName(module.getBind()).equals("None")) {
            text = Formatting.WHITE + module.getName() + " " + Formatting.GRAY + "[" + KeyUtils.getKeyName(module.getBind()).toLowerCase() + "]";
        }

        int textX = (int) (x+2);
        int textY = (int) (y-1);

        if (isHoveringTitle(mouseX, mouseY)) {
            switch (ClickGUIModule.INSTANCE.hoverEffect.getValue()) {
                case ClickGUIModule.HoverEffect.Right -> {
                    textX = (int) (x+4);
                }
                case ClickGUIModule.HoverEffect.Up -> {
                    textY = (int) y-3;
                }
                case ClickGUIModule.HoverEffect.Highlight -> {
                    RenderUtils.drawRoundedRect(context.getMatrices(), ClickGUIModule.INSTANCE.highlightColor.getValue(), x, y, width, titleHeight, ClickGUIModule.INSTANCE.moduleRadius.getValue(), ClickGUIModule.INSTANCE.moduleRadius.getValue(), ClickGUIModule.INSTANCE.moduleRadius.getValue(), ClickGUIModule.INSTANCE.moduleRadius.getValue(), 12);
                }
                default -> {}
            }
        }

        RenderUtils.drawCustomString(context, text, new Color(255, 255, 255, 255), textX, textY, 11);
    }

    @Override
    public String getHoveredDescription(double mouseX, double mouseY) {
        if (isHoveringTitle(mouseX, mouseY)) {
            return module.getDescription();
        }

        if (open) {
            for (ModuleSetting<?> setting : moduleSettings) {
                if (!setting.isVisible()) continue;
                if (setting.isHovering(mouseX, mouseY)) {
                    return setting.getSetting().getDescription();
                }
            }
        }

        return null;
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (open) {
            boolean handled = false;
            for (ModuleSetting<?> setting : moduleSettings) {
                if (!setting.isVisible()) continue;

                if (!handled && setting.isHovering(mouseX, mouseY)) {
                    setting.mouseClicked((int) mouseX, (int) mouseY, button);
                    handled = true;
                } else if (setting instanceof StringSettingComponent stringSetting) {
                    stringSetting.mouseClicked((int) mouseX, (int) mouseY, button);
                } else if (setting instanceof ColorSettingComponent colorSetting) {
                    colorSetting.mouseClicked((int) mouseX, (int) mouseY, button);
                } else if (setting instanceof BindSettingComponent bindSetting) {
                    bindSetting.mouseClicked((int) mouseX, (int) mouseY, button);
                }
            }
        }

        if (isHoveringTitle(mouseX, mouseY)) {
            if (button == 0) {
                module.toggle();
            } else if (button == 1) {
                open = !open;
            }
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (open) {
            for (ModuleSetting<?> setting : moduleSettings) {
                if (!setting.isVisible()) continue;

                if (setting instanceof StringSettingComponent stringSetting) {
                    stringSetting.keyPressed(keyCode, scanCode, modifiers);
                } else if (setting instanceof ColorSettingComponent colorSetting) {
                    colorSetting.keyPressed(keyCode, scanCode, modifiers);
                } else if (setting instanceof BindSettingComponent bindSetting) {
                    bindSetting.keyPressed(keyCode, scanCode, modifiers);
                }
            }
        }
    }

    @Override
    public void charTyped(char chr, int modifiers) {
        if (open) {
            for (ModuleSetting<?> setting : moduleSettings) {
                if (!setting.isVisible()) continue;

                if (setting instanceof StringSettingComponent stringSetting) {
                    stringSetting.charTyped(chr, modifiers);
                } else if (setting instanceof ColorSettingComponent colorSetting) {
                    colorSetting.charTyped(chr, modifiers);
                }
            }
        }
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if(open) {
            for (ModuleSetting<?> setting : moduleSettings) {
                if (!setting.isVisible()) continue;
                setting.mouseDragged(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if(open) {
            for (ModuleSetting<?> setting : moduleSettings) {
                if (!setting.isVisible()) continue;
                setting.mouseReleased(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void setPos(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean isHoveringTitle(double mx, double my) {
        return mx >= x && mx <= x + width && my >= y && my <= y + titleHeight;
    }
}