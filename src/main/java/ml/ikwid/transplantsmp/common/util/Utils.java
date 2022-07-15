package ml.ikwid.transplantsmp.common.util;

import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;

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

	public static int innerSlotXShift(PlayerEntity playerEntity) {
		ITransplantable transplantable = (ITransplantable) playerEntity;

		if(transplantable == null) {
			return 0;
		}
		return -((transplantable.getHotbarDraws() - 9) * Constants.INNER_SLOT_WIDTH / 2);
	}

	public static int calcSlotXShiftArb(int draws) {
		return -((draws - 9) * Constants.INNER_SLOT_WIDTH / 2);
	}

	public static int mapSlotIndexToID(int index) {
		if(index >= Constants.NEW_ARMOR_START_LOC && index < Constants.NEW_ARMOR_START_LOC + 4) {
			return index - Constants.NEW_ARMOR_START_LOC + 4;
		}
		if(index >= Constants.EXTRA_HOTBAR_START_LOC + 9 && index < Constants.EXTRA_HOTBAR_START_LOC + 9 + 27) {
			return index - Constants.EXTRA_HOTBAR_START_LOC + 4;
		}
		return -1;
	}
}
