package ml.ikwid.transplantsmp.common.imixins;

import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public interface IPlayerInventoryTransplanted {
	DefaultedList<ItemStack> getSecondaryArmor();
}
