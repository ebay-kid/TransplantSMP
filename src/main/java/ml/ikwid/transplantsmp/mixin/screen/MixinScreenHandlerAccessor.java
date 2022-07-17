package ml.ikwid.transplantsmp.mixin.screen;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ScreenHandler.class)
public interface MixinScreenHandlerAccessor {
	@Invoker("addSlot")
	Slot addSlotAccess(Slot slot);
}
