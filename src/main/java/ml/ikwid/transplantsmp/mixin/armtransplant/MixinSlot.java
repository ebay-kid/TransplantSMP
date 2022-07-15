package ml.ikwid.transplantsmp.mixin.armtransplant;

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
	@Shadow public int x;

	@Mutable
	@Final
	@Shadow public int y;

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}
}
