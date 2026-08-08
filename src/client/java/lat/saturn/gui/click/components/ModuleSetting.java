package lat.saturn.gui.click.components;

import lat.saturn.api.setting.Setting;
import lat.saturn.api.util.IMinecraft;
import lombok.Getter;
import net.minecraft.client.gui.DrawContext;

public abstract class ModuleSetting<T extends Setting<?, ?>> implements IMinecraft {
    public final T setting;

    @Getter
    public double x, y;
    @Getter
    public double width;
    @Getter
    public double height;

    public ModuleSetting(T setting, double x, double y, double width) {
        this.setting = setting;
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean isHovering(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width &&
                mouseY >= y && mouseY <= y + height;
    }

    public boolean isVisible() {
        return setting != null && setting.isVisible();
    }

    public abstract void render(DrawContext context, int mouseX, int mouseY, float partialTicks);
    public void mouseClicked(double mouseX, double mouseY, int button) {}
    public void mouseDragged(double mouseX, double mouseY, int button) {}
    public void mouseReleased(double mouseX, double mouseY, int button) {}
    public void mouseScrolled(double mouseX, double mouseY, double x, double y) {}
    public void keyPressed(int keyCode) {}
    public void charTyped(char chr, int modifiers) {}

    public T getSetting() {
        return setting;
    }
}