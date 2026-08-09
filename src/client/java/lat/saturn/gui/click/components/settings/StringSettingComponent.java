package lat.saturn.gui.click.components.settings;

import lat.saturn.api.setting.settings.StringSetting;
import lat.saturn.api.util.render.RenderUtils;
import lat.saturn.gui.click.components.ModuleSetting;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class StringSettingComponent extends ModuleSetting<StringSetting> {

    private final StringSetting setting;

    private boolean focused;
    private int cursor;
    private boolean selectAll;

    private long lastBlink;
    private boolean blinkOn = true;

    private static final int BOX_WIDTH = 60;

    public StringSettingComponent(StringSetting setting, double x, double y, double width) {
        super(setting, x, y, width);
        this.setting = setting;
        this.height = mc.textRenderer.fontHeight + 4;
        this.cursor = setting.getValue().length();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        String value = setting.getValue();
        boolean empty = value == null;

        int boxX = (int) x;
        int boxY = (int) y;
        int boxWidth = (int) width;
        int boxHeight = (int) height;


        int textY = (int) (boxY + (boxHeight / 2f) - (RenderUtils.customFontHeight(11) / 2f));

        if (empty) {
            RenderUtils.drawCustomString(context, setting.getName(), new Color(140, 140, 140), boxX + 4, textY, 11);
            return;
        }

        int maxTextWidth = boxWidth - 8;

        int start = 0;
        while (start < cursor && RenderUtils.customTextWidth(value.substring(start, cursor), 11) > maxTextWidth) {
            start++;
        }
        int end = value.length();
        while (end > start && RenderUtils.customTextWidth(value.substring(start, end), 11) > maxTextWidth) {
            end--;
        }
        String visible = value.substring(start, end);

        Color textColor = !value.isEmpty() ? Color.WHITE : Color.GRAY;
        RenderUtils.drawCustomString(context, value.isEmpty() ? setting.getName() + "..." : visible, textColor, boxX + 4, textY, 11);

        if (focused && selectAll && !value.isEmpty()) {
            int selWidth = (int) RenderUtils.customTextWidth(visible, 11);
            RenderUtils.drawRect(context.getMatrices(), new Color(80, 130, 200, 110), boxX + 4, boxY + 2, selWidth, boxHeight - 4);
        }

        if (focused && !selectAll) {
            long now = System.currentTimeMillis();
            if (now - lastBlink > 500) {
                blinkOn = !blinkOn;
                lastBlink = now;
            }
            if (blinkOn) {
                int localCursor = cursor - start;
                int cursorX = (int) (boxX + 4 + RenderUtils.customTextWidth(visible.substring(0, localCursor), 11));
                RenderUtils.drawRect(context.getMatrices(), Color.WHITE, cursorX, boxY + 2, 1, boxHeight - 4);
            }
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        boolean inBox = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;

        if (button == 0) {
            focused = inBox;
            selectAll = false;
            if (focused) {
                String value = setting.getValue();
                cursor = value == null ? 0 : value.length();
            }
        }
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!focused) return;

        String value = setting.getValue();
        boolean ctrl = net.minecraft.client.gui.screen.Screen.hasControlDown();

        switch (keyCode) {
            case 259:
                if (selectAll) clearAll();
                else if (cursor > 0) {
                    setting.setValue(value.substring(0, cursor - 1) + value.substring(cursor));
                    cursor--;
                }
                break;

            case 261:
                if (selectAll) clearAll();
                else if (cursor < value.length())
                    setting.setValue(value.substring(0, cursor) + value.substring(cursor + 1));
                break;

            case 263:
                selectAll = false;
                if (cursor > 0) cursor--;
                break;

            case 262:
                selectAll = false;
                if (cursor < value.length()) cursor++;
                break;

            case 268:
                selectAll = false;
                cursor = 0;
                break;

            case 269:
                selectAll = false;
                cursor = value.length();
                break;

            case 65:
                if (ctrl) selectAll = true;
                break;

            case 67:
                if (ctrl) mc.keyboard.setClipboard(value);
                break;

            case 86:
                if (ctrl) {
                    String clip = mc.keyboard.getClipboard();
                    if (clip != null && !clip.isEmpty()) {
                        clip = clip.replace("\n", " ").replace("\r", "");
                        if (selectAll) {
                            setting.setValue(clip);
                            cursor = setting.getValue().length();
                            selectAll = false;
                        } else {
                            String next = value.substring(0, cursor) + clip + value.substring(cursor);
                            setting.setValue(next);
                            cursor = Math.min(cursor + clip.length(), setting.getValue().length());
                        }
                    }
                }
                break;

            case 256:
                focused = false;
                selectAll = false;
                break;
        }
    }

    public void charTyped(char chr, int modifiers) {
        if (!focused || Character.isISOControl(chr)) return;

        String value = setting.getValue();
        if (selectAll) {
            setting.setValue(String.valueOf(chr));
            cursor = setting.getValue().length();
            selectAll = false;
        } else {
            String next = value.substring(0, cursor) + chr + value.substring(cursor);
            setting.setValue(next);
            if (setting.getValue().equals(next)) cursor++;
        }
    }

    private void clearAll() {
        setting.setValue("");
        cursor = 0;
        selectAll = false;
    }
}