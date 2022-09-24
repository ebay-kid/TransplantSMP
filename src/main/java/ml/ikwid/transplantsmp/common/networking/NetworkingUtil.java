package ml.ikwid.transplantsmp.common.networking;

import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class NetworkingUtil {
	public static void sendTransplantTypeUpdate(String type, ServerPlayerEntity player) {
		PacketByteBuf buf3 = PacketByteBufs.create();
		buf3.writeString(type);
		ServerPlayNetworking.send(player, NetworkingIDs.UPDATE_TRANSPLANT_TYPE_S2C, buf3);
	}

	public static void sendTransplantCountUpdate(ServerPlayerEntity player) {
		ITransplantable transplantable = (ITransplantable) player;

		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeInt(transplantable.getTransplantedAmount());
		ServerPlayNetworking.send(player, NetworkingIDs.UPDATE_TRANSPLANT_COUNT_S2C, buf);
	}

	public static void sendTransplantNeedsChoose(ServerPlayerEntity player) {
		player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, Integer.MAX_VALUE, 5)); // it'll crash if you die so we make it not possible for you to die
		((ITransplantable) player).setIsSettingTransplant(true);

		ServerPlayNetworking.send(player, NetworkingIDs.NEEDS_TRANSPLANT_S2C, PacketByteBufs.empty());
	}

	public static void sendArmHasteBalanceAmountUpdate(List<ServerPlayerEntity> players, double newValue) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeDouble(newValue);

		for (ServerPlayerEntity player : players) {
			ServerPlayNetworking.send(player, NetworkingIDs.ARM_HASTE_BALANCE_AMOUNT_S2C, buf);
		}
	}

	public static void sendArmBalanceToggleUpdate(List<ServerPlayerEntity> players, boolean newValue) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBoolean(newValue);

		for (ServerPlayerEntity player : players) {
			ServerPlayNetworking.send(player, NetworkingIDs.BALANCE_ARM_TOGGLE_S2C, buf);
		}
	}
}