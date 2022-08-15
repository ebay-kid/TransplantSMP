package ml.ikwid.transplantsmp.common.util;

import com.mojang.authlib.GameProfile;
import ml.ikwid.transplantsmp.common.TransplantType;
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
		// TransplantSMP.LOGGER.info("sel slot: " + selSlot);

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

	/**
	 * Given an arbitrary slot index and the number of draws needed, calculate the exact starting x (left-side) to draw the slot. Note that it is the SLOT, not the hotbar icon itself.
	 * @param draws The total number of hotbar draws (see {@link ITransplantable#getHotbarDraws()})
	 * @param slotIndex The being-drawn slot's index
	 * @return The x of the left side of the slot (where drawing begins)
	 */
	public static int calcSlotXShiftArb(int draws, int slotIndex) {
		return 8 + (slotIndex * Constants.INNER_SLOT_WIDTH) -((draws - 9) * Constants.INNER_SLOT_WIDTH / 2);
	}

	public static boolean bannableAmount(ITransplantable transplantable) {
		return transplantable.getTransplantType() == TransplantType.ARM_TRANSPLANT ? transplantable.getTransplantedAmount() <= -16 : transplantable.getTransplantedAmount() <= -18;
	}

	public static void ban(ServerPlayerEntity serverPlayerEntity) {
		GameProfile playerProfile = serverPlayerEntity.getGameProfile();

		BannedPlayerList bannedPlayerList = Objects.requireNonNull(serverPlayerEntity.getServer()).getPlayerManager().getUserBanList();
		bannedPlayerList.add(new BannedPlayerEntry(playerProfile, null, "The all-powerful server", null, "You died too much (SO BAD XD)"));

		serverPlayerEntity.networkHandler.disconnect(Text.translatable("multiplayer.disconnect.banned"));
	}
}
