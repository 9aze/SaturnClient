package lat.saturn.feature.element;

import lat.saturn.api.event.render.EventRender2D;
import lat.saturn.api.manager.element.Element;
import lat.saturn.api.manager.element.RegisterElement;
import lat.saturn.api.util.render.RenderUtils;
import lat.saturn.feature.module.client.ColorModule;
import lat.saturn.feature.module.client.HudEditor;
import meteordevelopment.orbit.EventHandler;

@RegisterElement(name = "Watermark", x = 0.0, y = 0.0, description = "Tuff watermark", toggled = true)
public class Watermark extends Element {
    @EventHandler
    public void onRender2D(EventRender2D event) {
        if(!this.isToggled()) return;
        String text = "Saturn Client";
        setSize(text);
        RenderUtils.drawHUDString(event.context, text, ColorModule.INSTANCE.clientColor.getValue(), (float) getScreenX(), (float) getScreenY(), 11);
    }

    public void setSize(String text) {
        boolean custom = HudEditor.INSTANCE.customFont.getValue();
        setWidth(custom ? (int) RenderUtils.customTextWidth(text, 11) : mc.textRenderer.getWidth(text));
        setHeight(custom ? (int) RenderUtils.customFontHeight(11) - 2 : mc.textRenderer.fontHeight);
    }
}