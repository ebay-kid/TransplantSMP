package ml.ikwid.transplantsmp.mixin.screen;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.imixins.ISlotTransplanted;
import ml.ikwid.transplantsmp.common.inventory.ArmorSlot;
import ml.ikwid.transplantsmp.common.inventory.HotbarSlot;
import ml.ikwid.transplantsmp.common.util.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ScreenHandler.class)
public abstract class MixinScreenHandler {
	@Shadow protected abstract Slot addSlot(Slot slot);

	private final ScreenHandler self = (ScreenHandler) (Object) this;

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Redirect(method = "addSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;add(Ljava/lang/Object;)Z", ordinal = 0))
	private boolean handleSpecialSlots(DefaultedList instance, Object o) {
		Slot slot = (Slot) o;
		int id = slot.id;
		boolean shouldAddLater = false;

		// TransplantSMP.LOGGER.info("Slot num: " + slot.getIndex());

		if(slot.inventory instanceof PlayerInventory playerInventory) {
			// TransplantSMP.LOGGER.info("Slot " + slot.getIndex() + " is a player inv. slot, "); // + slot.x + ", " + slot.y);
			int index = slot.getIndex();

			if(!(slot instanceof HotbarSlot)) { // if it's a hotbar slot, just skip as we dealt with it earlier
				if(slot instanceof ArmorSlot) {
					if(index < 40) {
						// TransplantSMP.LOGGER.info("armor slot initialized");
						slot = new ArmorSlot(playerInventory, index + 9, slot.x, slot.y);
					}
				} else {
					if (index < 9) {
						// TransplantSMP.LOGGER.info("it is also a hotbar slot");

						slot = new HotbarSlot(playerInventory, index, slot.x, slot.y);

						if(index == 8) { // hopefully not a futile attempt to fix the bug
							shouldAddLater = true;
						}
					} else if (index < 36) {
						// TransplantSMP.LOGGER.info("shift inv slot index");
						((ISlotTransplanted) slot).setIndex(index + 9);
					} else if (index == 40) {
						// TransplantSMP.LOGGER.info("it is the off hand");
						slot = new Slot(playerInventory, Constants.OFF_HAND, slot.x, slot.y);
					}
				}
			}

			// TransplantSMP.LOGGER.info("New: " + slot.getIndex() + " at " + slot.x + ", " + slot.y);
		}

		slot.id = id;
		// TransplantSMP.LOGGER.info("index is 8, adding extra slots.");
		boolean instanceAdd = instance.add(slot);
		if(shouldAddLater) {
			for (int i = 9; i < 18; i++) {
				this.addSlot(new HotbarSlot((PlayerInventory) (slot.inventory), i, 8 + (i - 9) * 18, slot.y + Constants.HOTBAR_SPACE_IN_INV_SCREEN));
			}
		}
		return instanceAdd;
	}

	@Redirect(method = "internalOnSlotClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;canTakeItems(Lnet/minecraft/entity/player/PlayerEntity;)Z"))
	private boolean log(Slot instance, PlayerEntity playerEntity) {
		boolean canTake = instance.canTakeItems(playerEntity);
		if(instance.inventory instanceof PlayerInventory) {
			// TransplantSMP.LOGGER.info("Slot " + instance.getIndex() + " is " + canTake);
		}
		return canTake;
	}
}
