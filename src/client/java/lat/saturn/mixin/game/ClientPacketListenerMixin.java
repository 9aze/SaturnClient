package lat.saturn.mixin.game;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.network.packet.UnknownCustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.IDN;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPacketListenerMixin {

    private static final Identifier JOIN_ID = Identifier.of("anarchymod", "join");

    private static final Set<String> DOMAINS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            "*.6b6t.org",
            "*.10b10t.org",
            "*.6b6t.cc",
            "*.6b6t.me",
            "*.7b7t.me",
            "*.8b8t.org",
            "*.8b8t.xyz",
            "*.alacity.net",
            "*.anarchypvp.pw",
            "*.l2x9.org",
            "*.simpleanarchy.org"
    )));

    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        ServerInfo server = MinecraftClient.getInstance().getCurrentServerEntry();

        if (server == null || !containsDomain(server.address)) {
            return;
        }

        ClientPlayNetworkHandler listener = (ClientPlayNetworkHandler) (Object) this;

        listener.getConnection().send(
                new CustomPayloadC2SPacket(
                        new UnknownCustomPayload(JOIN_ID)
                )
        );
    }

    private static boolean containsDomain(String address) {
        String host = normalize(address);
        if (host == null) return false;

        for (String domain : DOMAINS) {
            String normalized = normalize(domain);
            if (normalized == null) continue;

            if (normalized.startsWith("*.")) {
                String base = normalized.substring(2);

                if (host.equals(base) || host.endsWith("." + base)) {
                    return true;
                }
            } else if (host.equals(normalized)) {
                return true;
            }
        }

        return false;
    }

    private static String normalize(String input) {
        if (input == null) return null;

        String host = input.trim();

        if (host.startsWith("*.")) {
            host = host.substring(2);
        }

        if (host.startsWith("[")) {
            int end = host.indexOf(']');
            if (end < 0) return null;
            host = host.substring(1, end);
        } else {
            int colon = host.lastIndexOf(':');

            if (colon >= 0 && host.indexOf(':') == colon) {
                host = host.substring(0, colon);
            }
        }

        while (host.endsWith(".")) {
            host = host.substring(0, host.length() - 1);
        }

        if (host.isEmpty()) return null;

        try {
            return host.indexOf(':') >= 0
                    ? host.toLowerCase(Locale.ROOT)
                    : IDN.toASCII(host, IDN.USE_STD3_ASCII_RULES)
                    .toLowerCase(Locale.ROOT);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }
}