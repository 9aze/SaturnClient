package lat.saturn.api.manager;

import lat.saturn.api.manager.module.ModuleManager;
import lat.saturn.api.manager.element.ElementManager;

// class to manage all managers and shit
public class Managers {
    public static ModuleManager MODULE_MANAGER;
    public static ElementManager ELEMENT_MANAGER;

    public void init() {
        MODULE_MANAGER = new ModuleManager();
        ELEMENT_MANAGER = new ElementManager();
    }
}