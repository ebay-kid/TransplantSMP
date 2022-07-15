package ml.ikwid.transplantsmp.common.networking;

import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class NetworkingUtil {
	public static void sendTransplantTypeUpdate(String type, ServerPlayerEntity player) {
		PacketByteBuf buf3 = PacketByteBufs.create();
		buf3.writeString(type);
		ServerPlayNetworking.send(player, NetworkingConstants.UPDATE_TRANSPLANT_TYPE, buf3);
	}

	public static void sendTransplantCountUpdate(ServerPlayerEntity player) {
		ITransplantable transplantable = (ITransplantable) player;

		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeInt(transplantable.getTransplantedAmount());
		ServerPlayNetworking.send(player, NetworkingConstants.UPDATE_ORGAN_COUNT, buf);
	}

	public static void sendTransplantNeedsChoose(ServerPlayerEntity player) {
		ServerPlayNetworking.send(player, NetworkingConstants.NEEDS_TRANSPLANT, PacketByteBufs.empty());
	}
}