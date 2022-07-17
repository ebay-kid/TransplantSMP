package ml.ikwid.transplantsmp.mixin.screen;

import ml.ikwid.transplantsmp.common.imixins.ISlotTransplanted;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.util.Utils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GenericContainerScreenHandler.class)
public abstract class MixinGenericContainerScreenHandler {
	/*
	private final GenericContainerScreenHandler self = (GenericContainerScreenHandler)(Object) this;

	@Unique
	private PlayerEntity capturedPlayer;

	@Redirect(method = "<init>(Lnet/minecraft/screen/ScreenHandlerType;ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/GenericContainerScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 1))
	private Slot capturePlayer(GenericContainerScreenHandler instance, Slot slot) {
		this.capturedPlayer = ((PlayerInventory) (slot.inventory)).player;
		return ((MixinScreenHandlerAccessor) self).addSlotAccess(slot);
	}

	@ModifyConstant(method = "<init>(Lnet/minecraft/screen/ScreenHandlerType;ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;I)V", constant = @Constant(intValue = 9, ordinal = 6))
	private int changeSlotDrawCount(int constant) {
		return ((ITransplantable) capturedPlayer).getHotbarDraws();
	}

	@Redirect(method = "<init>(Lnet/minecraft/screen/ScreenHandlerType;ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/GenericContainerScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 2))
	private Slot changeHotbarSlotLocation(GenericContainerScreenHandler instance, Slot slot) {
		((ISlotTransplanted) slot).setX(slot.x + Utils.innerSlotXShift(((PlayerInventory)(slot.inventory)).player));
		return ((MixinScreenHandlerAccessor) self).addSlotAccess(slot);
	}
	*/
}
