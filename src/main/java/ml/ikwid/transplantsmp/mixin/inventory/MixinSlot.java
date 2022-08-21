package ml.ikwid.transplantsmp.mixin.inventory;

import ml.ikwid.transplantsmp.common.imixins.ISlotTransplanted;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Slot.class)
public abstract class MixinSlot implements ISlotTransplanted {
	@Mutable
	@Final
	@Shadow private int index;

	@Override
	public void setIndex(int index) {
		this.index = index;
	}
}
