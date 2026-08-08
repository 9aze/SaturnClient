package lat.saturn.gui.click.components;

import lat.saturn.api.manager.Managers;
import lat.saturn.api.manager.module.Category;
import lat.saturn.api.manager.module.Module;
import lat.saturn.api.util.IMinecraft;
import lat.saturn.api.util.render.RenderUtils;
import lat.saturn.feature.module.client.ColorModule;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.util.ArrayList;

public class CategoryPane implements IMinecraft {
    private final Category category;
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

        double moduleY = y + titleHeight;
        for (Module module : Managers.MODULE_MANAGER.getByCategory(category)) {
            moduleButtons.add(new ModuleButton(module, x, moduleY, width));
            moduleY += titleHeight;
        }
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // render title
        RenderUtils.drawRect(context.getMatrices(), ColorModule.INSTANCE.clientColor.getValue(), x, y, width, titleHeight);
        RenderUtils.drawString(context, category.getName(), new Color(255, 255, 255, 255), (int) x + 4, (int) (y + 3), true);

        // render module btns
        if (open) {
            double moduleY = y + titleHeight;
            for (ModuleButton moduleButton : moduleButtons) {
                moduleButton.setPos(x, moduleY);
                moduleButton.render(context, mouseX, mouseY, delta);
                moduleY += moduleButton.getHeight();
            }
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHoveringTitle(mouseX, mouseY)) {
            if (button == 1) { //right click -> open/close
                open = !open;
            } else if (button == 0) { //left click -> dragging
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

    public boolean isHoveringTitle(double mx, double my) {
        return mx >= x && mx <= x + width && my >= y && my <= y + titleHeight;
    }
}