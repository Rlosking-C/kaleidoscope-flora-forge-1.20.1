package com.rlosking.flora.network;

import com.rlosking.flora.KaleidoscopeFlora;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import com.rlosking.flora.event.CakeEvents;

public final class FloraNetwork {

    private static final String PROTOCOL = "1";

    private static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(KaleidoscopeFlora.MOD_ID, "main"))
            .networkProtocolVersion(() -> PROTOCOL)
            .clientAcceptedVersions(PROTOCOL::equals)
            .serverAcceptedVersions(PROTOCOL::equals)
            .simpleChannel();

    private static int nextId;

    public static void register() {
        CHANNEL.registerMessage(nextId++, MidAirJumpMessage.class,
                MidAirJumpMessage::encode, MidAirJumpMessage::decode, MidAirJumpMessage::handle);
    }

    public static void sendMidAirJump(boolean stillRising) {
        CHANNEL.sendToServer(new MidAirJumpMessage(stillRising));
    }

    public record MidAirJumpMessage(boolean stillRising) {

        public static void encode(MidAirJumpMessage message, FriendlyByteBuf buf) {
            buf.writeBoolean(message.stillRising());
        }

        public static MidAirJumpMessage decode(FriendlyByteBuf buf) {
            return new MidAirJumpMessage(buf.readBoolean());
        }

        public static void handle(MidAirJumpMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (player != null) {
                    CakeEvents.noteMidAirJump(player, message.stillRising());
                }
            });
            context.setPacketHandled(true);
        }
    }

    private FloraNetwork() {
    }
}
