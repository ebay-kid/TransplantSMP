package ml.ikwid.transplantsmp.common.util;

public class Utils {
	/**
	 * Translates the selected slot to a hotbar number.
	 * @param selSlot The index of the slot in the inventory
	 * @return the corrected value
	 */
	public static int translateSlotToHotbar(int selSlot) {
		// TransplantSMP.LOGGER.info("sel slot: " + selSlot);

		return selSlot < 9 ? selSlot : selSlot - Constants.NEW_HOTBAR_START_LOC + 9;
	}

	/**
	 * Translates the hotbar slot number back to an inventory slot value
	 * @param slot The hotbar slot number
	 * @return The index within the main inventory
	 */
	public static int translateHotbarToSlot(int slot) {
		return slot < 9 ? slot : slot + Constants.NEW_HOTBAR_START_LOC - 9;
	}
}
