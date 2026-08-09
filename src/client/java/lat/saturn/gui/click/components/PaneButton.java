package lat.saturn.gui.click.components;

import net.minecraft.client.gui.DrawContext;

public interface PaneButton {
    void render(DrawContext context, int mouseX, int mouseY, float delta);
    void mouseClicked(double mouseX, double mouseY, int button);
    void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY);
    void mouseReleased(double mouseX, double mouseY, int button);
    void keyPressed(int keyCode, int scanCode, int modifiers);
    void charTyped(char chr, int modifiers);
    String getHoveredDescription(double mouseX, double mouseY);
    void setPos(double x, double y);
    double getHeight();
}