package lat.saturn;

import lat.saturn.api.manager.Managers;
import net.fabricmc.api.ModInitializer;
import meteordevelopment.orbit.EventBus;
import meteordevelopment.orbit.IEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lat.saturn.BuildConstants;

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


    @Override
    public void onInitialize() {
        long start_time = System.currentTimeMillis();
        LOGGER.info("Initialization process for {} has started.", NAME);

        ((EventBus) EVENT_BUS).registerLambdaFactory("lat.saturn",
                (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));

        MANAGERS = new Managers();
        MANAGERS.init();

        EVENT_BUS.subscribe(this);

        LOGGER.info(String.format("Initialization process for %s has finished in %sms.", NAME, System.currentTimeMillis() - start_time));
    }
}
