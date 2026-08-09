package lat.saturn.api.manager.config;

import com.google.gson.*;
import lat.saturn.SaturnClient;
import lat.saturn.api.manager.Managers;
import lat.saturn.api.manager.element.Element;
import lat.saturn.api.manager.module.Module;
import lat.saturn.api.setting.Setting;
import lat.saturn.api.setting.settings.ColorSetting;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class ConfigManager {
    private static final Path CONFIG_PATH = Paths.get("saturn", "config.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public void save() {
        try {
            Path parent = CONFIG_PATH.getParent();
            if (parent != null) Files.createDirectories(parent);

            JsonObject root = new JsonObject();
            root.add("modules", saveModules());
            root.add("elements", saveElements());

            Files.writeString(CONFIG_PATH, GSON.toJson(root), StandardCharsets.UTF_8);
            SaturnClient.LOGGER.info("Saved config");
        } catch (IOException e) {
            SaturnClient.LOGGER.error("Failed to save config", e);
        }
    }

    public void load() {
        try {
            if (!Files.exists(CONFIG_PATH)) {
                save();
                return;
            }

            JsonObject root = JsonParser.parseString(
                    Files.readString(CONFIG_PATH, StandardCharsets.UTF_8)
            ).getAsJsonObject();

            if (root.has("modules")) loadModules(root.getAsJsonObject("modules"));
            if (root.has("elements")) loadElements(root.getAsJsonObject("elements"));
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Failed to load config", e);
        }
    }

    private JsonObject saveModules() {
        JsonObject modules = new JsonObject();

        for (Class<? extends Module> clazz : Managers.MODULE_MANAGER.getModules()) {
            Module module = Managers.MODULE_MANAGER.getByClass(clazz);
            if (module == null) continue;

            JsonObject json = new JsonObject();
            json.addProperty("toggled", module.isToggled());
            json.addProperty("bind", module.getBind());
            json.addProperty("bindMode", module.getBindMode().name());
            json.add("settings", saveSettings(module.getSettings()));

            modules.add(module.getName(), json);
        }

        return modules;
    }

    private void loadModules(JsonObject modules) {
        for (Class<? extends Module> clazz : Managers.MODULE_MANAGER.getModules()) {
            Module module = Managers.MODULE_MANAGER.getByClass(clazz);
            if (module == null || !modules.has(module.getName())) continue;

            JsonObject json = modules.getAsJsonObject(module.getName());

            if (json.has("toggled"))
                module.setToggled(json.get("toggled").getAsBoolean());

            if (json.has("bind")) {
                int bind = json.get("bind").getAsInt();
                module.setBind(bind > 0 ? bind : 0);
            }

            if (json.has("bindMode")) {
                try {
                    module.setBindMode(Module.BindMode.valueOf(
                            json.get("bindMode").getAsString().toUpperCase()
                    ));
                } catch (Exception ignored) {
                    module.setBindMode(Module.BindMode.TOGGLE);
                }
            }

            if (json.has("settings"))
                loadSettings(json.getAsJsonObject("settings"), module.getSettings());
        }
    }

    private JsonObject saveElements() {
        JsonObject elements = new JsonObject();

        for (Class<? extends Element> clazz : Managers.ELEMENT_MANAGER.getElements()) {
            Element element = Managers.ELEMENT_MANAGER.getByClass(clazz);
            if (element == null) continue;

            JsonObject json = new JsonObject();
            json.addProperty("toggled", element.isToggled());
            json.add("settings", saveSettings(element.getSettings()));
            elements.add(element.getName(), json);
        }

        return elements;
    }

    private void loadElements(JsonObject elements) {
        for (Class<? extends Element> clazz : Managers.ELEMENT_MANAGER.getElements()) {
            Element element = Managers.ELEMENT_MANAGER.getByClass(clazz);
            if (element == null || !elements.has(element.getName())) continue;

            JsonObject json = elements.getAsJsonObject(element.getName());

            if (json.has("toggled"))
                element.setToggled(json.get("toggled").getAsBoolean());

            if (json.has("settings"))
                loadSettings(json.getAsJsonObject("settings"), element.getSettings());
        }
    }

    private JsonObject saveSettings(Iterable<Setting<?, ?>> settings) {
        JsonObject json = new JsonObject();

        for (Setting<?, ?> setting : settings) {
            Object value = setting.getValue();
            if (value == null) continue;

            if (setting instanceof ColorSetting colorSetting) {
                JsonObject color = new JsonObject();
                color.addProperty("color", colorSetting.getValue().getRGB());
                color.addProperty("rainbow", colorSetting.isRainbow());
                color.addProperty("sync", colorSetting.isSync());
                json.add(setting.getName(), color);
            } else {
                json.add(setting.getName(), GSON.toJsonTree(value));
            }
        }

        return json;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void loadSettings(JsonObject json, Iterable<Setting<?, ?>> settings) {
        for (Setting<?, ?> setting : settings) {
            if (!json.has(setting.getName())) continue;

            JsonElement element = json.get(setting.getName());
            if (element.isJsonNull()) continue;

            try {
                if (setting instanceof ColorSetting colorSetting) {
                    JsonObject color = element.getAsJsonObject();

                    if (color.has("color"))
                        colorSetting.setValue(new java.awt.Color(
                                color.get("color").getAsInt(), true
                        ));

                    if (color.has("rainbow"))
                        colorSetting.setRainbow(
                                color.get("rainbow").getAsBoolean()
                        );

                    if (color.has("sync"))
                        colorSetting.setSync(
                                color.get("sync").getAsBoolean()
                        );

                    continue;
                }

                Object currentValue = setting.getValue();
                if (currentValue == null) continue;

                Object value = GSON.fromJson(element, currentValue.getClass());
                ((Setting) setting).setValue(value);
            } catch (Exception e) {
                SaturnClient.LOGGER.error(
                        "Failed to load setting {}", setting.getName(), e
                );
            }
        }
    }
}
