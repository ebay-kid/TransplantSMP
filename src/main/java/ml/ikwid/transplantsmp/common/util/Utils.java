package ml.ikwid.transplantsmp.common.util;

import com.mojang.authlib.GameProfile;
import ml.ikwid.transplantsmp.api.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.minecraft.server.BannedPlayerEntry;
import net.minecraft.server.BannedPlayerList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Objects;

public class Utils {
	/**
	 * Translates the selected slot to a hotbar number.
	 * @param selSlot The index of the slot in the inventory
	 * @return the corrected value
	 */
	public static int translateSlotToHotbar(int selSlot) {
		return selSlot < 9 ? selSlot : selSlot - Constants.EXTRA_HOTBAR_START_LOC + 9;
	}

	/**
	 * Translates the hotbar slot number back to an inventory slot value
	 * @param slot The hotbar slot number
	 * @return The index within the main inventory
	 */
	public static int translateHotbarToSlot(int slot) {
		return slot < 9 ? slot : slot + Constants.EXTRA_HOTBAR_START_LOC - 9;
	}

	public static boolean bannableAmount(ITransplantable transplantable) {
		TransplantType transplantType = transplantable.getTransplantType();
		return transplantType.canTransplant((transplantable.getTransplantedAmount() - transplantType.getDefaultChangeByAmount()));
	}

	public static void ban(ServerPlayerEntity serverPlayerEntity) {
		GameProfile playerProfile = serverPlayerEntity.getGameProfile();

		BannedPlayerList bannedPlayerList = Objects.requireNonNull(serverPlayerEntity.getServer()).getPlayerManager().getUserBanList();
		bannedPlayerList.add(new BannedPlayerEntry(playerProfile, null, "TransplantSMP Mod", null, "Died too much"));

		serverPlayerEntity.networkHandler.disconnect(Text.translatable("multiplayer.disconnect.banned"));
	}

	public static boolean isExtraArmorSlot(int slot) {
		return slot >= Constants.EXTRA_ARMOR_START_LOC && slot <= Constants.EXTRA_ARMOR_START_LOC + 3;
	}
}
