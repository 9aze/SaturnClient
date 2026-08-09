package lat.saturn.feature.element;

import lat.saturn.api.event.render.EventRender2D;
import lat.saturn.api.manager.element.Element;
import lat.saturn.api.manager.element.RegisterElement;
import lat.saturn.api.manager.module.Category;
import lat.saturn.api.manager.module.RegisterModule;
import lat.saturn.api.util.render.RenderUtils;
import lat.saturn.feature.module.client.ClickGUIModule;
import lat.saturn.feature.module.client.ColorModule;
import lat.saturn.feature.module.client.HudEditor;
import meteordevelopment.orbit.EventHandler;

import java.awt.*;

@RegisterElement(name = "Watermark", x = 0.0, y = 0.0, description = "Tuff watermark", toggled = true)
public class Watermark extends Element {

    @EventHandler
    public void onRender2D(EventRender2D event) {
        if(!this.isToggled()) return;

        String text = "Saturn Client";
        RenderUtils.drawHUDString(event.context, "Saturn Client", ColorModule.INSTANCE.clientColor.getValue(), (float) getX(), (float) getY(), 11);
        setSize(text);
    }

    public void setSize(String text) {
        boolean custom = HudEditor.INSTANCE.customFont.getValue();
        setWidth(custom ? (int) RenderUtils.customTextWidth(text, 11) : mc.textRenderer.getWidth(text));
        setHeight(custom ? (int) RenderUtils.customFontHeight(11)-2 : mc.textRenderer.fontHeight);
    }
}
