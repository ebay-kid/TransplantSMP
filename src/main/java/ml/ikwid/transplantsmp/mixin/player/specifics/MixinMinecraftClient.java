package ml.ikwid.transplantsmp.mixin.player.specifics;

import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {
	@Shadow public ClientPlayerEntity player;

	@ModifyConstant(method = "handleInputEvents", constant = @Constant(intValue = 9, ordinal = 0))
	private int increaseCheckedHotkeys(int constant) {
		return ((ITransplantable) (this.player)).getHotbarDraws();
	}
}
