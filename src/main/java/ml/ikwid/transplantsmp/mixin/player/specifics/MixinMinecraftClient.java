package ml.ikwid.transplantsmp.mixin.player.specifics;

import ml.ikwid.transplantsmp.client.TransplantSMPClient;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.transplants.ArmTransplant;
import ml.ikwid.transplantsmp.common.transplants.RegisterTransplants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {
	@Shadow public ClientPlayerEntity player;

	@ModifyConstant(method = "handleInputEvents", constant = @Constant(intValue = 9, ordinal = 0))
	private int increaseCheckedHotkeys(int constant) {
		ITransplantable transplantable = (ITransplantable) player;
		if(transplantable.getTransplantType() != RegisterTransplants.ARM_TRANSPLANT) {
			return constant;
		}
		ArmTransplant armTransplant = (ArmTransplant) (transplantable.getTransplantType());
		return armTransplant.getHotbarDraws(player);
	}

	@Redirect(method = "handleInputEvents", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerInventory;selectedSlot:I", opcode = Opcodes.PUTFIELD))
	private void allowSecondaryHotkeyMethod(PlayerInventory instance, int value) {
		ITransplantable transplantable = (ITransplantable) player;
		if(transplantable.getTransplantType() != RegisterTransplants.ARM_TRANSPLANT) {
			return;
		}
		ArmTransplant armTransplant = (ArmTransplant) (transplantable.getTransplantType());
		if(value < 9 && TransplantSMPClient.hotbarSwapKeybind.isPressed()) {
			int ret = value + 9;
			if(ret < armTransplant.getHotbarDraws(player)) {
				instance.selectedSlot = ret;
				return;
			}
		}
		instance.selectedSlot = value;
	}
}
