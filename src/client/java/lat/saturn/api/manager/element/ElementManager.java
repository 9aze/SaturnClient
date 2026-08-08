package lat.saturn.api.manager.element;


import lat.saturn.SaturnClient;
import lat.saturn.api.setting.Setting;
import lat.saturn.api.util.IMinecraft;
import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ElementManager implements IMinecraft {
    private final Map<String, Element> byName = new Object2ObjectAVLTreeMap<>();
    private final Map<Class<? extends Element>, Element> byClass;
    private final Map<HudCategory, List<Element>> byCategory;

    @Getter
    Set<Class<? extends Element>> elements;

    public ElementManager() {
        elements = new Reflections("lat.saturn.feature.element").getSubTypesOf(Element.class);
        byClass = new Object2ObjectOpenHashMap<>(elements.size());
        byCategory = new Object2ObjectOpenHashMap<>(elements.size());
        try {
            for (Class<?> clazz : elements) {
                if (!clazz.isAnnotationPresent(RegisterElement.class)) continue;

                Element element = (Element) clazz.getDeclaredConstructor().newInstance();
                for (Field field : element.getClass().getDeclaredFields()) {
                    try {
                        if (!field.canAccess(element)) field.setAccessible(true);
                    } catch (IllegalArgumentException ignored) {
                        continue; // skip static fields
                    }
                    Object object = field.get(element);
                    if (object instanceof Setting<?, ?> s) element.settings.add(s);
                }

                SaturnClient.LOGGER.info("registered element {}", element);
                byName.put(element.getName(), element);
                byClass.put(element.getClass(), element);
                List<Element> list = byCategory.computeIfAbsent(element.getCategory(), category -> new ArrayList<>());
                list.add(element);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException exception) {
            SaturnClient.LOGGER.error("error registering element", exception);
            return;
        }
        SaturnClient.EVENT_BUS.subscribe(this);
    }

    public @Nullable Element getByClass(Class<? extends Element> cl) {
        return byClass.get(cl);
    }

    public @Nullable Element getByName(String name) {
        return byName.get(name);
    }

    public List<Element> getByCategory(HudCategory category) {
        return byCategory.getOrDefault(category, new ArrayList<>());
    }

}