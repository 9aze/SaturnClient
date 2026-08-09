package lat.saturn.gui.click.components;

import lat.saturn.api.manager.Managers;
import lat.saturn.api.manager.module.Category;
import lat.saturn.api.manager.module.Module;
import lat.saturn.api.util.IMinecraft;
import lat.saturn.api.util.render.RenderUtils;
import lat.saturn.feature.module.client.ClickGUIModule;
import lat.saturn.feature.module.client.ColorModule;
import lat.saturn.gui.click.ToolTip;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.ArrayList;

public class CategoryPane implements IMinecraft {

    private final Category category;
    private final Identifier icon;
    private double x, y, width;
    private boolean open = true;
    private boolean dragging;
    private double dragX, dragY;
    private double titleHeight = mc.textRenderer.fontHeight + 6;
    private ArrayList<ModuleButton> moduleButtons = new ArrayList<>();

    public CategoryPane(Category category, double x, double y, double width) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;
        this.icon = Identifier.of("saturnclient", "icons/" + category.getIcon());

        double moduleY = y + titleHeight + 3;
        for (Module module : Managers.MODULE_MANAGER.getByCategory(category)) {
            ModuleButton button = new ModuleButton(module, x + 2, moduleY, width - 4);
            moduleButtons.add(button);
            moduleY += button.getHeight() + 2;
        }
    }

    private double getModuleAreaHeight() {
        if (moduleButtons.isEmpty()) return 0;

        double height = 3;
        for (ModuleButton moduleButton : moduleButtons) {
            height += moduleButton.getHeight() + 2;
        }

        return height;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Color outlineColor = ColorModule.INSTANCE.clientColor.getValue();
        double totalHeight = titleHeight + (open ? getModuleAreaHeight() : 0);

        RenderUtils.drawRoundedRect(context.getMatrices(), new Color(17, 17, 17), x, y, width, titleHeight, ClickGUIModule.INSTANCE.categoryRadius.getValue(), ClickGUIModule.INSTANCE.categoryRadius.getValue(), 0f, 0f, 12);

        int iconSize = 12;
        int iconX = (int) x + 4;
        int iconY = (int) (y + (titleHeight - iconSize) / 2);

        Color color = ColorModule.INSTANCE.clientColor.getValue();

        context.drawTexture(
                RenderLayer::getGuiTextured,
                icon,
                iconX, iconY,
                0, 0,
                iconSize, iconSize,
                iconSize, iconSize,
                color.getRGB()
        );

        RenderUtils.drawCustomString(context, category.getName(), new Color(255, 255, 255, 255), iconX + iconSize + 2, (int) (y - 1), 11);

        String tooltip = null;

        if (open && !moduleButtons.isEmpty()) {
            double areaHeight = getModuleAreaHeight();

            RenderUtils.drawRoundedRect(context.getMatrices(), new Color(17, 17, 17), x, y + titleHeight, width, areaHeight, 0f, 0f, ClickGUIModule.INSTANCE.categoryRadius.getValue(), ClickGUIModule.INSTANCE.categoryRadius.getValue(), 12);

            double moduleY = y + titleHeight + 3;
            for (ModuleButton moduleButton : moduleButtons) {
                moduleButton.setPos(x + 2, moduleY);
                moduleButton.render(context, mouseX, mouseY, delta);

                String description = moduleButton.getHoveredDescription(mouseX, mouseY);
                if (description != null) {
                    tooltip = description;
                }

                moduleY += moduleButton.getHeight() + 2;
            }

            RenderUtils.drawRect(context.getMatrices(), outlineColor, x, y + titleHeight, width, 1);
        }

        RenderUtils.drawRoundedOutline(context.getMatrices(), outlineColor, x, y, width, totalHeight, ClickGUIModule.INSTANCE.categoryRadius.getValue(), ClickGUIModule.INSTANCE.categoryRadius.getValue(), ClickGUIModule.INSTANCE.categoryRadius.getValue(), ClickGUIModule.INSTANCE.categoryRadius.getValue(), 1f, 12);

        if (tooltip != null && !tooltip.isEmpty()) {
            ToolTip.render(context, tooltip, mouseX, mouseY);
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHoveringTitle(mouseX, mouseY)) {
            if (button == 1) {
                open = !open;
            } else if (button == 0) {
                dragging = true;
                dragX = mouseX - x;
                dragY = mouseY - y;
            }
        }

        if (open) {
            for (ModuleButton moduleButton : moduleButtons) {
                moduleButton.mouseClicked(mouseX, mouseY, button);
            }
        }
    }

    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging && button == 0) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }

        if (open) {
            for (ModuleButton moduleButton : moduleButtons) {
                moduleButton.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
            }
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;

        if (open) {
            for (ModuleButton moduleButton : moduleButtons) {
                moduleButton.mouseReleased(mouseX, mouseY, button);
            }
        }
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (open) {
            for (ModuleButton moduleButton : moduleButtons) {
                moduleButton.keyPressed(keyCode, scanCode, modifiers);
            }
        }
    }

    public void charTyped(char chr, int modifiers) {
        if (open) {
            for (ModuleButton moduleButton : moduleButtons) {
                moduleButton.charTyped(chr, modifiers);
            }
        }
    }

    public boolean isHoveringTitle(double mx, double my) {
        return mx >= x && mx <= x + width && my >= y && my <= y + titleHeight;
    }
}