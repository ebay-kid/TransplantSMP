package ml.ikwid.transplantsmp.common.networking;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.api.TransplantType;
import ml.ikwid.transplantsmp.api.TransplantTypes;
import ml.ikwid.transplantsmp.client.TransplantSMPClient;
import ml.ikwid.transplantsmp.client.screen.ChooseTransplantScreen;
import ml.ikwid.transplantsmp.api.ITransplantable;
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
			buf.writeInt(i); // Write the Main Mod SEMVER
		}
		for(TransplantType type : TransplantTypes.getTransplantTypes()) {
			buf.writeString(type.getName()); // Write the name of all registered transplants. TODO: Versions of the transplants/their mods, but that's slightly more complicated and raises other issues re. sending mod list to the server.
		}

		return CompletableFuture.completedFuture(buf);
	}

	public static void updateOrganCount(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
		int count = packetByteBuf.readInt();
		minecraftClient.execute(() -> {
			//noinspection ConstantConditions
			((ITransplantable)(minecraftClient.player)).setTransplantedAmount(count, true);
		});
	}

	public static void updateTransplantType(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
		String type = packetByteBuf.readString();
		minecraftClient.execute(() -> {
			//noinspection ConstantConditions
			((ITransplantable)(minecraftClient.player)).setTransplantType(TransplantTypes.get(type), true);
			minecraftClient.setScreen(null);
		});
	}

	public static void setTransplantNeededScreen(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
		minecraftClient.execute(() -> minecraftClient.setScreen(new ChooseTransplantScreen()));
	}

	public static void updateArmBalanceAmount(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
		double amount = packetByteBuf.readDouble();
		minecraftClient.execute(() -> TransplantSMPClient.armHasteBalanceAmount = amount);
	}

	public static void updateArmBalanceToggle(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
		boolean toggle = packetByteBuf.readBoolean();
		minecraftClient.execute(() -> TransplantSMPClient.balanceArm = toggle);
	}
}
