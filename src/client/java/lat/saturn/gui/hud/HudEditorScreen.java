package lat.saturn.gui.hud;

import lat.saturn.api.manager.Managers;
import lat.saturn.api.manager.element.Element;
import lat.saturn.api.manager.element.HudCategory;
import lat.saturn.api.util.render.RenderUtils;
import lat.saturn.feature.module.client.ClickGUIModule;
import lat.saturn.gui.click.components.CategoryPane;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class HudEditorScreen extends Screen {

    private static final int PANE_WIDTH = 115;

    private final CategoryPane pane;
    private final ArrayList<ElementFrame> frames = new ArrayList<>();

    public HudEditorScreen() {
        super(Text.of("Hud Editor"));
        pane = new CategoryPane(HudCategory.HUD, 20, 20, PANE_WIDTH);

        for(Element element : Managers.ELEMENT_MANAGER.getByCategory(HudCategory.HUD))
            frames.add(new ElementFrame(element));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderUtils.drawRect(context.getMatrices(), ClickGUIModule.INSTANCE.bgColor.getValue(), 0, 0, MinecraftClient.getInstance().getWindow().getScaledWidth(), MinecraftClient.getInstance().getWindow().getScaledHeight());
        pane.render(context, mouseX, mouseY, delta);

        for(ElementFrame ef : frames)
            ef.render(context, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        pane.mouseClicked(mouseX, mouseY, button);

        for(ElementFrame ef : frames)
            if(ef.mouseClicked(mouseX, mouseY, button))
                return true;

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        pane.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);

        for(ElementFrame ef : frames)
            if(ef.mouseDragged(mouseX, mouseY, button))
                return true;

        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        pane.mouseReleased(mouseX, mouseY, button);

        for(ElementFrame ef : frames)
            if(ef.mouseReleased(mouseX, mouseY, button))
                return true;

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        pane.keyPressed(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        pane.charTyped(chr, modifiers);
        return super.charTyped(chr, modifiers);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
