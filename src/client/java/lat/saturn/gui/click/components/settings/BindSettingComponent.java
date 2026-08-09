package lat.saturn.gui.click.components.settings;

import lat.saturn.api.manager.module.Module;
import lat.saturn.api.setting.Setting;
import lat.saturn.api.util.render.RenderUtils;
import lat.saturn.gui.click.components.ModuleSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Formatting;

import java.awt.Color;

public class BindSettingComponent extends ModuleSetting<BindSettingComponent.BindSetting> {

    private final Module module;
    private boolean listening;

    private static BindSettingComponent activeListener;

    private static final int UNBOUND = 0;
    private static final float TEXT_SIZE = 11f;
    private static final int BADGE_WIDTH = 16;

    public BindSettingComponent(Module module, double x, double y, double width) {
        super(new BindSetting(module), x, y, width);
        this.module = module;
        this.height = mc.textRenderer.fontHeight + 4;
    }

    static class BindSetting extends Setting<Integer, BindSetting> {
        BindSetting(Module module) {
            super(module.getName() + " Bind", "Keybind for " + module.getName(), module.getBind());
        }
    }

    private String bindName() {
        int bind = module.getBind();
        if (bind <= 0) return "None";

        try {
            String name = InputUtil.fromKeyCode(bind, 0).getLocalizedText().getString();
            if (name == null || name.isBlank() || name.contains(".key.") || name.contains(".action."))
                return "None";
            return name;
        } catch (Exception e) {
            return "None";
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        int boxX = (int) x;
        int boxY = (int) y;
        int boxWidth = (int) width;
        int boxHeight = (int) height;

        String label = listening
                ? Formatting.GRAY + "Press a key..."
                : Formatting.WHITE + "Bind: " + Formatting.GRAY + bindName();

        float textY = (float) (boxY + (boxHeight / 2f)
                - (RenderUtils.customFontHeight(TEXT_SIZE) / 2f));

        RenderUtils.drawCustomString(
                context, label, Color.WHITE, boxX + 4, textY, TEXT_SIZE
        );

        String modeLabel = module.getBindMode() == Module.BindMode.HOLD ? "[H]" : "[T]";
        int badgeX = boxX + boxWidth - BADGE_WIDTH - 3;
        int badgeY = boxY + 2;
        int badgeHeight = boxHeight - 4;

        float modeTextWidth = RenderUtils.customTextWidth(modeLabel, 9f);
        float modeTextX = badgeX + (BADGE_WIDTH - modeTextWidth) / 2f;
        float modeTextY = badgeY
                + (badgeHeight - RenderUtils.customFontHeight(9f)) / 2f;

        RenderUtils.drawCustomString(
                context, modeLabel, Color.GRAY, modeTextX, modeTextY, 9f
        );
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        boolean inBox = mouseX >= x && mouseX <= x + width
                && mouseY >= y && mouseY <= y + height;

        if (!inBox) {
            if (listening) {
                listening = false;
                if (activeListener == this) activeListener = null;
            }
            return;
        }

        switch (button) {
            case 0 -> {
                if (activeListener != null && activeListener != this)
                    activeListener.listening = false;

                listening = true;
                activeListener = this;
            }

            case 1 -> {
                module.setBind(UNBOUND);
                setting.setValue(UNBOUND);

                listening = false;
                if (activeListener == this) activeListener = null;
            }

            case 2 -> module.setBindMode(
                    module.getBindMode() == Module.BindMode.HOLD
                            ? Module.BindMode.TOGGLE
                            : Module.BindMode.HOLD
            );
        }
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!listening) return;

        module.setBind(keyCode);
        setting.setValue(keyCode);

        listening = false;
        if (activeListener == this) activeListener = null;
    }

    @Override
    public boolean isHovering(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width
                && mouseY >= y && mouseY <= y + height;
    }
}