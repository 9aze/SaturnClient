package lat.saturn.api.manager.module;


import lat.saturn.SaturnClient;
import lat.saturn.api.event.input.EventKey;
import lat.saturn.api.setting.Setting;
import lat.saturn.api.util.IMinecraft;
import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import meteordevelopment.orbit.EventHandler;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ModuleManager implements IMinecraft {
    private final Map<String, Module> byName = new Object2ObjectAVLTreeMap<>();
    private final Map<Class<? extends Module>, Module> byClass;
    private final Map<Category, List<Module>> byCategory;

    @Getter
    Set<Class<? extends Module>> modules;

    public ModuleManager() {
        modules = new Reflections("lat.saturn.feature.module").getSubTypesOf(Module.class);
        byClass = new Object2ObjectOpenHashMap<>(modules.size());
        byCategory = new Object2ObjectOpenHashMap<>(modules.size());
        try {
            for (Class<?> clazz : modules) {
                if (!clazz.isAnnotationPresent(RegisterModule.class)) continue;

                Module module = (Module) clazz.getDeclaredConstructor().newInstance();
                for (Field field : module.getClass().getDeclaredFields()) {
                    try {
                        if (!field.canAccess(module)) field.setAccessible(true);
                    } catch (IllegalArgumentException ignored) {
                        continue; // skip static fields
                    }
                    Object object = field.get(module);
                    if (object instanceof Setting<?, ?> s) module.settings.add(s);
                }

                SaturnClient.LOGGER.info("registered module {}", module);
                byName.put(module.getName(), module);
                byClass.put(module.getClass(), module);
                List<Module> list = byCategory.computeIfAbsent(module.getCategory(), category -> new ArrayList<>());
                list.add(module);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException exception) {
            SaturnClient.LOGGER.error("error registering module", exception);
            return;
        }
        SaturnClient.EVENT_BUS.subscribe(this);
    }

    public @Nullable Module getByClass(Class<? extends Module> cl) {
        return byClass.get(cl);
    }

    public @Nullable Module getByName(String name) {
        return byName.get(name);
    }

    public List<Module> getByCategory(Category category) {
        return byCategory.getOrDefault(category, new ArrayList<>());
    }

    // handle binds
    @EventHandler
    private void onKey(EventKey event) {
        handleBind(event.key, event.action);
    }

    private void handleBind(int inputCode, int action) {
        for (Module module : byName.values()) {
            if (mc.currentScreen != null && !module.isToggleInScreens()) continue;

            if (module.getBind() != inputCode) continue;

            if (module.getBindMode() == Module.BindMode.HOLD) {
                if (action == GLFW.GLFW_PRESS) {
                    module.setToggled(true);
                } else if (action == GLFW.GLFW_RELEASE) {
                    module.setToggled(false);
                }
            } else {
                if (action == GLFW.GLFW_PRESS) {
                    module.toggle();
                    System.out.println("Should toggle " + module.getName());
                }
            }
        }
    }

}