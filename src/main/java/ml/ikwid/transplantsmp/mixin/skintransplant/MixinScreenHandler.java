package ml.ikwid.transplantsmp.mixin.skintransplant;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ScreenHandler.class)
public interface MixinScreenHandler {
	@Invoker("addSlot")
	Slot addSlotAccess(Slot slot);
}
