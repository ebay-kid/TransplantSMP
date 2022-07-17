package ml.ikwid.transplantsmp.mixin.screen;

import ml.ikwid.transplantsmp.common.imixins.IHotbarScreenHandler;
import ml.ikwid.transplantsmp.common.inventory.HotbarSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;

@Mixin(ScreenHandler.class)
public abstract class MixinScreenHandler implements IHotbarScreenHandler {
	@Unique
	private final ArrayList<HotbarSlot> hotbarSlots = new ArrayList<>(18);

	@SuppressWarnings("rawtypes")
	@Redirect(method = "addSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;add(Ljava/lang/Object;)Z", ordinal = 0))
	private boolean handleHotbarSlots(DefaultedList instance, Object o) {
		Slot slot = (Slot) o;
		if(slot.inventory instanceof PlayerInventory && slot.getIndex() < 18) {
			PlayerInventory playerInventory = (PlayerInventory)(slot.inventory);
			slot = new HotbarSlot(playerInventory, slot.getIndex(), slot.x, slot.y);

			this.hotbarSlots.add((HotbarSlot) slot);
		}

		//noinspection unchecked
		return instance.add(slot);
	}

	@Override
	public ArrayList<HotbarSlot> getHotbarSlots() {
		return this.hotbarSlots;
	}
}
