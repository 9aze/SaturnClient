package lat.saturn.feature.module.movement;

import lat.saturn.api.manager.module.Category;
import lat.saturn.api.manager.module.Module;
import lat.saturn.api.manager.module.RegisterModule;
import lat.saturn.api.setting.settings.DoubleSetting;
import lat.saturn.mixin.game.EntityVelocityUpdateS2CPacketAccessor;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

@RegisterModule(name = "Velocity", description = "Modifies knockback taken from entities.", category = Category.MOVEMENT, toggled = false)
public class Velocity extends Module {
    private static Velocity INSTANCE;

    DoubleSetting horizontal = new DoubleSetting("Horizontal", "Horizontal knockback multiplier.", 0.0, 0.0, 1.0);
    DoubleSetting vertical = new DoubleSetting("Vertical", "Vertical knockback multiplier.", 0.0, 0.0, 1.0);

    public Velocity() {
        INSTANCE = this;
    }

    public static boolean handleVelocity(EntityVelocityUpdateS2CPacket packet) {
        if (INSTANCE == null || !INSTANCE.isToggled()) return false;
        if (INSTANCE.mc.player == null || packet.getEntityId() != INSTANCE.mc.player.getId()) return false;

        double h = INSTANCE.horizontal.getValue();
        double v = INSTANCE.vertical.getValue();

        if (h == 0.0 && v == 0.0) return true; // no knockback

        EntityVelocityUpdateS2CPacketAccessor accessor = (EntityVelocityUpdateS2CPacketAccessor) packet;
        accessor.setVelocityX((int) (packet.getVelocityX() * h * 8000.0));
        accessor.setVelocityY((int) (packet.getVelocityY() * v * 8000.0));
        accessor.setVelocityZ((int) (packet.getVelocityZ() * h * 8000.0));
        return false;
    }
}