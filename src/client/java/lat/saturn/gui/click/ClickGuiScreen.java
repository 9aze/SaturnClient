package lat.saturn.gui.click;

import lat.saturn.api.manager.module.Category;
import lat.saturn.gui.click.components.CategoryPane;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class ClickGuiScreen extends Screen {
    private static final int PANE_SPACING = 4;
    private static final int PANE_WIDTH = 100;
    private final ArrayList<CategoryPane> panes = new ArrayList<>();

    public ClickGuiScreen() {
        super(Text.of("Click GUI"));

        // init category panes
        int startX = 10;

        for(Category category : Category.values()) {
            int paneX = startX + (panes.size() * (PANE_WIDTH + PANE_SPACING));
            int paneY = 20;

            CategoryPane pane = new CategoryPane(category, paneX, paneY, PANE_WIDTH);
            panes.add(pane);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        for(CategoryPane pane : panes) {
            pane.render(context, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(CategoryPane pane : panes) {
            pane.mouseClicked(mouseX, mouseY, button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for(CategoryPane pane : panes) {
            pane.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for(CategoryPane pane : panes) {
            pane.mouseReleased(mouseX, mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
