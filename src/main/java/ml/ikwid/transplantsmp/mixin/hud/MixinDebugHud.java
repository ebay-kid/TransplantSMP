package ml.ikwid.transplantsmp.mixin.hud;

import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DebugHud.class)
public abstract class MixinDebugHud {
	@Inject(method = "getLeftText", at = @At("TAIL"))
	private void modifyLeftText(CallbackInfoReturnable<List<String>> cir) {
		List<String> debugText = cir.getReturnValue();
		if(debugText == null) {
			return;
		}
		MinecraftClient client = MinecraftClient.getInstance();
		if(client == null) {
			return;
		}
		if(!client.options.debugEnabled) {
			return;
		}
		ClientPlayerEntity playerEntity = client.player;
		if(playerEntity == null) {
			return;
		}
		ITransplantable transplantable = (ITransplantable) playerEntity;
		debugText.add("transplant smp -- transplant type: " + transplantable.getTransplantType() + ", transplanted amt: " + transplantable.getTransplantedAmount());
		debugText.add("armor -- bars: " + playerEntity.getArmor() + ", toughness: " + playerEntity.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS) + ", kb res: " + playerEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
	}
}