package ml.ikwid.transplantsmp.mixin.player.specifics;

import ml.ikwid.transplantsmp.client.TransplantSMPClient;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
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
		return ((ITransplantable) (this.player)).getHotbarDraws();
	}

	@Redirect(method = "handleInputEvents", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerInventory;selectedSlot:I", opcode = Opcodes.PUTFIELD))
	private void allowSecondaryHotkeyMethod(PlayerInventory instance, int value) {
		if(value < 9 && TransplantSMPClient.hotbarSwapKeybind.isPressed()) {
			int ret = value + 9;
			if(ret < ((ITransplantable) (this.player)).getHotbarDraws()) {
				instance.selectedSlot = ret;
				return;
			}
		}
		instance.selectedSlot = value;
	}
}
