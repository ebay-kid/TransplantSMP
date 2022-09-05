package ml.ikwid.transplantsmp.common.networking;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.client.screen.ChooseTransplantScreen;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class NetworkingHandlerClient {
	public static CompletableFuture<PacketByteBuf> handleHandshakeClientSide(MinecraftClient minecraftClient, ClientLoginNetworkHandler clientLoginNetworkHandler, PacketByteBuf packetByteBuf, Consumer<GenericFutureListener<? extends Future<? super Void>>> genericFutureListenerConsumer) {
		PacketByteBuf buf = PacketByteBufs.create();
		for(int i : TransplantSMP.SEMVER) {
			buf.writeInt(i);
		}
		return CompletableFuture.completedFuture(buf);
	}

	public static void updateOrganCount(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
		int count = packetByteBuf.readInt();
		minecraftClient.execute(() -> {
			//noinspection ConstantConditions
			((ITransplantable)(minecraftClient.player)).setTransplantedAmount(count, true, false);
			TransplantSMP.LOGGER.info("count updated -client, count = " + count);
		});
	}

	public static void updateTransplantType(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
		String type = packetByteBuf.readString();
		minecraftClient.execute(() -> {
			//noinspection ConstantConditions
			((ITransplantable)(minecraftClient.player)).setTransplantType(TransplantType.get(type), true);
			minecraftClient.setScreen(null);

			TransplantSMP.LOGGER.info("type updated -client, type = " + type);
		});
	}

	public static void setTransplantNeededScreen(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
		minecraftClient.execute(() -> minecraftClient.setScreen(new ChooseTransplantScreen()));
	}
}
