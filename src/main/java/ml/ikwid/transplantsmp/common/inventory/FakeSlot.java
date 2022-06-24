package ml.ikwid.transplantsmp.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class FakeSlot extends Slot {
	public FakeSlot(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}

	@Override
	public int getMaxItemCount() {
		return 0;
	}

	@Override
	public boolean canTakeItems(PlayerEntity player) {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}


	@Override
	public boolean canInsert(ItemStack stack) {
		return false;
	}
}
