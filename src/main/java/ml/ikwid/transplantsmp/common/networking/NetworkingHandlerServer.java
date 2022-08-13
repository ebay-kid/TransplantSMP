package ml.ikwid.transplantsmp.common.networking;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

@SuppressWarnings("unused")
public class NetworkingHandlerServer {
	public static void handshake(ServerLoginNetworkHandler serverLoginNetworkHandler, MinecraftServer minecraftServer, PacketSender packetSender, ServerLoginNetworking.LoginSynchronizer loginSynchronizer) {
		packetSender.sendPacket(NetworkingIDs.HANDSHAKE, PacketByteBufs.empty());
	}

	public static void handleHandshakeServerSide(MinecraftServer minecraftServer, ServerLoginNetworkHandler serverLoginNetworkHandler, boolean understood, PacketByteBuf packetByteBuf, ServerLoginNetworking.LoginSynchronizer loginSynchronizer, PacketSender packetSender) {
		if(!understood) {
			serverLoginNetworkHandler.disconnect(Text.literal("This server requires TransplantSMP version " + TransplantSMP.VERSION + "to join."));
			return;
		}

		int[] clientSemVer = new int[3];
		for(int i = 0; i < 3; i++) {
			clientSemVer[i] = packetByteBuf.readInt();
			if(clientSemVer[i] != TransplantSMP.SEMVER[i]) {
				serverLoginNetworkHandler.disconnect(Text.literal("This server requires TransplantSMP version " + TransplantSMP.VERSION + "to join."));
			}
		}
	}

	public static void chosenTransplantType(MinecraftServer minecraftServer, ServerPlayerEntity player, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
		String chosenType = packetByteBuf.readString();
		minecraftServer.execute(() -> {
			ITransplantable transplantable = (ITransplantable) player;
			if(transplantable.getTransplantType() == null) {
				TransplantType transplantType = TransplantType.get(chosenType);

				transplantable.setTransplantType(transplantType, false);
				transplantable.setTransplantedAmount(0, true, true);


				TransplantSMP.LOGGER.info("transplant chosen for " + player.getName().getString());
			} else {
				TransplantSMP.LOGGER.info("someone's being sussy and trying to change transplants (" + player.getName().getString() + ")!");
			}
		});
	}
}
