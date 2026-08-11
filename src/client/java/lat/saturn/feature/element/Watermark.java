package lat.saturn.feature.element;

import lat.saturn.SaturnClient;
import lat.saturn.api.event.render.EventRender2D;
import lat.saturn.api.manager.element.Element;
import lat.saturn.api.manager.element.RegisterElement;
import lat.saturn.api.setting.settings.BoolSetting;
import lat.saturn.api.setting.settings.ColorSetting;
import lat.saturn.api.setting.settings.StringSetting;
import lat.saturn.api.util.render.RenderUtils;
import lat.saturn.feature.module.client.ColorModule;
import lat.saturn.feature.module.client.HudEditor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.awt.*;

@RegisterElement(name = "Watermark", x = 0.0, y = 0.0, description = "Tuff watermark", toggled = true)
public class Watermark extends Element {

    private StringSetting title = new StringSetting("Title", "The first section of the watermark", "Saturn Client", 64, false);
    private BoolSetting version = new BoolSetting("Version", "Show client version in the watermark", true);
    private ColorSetting titleColor = new ColorSetting("Title Color", "Color of the title text, syncs with accent color by default", new Color(163, 97, 150), true, false, false, true, true);
    private ColorSetting versionColor = new ColorSetting("Version Color", "Color of the version text", new Color(255, 255, 255), true, false, false, true, false);

    @EventHandler
    public void onRender2D(EventRender2D event) {
        if (!this.isToggled()) return;

        MutableText watermark = Text.literal(title.getValue())
                .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(titleColor.getValue().getRGB() & 0xFFFFFF)));

        if (version.getValue()) {
            watermark.append(Text.literal(" v" + SaturnClient.MOD_VERSION)
                    .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(versionColor.getValue().getRGB() & 0xFFFFFF))));
        }

        setSize(watermark);
        RenderUtils.drawHUDString(event.context, watermark, (float) getScreenX(), (float) getScreenY(), 11);
    }

    public void setSize(Text text) {
        boolean custom = HudEditor.INSTANCE.customFont.getValue();
        setWidth(custom ? (int) RenderUtils.customTextWidth(text, 11) : mc.textRenderer.getWidth(text));
        setHeight(custom ? (int) RenderUtils.customFontHeight(11) - 2 : mc.textRenderer.fontHeight);
    }
}