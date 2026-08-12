package lat.saturn.api.event.net;

import lat.saturn.api.event.Cancellable;
import lombok.Getter;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;

public class EventPacket {
    public static class Receive extends Cancellable {
        @Getter
        public Packet<?> packet;
        public ClientConnection connection;

        public Receive(Packet<?> packet, ClientConnection connection) {
            this.setCancelled(false);
            this.packet = packet;
            this.connection = connection;
        }
    }

    public static class Send extends Cancellable {
        @Getter
        public Packet<?> packet;
        public ClientConnection connection;

        public Send(Packet<?> packet, ClientConnection connection) {
            this.setCancelled(false);
            this.packet = packet;
            this.connection = connection;
        }
    }

    public static class Sent {
        @Getter
        public Packet<?> packet;
        public ClientConnection connection;

        public Sent(Packet<?> packet, ClientConnection connection) {
            this.packet = packet;
            this.connection = connection;
        }
    }
}