package ml.ikwid.transplantsmp.mixin.screen;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.imixins.IHotbarScreenHandler;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.inventory.HotbarSlot;
import ml.ikwid.transplantsmp.common.util.Constants;
import net.minecraft.entity.player.PlayerEntity;
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

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Redirect(method = "addSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;add(Ljava/lang/Object;)Z", ordinal = 0))
	private boolean handleSpecialSlots(DefaultedList instance, Object o) {
		Slot slot = (Slot) o;

		TransplantSMP.LOGGER.info("Slot num: " + slot.getIndex());

		if(slot.inventory instanceof PlayerInventory) {
			// TransplantSMP.LOGGER.info("Slot " + slot.getIndex() + " is a player inv. slot, "); // + slot.x + ", " + slot.y);
			if(slot.getIndex() < 18) {
				// TransplantSMP.LOGGER.info("it is also a hotbar slot");

				PlayerInventory playerInventory = (PlayerInventory) (slot.inventory);
				ITransplantable transplantable = (ITransplantable)(playerInventory.player);

				slot = new HotbarSlot(playerInventory, slot.getIndex(), slot.x + transplantable.innerSlotXShift(), slot.y);

				HotbarSlot hotbarSlot = (HotbarSlot) slot;
				hotbarSlot.updateEnabledState();

				//TransplantSMP.LOGGER.info("New: " + slot.x + ", " + slot.y);
				this.hotbarSlots.add(hotbarSlot);
			} else if(slot.getIndex() == Constants.OFF_HAND) {
				// TransplantSMP.LOGGER.info("it is the off hand");
				slot = new Slot(slot.inventory, Constants.OFF_HAND, slot.x, slot.y);
			}
		}

		return instance.add(slot);
	}

	@Override
	public ArrayList<HotbarSlot> getHotbarSlots() {
		return this.hotbarSlots;
	}

	@Override
	public int getHighestEnabledSlot() {
		for(HotbarSlot slot : this.hotbarSlots) {
			if(!slot.isEnabled()) {
				return slot.getIndex() - 1;
			}
		}
		return 17;
	}

	@Redirect(method = "internalOnSlotClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;canTakeItems(Lnet/minecraft/entity/player/PlayerEntity;)Z"))
	private boolean log(Slot instance, PlayerEntity playerEntity) {
		boolean canTake = instance.canTakeItems(playerEntity);
		if(instance.inventory instanceof PlayerInventory) {
			TransplantSMP.LOGGER.info("Slot " + instance.getIndex() + " is " + canTake);
		}
		return canTake;
	}
}
