package lat.saturn;

import lat.saturn.api.manager.Managers;
import lat.saturn.api.manager.config.ConfigManager;
import net.fabricmc.api.ModInitializer;
import meteordevelopment.orbit.EventBus;
import meteordevelopment.orbit.IEventBus;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class SaturnClient implements ModInitializer {
    public static final String NAME = BuildConstants.MOD_NAME;
    public static final String MOD_ID = BuildConstants.MOD_ID;
    public static final String MOD_VERSION = BuildConstants.MOD_VERSION;
    public static final String MINECRAFT_VERSION = BuildConstants.MINECRAFT_VERSION;
    public static final Logger LOGGER = LoggerFactory.getLogger("SaturnClient");
    public static final IEventBus EVENT_BUS = new EventBus();

    public static SaturnClient INSTANCE;
    public static Managers MANAGERS;
    public static ConfigManager CONFIG;

    @Override
    public void onInitialize() {
        long startTime = System.currentTimeMillis();
        INSTANCE = this;

        LOGGER.info("Initialization process for {} has started.", NAME);

        ((EventBus) EVENT_BUS).registerLambdaFactory("lat.saturn",
                (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(
                        null, klass, MethodHandles.lookup()
                ));

        MANAGERS = new Managers();
        MANAGERS.init();

        CONFIG = new ConfigManager();
        MinecraftClient.getInstance().execute(() -> CONFIG.load());

        EVENT_BUS.subscribe(this);

        Runtime.getRuntime().addShutdownHook(new Thread(CONFIG::save));

        LOGGER.info("Initialization process for {} has finished in {}ms.",
                NAME, System.currentTimeMillis() - startTime);
    }
}