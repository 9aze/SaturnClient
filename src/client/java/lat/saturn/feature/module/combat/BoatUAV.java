package lat.saturn.feature.module.combat;

import net.minecraft.client.MinecraftClient;
import lat.saturn.api.manager.module.Category;
import lat.saturn.api.manager.module.Module;
import lat.saturn.api.manager.module.RegisterModule;
import lat.saturn.api.setting.settings.BoolSetting;
import lat.saturn.api.setting.settings.DoubleSetting;
import lat.saturn.api.setting.settings.IntSetting;
import lat.saturn.api.event.world.EventTick;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

@RegisterModule(name="Boat UAV", description = "Boat Instakill", category = Category.COMBAT, toggled = false)
public class BoatUAV extends Module {

    BoolSetting autoTrack = new BoolSetting("Auto Track", "Automatically tracks the closest player's X and Z coordinates.", true);
    IntSetting charge = new IntSetting("Charge", "How long to charge the bow before releasing in ticks.", 5, 5, 20);
    DoubleSetting maxYDelta = new DoubleSetting("Max Y Delta", "Maximum vertical movement per tick.", 5.0, 0.1, 10.0);
    DoubleSetting maxXZDelta = new DoubleSetting("Max XZ Delta", "Maximum horizontal movement per tick.", 0.5, 0.05, 5.0);
    IntSetting bowSpamDelay = new IntSetting("Bow Spam Delay", "Delay between bow releases in ticks.", 2, 1, 20);

    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private enum State { MOVING_UP_Y_1, MOVING_UP_Y_2, MOVING_DOWN_Y }
    private State currentState = State.MOVING_UP_Y_1;
    private PlayerEntity targetPlayer = null;
    private Vec3d lastSentPos = null;
    private double yTarget = 0.0;
    private boolean wasBow = false;
    private int bowTickCounter = 0;

    public static boolean findItem(net.minecraft.item.Item targetItem) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null) return false;

        for (int i = 0; i < client.player.getInventory().size(); i++) {
            ItemStack stack = client.player.getInventory().getStack(i);
            if (stack.getItem() == targetItem) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onEnable() {
        if (mc.player == null || mc.world == null || mc.player.networkHandler == null) {
            mc.player.sendMessage(Text.literal("p/w/n handler is null, deactivating."), true);;
            toggle();
            return;
        }

        if (!(mc.player.getVehicle() instanceof BoatEntity)) {
            mc.player.sendMessage(Text.literal("You must be in a boat to activate."), true);
            toggle();
            return;
        }

        targetPlayer = findClosestPlayer();
        if (targetPlayer == null) {
            mc.player.sendMessage(Text.literal("No target player found, deactivating."), true);
            toggle();
            return;
        }

        currentState = State.MOVING_UP_Y_1;
        lastSentPos = mc.player.getPos();
        yTarget = targetPlayer.getY() + 40;
        wasBow = false;
        bowTickCounter = 0;
    }

    @Override
    public void onDisable() {
        targetPlayer = null;
        currentState = State.MOVING_UP_Y_1;
        lastSentPos = null;
        yTarget = 0.0;
        setPressed(false);
        wasBow = false;
        bowTickCounter = 0;
        //mc.player.sendMessage(Text.literal("Module deactivated."), true);
    }

    @EventHandler
    public void onTick(EventTick event)  {
        if (mc.player == null || mc.world == null || mc.player.networkHandler == null) {
            mc.player.sendMessage(Text.literal("p/w/n handler is null, deactivating."), true);
            toggle();
            return;
        }

        if (!(mc.player.getVehicle() instanceof BoatEntity)) {
            mc.player.sendMessage(Text.literal("Not in a boat, deactivating."), true);
            toggle();
            return;
        }

        BoatEntity boat = (BoatEntity) mc.player.getVehicle();
        if (lastSentPos == null) {
            lastSentPos = boat.getPos();
        }

        if (targetPlayer == null || !targetPlayer.isAlive() || !mc.world.getPlayers().contains(targetPlayer)) {
            targetPlayer = findClosestPlayer();
            if (targetPlayer == null) {
                mc.player.sendMessage(Text.literal("No target player found, deactivating."), true);
                toggle();
                return;
            }

            if (currentState == State.MOVING_UP_Y_1) {
                yTarget = targetPlayer.getY() + 40;
            }
        }

        if (!autoTrack.getValue()) return;

        Vec3d targetPos = targetPlayer.getPos();
        double finalYTarget = currentState == State.MOVING_UP_Y_2 ? targetPos.y + 80 : targetPos.y + 10;

        handleMovement(boat, targetPos, finalYTarget);

        if (currentState == State.MOVING_DOWN_Y) {
            if (!mc.player.getAbilities().creativeMode && !findItem(Items.ARROW)) {
                mc.player.sendMessage(Text.literal("No arrows found in inventory, stopping bow spam."), true);
                toggle();
                return;
            }

            boolean isBow = mc.player.getMainHandStack().getItem() == Items.BOW;
            if (!isBow && wasBow) setPressed(false);

            wasBow = isBow;
            if (!isBow) return;

            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(
                    mc.player.getYaw(), 90.0f, mc.player.isOnGround(), false
            ));

            if (bowTickCounter > 0) {
                bowTickCounter--;
                return;
            }

            if (mc.player.getItemUseTime() >= charge.getValue()) {
                mc.interactionManager.stopUsingItem(mc.player);
                bowTickCounter = bowSpamDelay.getValue();
            } else {
                setPressed(true);
            }
        } else {
            setPressed(false);
        }
    }

    private void handleMovement(BoatEntity boat, Vec3d targetPos, double targetY) {
        double currentX = lastSentPos.x;
        double currentY = lastSentPos.y;
        double currentZ = lastSentPos.z;

        double yDelta = (currentState == State.MOVING_UP_Y_1 ? yTarget : targetY) - currentY;
        double moveY = Math.max(-maxYDelta.getValue(), Math.min(maxYDelta.getValue(), yDelta));

        double xDelta = targetPos.x - currentX;
        double zDelta = targetPos.z - currentZ;
        double moveX = Math.max(-maxXZDelta.getValue(), Math.min(maxXZDelta.getValue(), xDelta));
        double moveZ = Math.max(-maxXZDelta.getValue(), Math.min(maxXZDelta.getValue(), zDelta));

        Vec3d newPos = new Vec3d(currentX + moveX, currentY + moveY, currentZ + moveZ);

        if (Math.abs(yDelta) < maxYDelta.getValue()) {
            if (currentState == State.MOVING_UP_Y_1) {
                currentState = State.MOVING_UP_Y_2;
                yTarget = targetPos.y + 80;
            } else if (currentState == State.MOVING_UP_Y_2) {
                currentState = State.MOVING_DOWN_Y;
            } else if (currentState == State.MOVING_DOWN_Y) {
                currentState = State.MOVING_UP_Y_1;
                yTarget = targetPos.y + 40;
            }
        }

        sendMovePacket(boat, newPos);
    }

    private void sendMovePacket(BoatEntity boat, Vec3d newPos) {
        mc.player.networkHandler.sendPacket(new VehicleMoveC2SPacket(newPos, boat.getYaw(), boat.getPitch(), false));
        lastSentPos = newPos;
    }

    private PlayerEntity findClosestPlayer() {
        if (mc.world == null || mc.player == null) return null;
        PlayerEntity closest = null;
        double closestDistance = Double.MAX_VALUE;
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player != mc.player && !player.isSpectator() && player.isAlive()) {
                double distance = mc.player.getPos().distanceTo(player.getPos());
                if (distance < closestDistance) {
                    closest = player;
                    closestDistance = distance;
                }
            }
        }
        return closest;
    }

    private void setPressed(boolean pressed) {
        mc.options.useKey.setPressed(pressed);
    }

}
