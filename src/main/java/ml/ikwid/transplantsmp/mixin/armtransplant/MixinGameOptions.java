package ml.ikwid.transplantsmp.mixin.armtransplant;

import ml.ikwid.transplantsmp.common.util.Constants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(GameOptions.class)
public class MixinGameOptions {
	@Mutable
	@Shadow @Final public KeyBinding[] hotbarKeys;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void immediateOverrideKeybind(MinecraftClient client, File optionsFile, CallbackInfo ci) {
		this.hotbarKeys = Constants.NEW_HOTBAR_KEYS;
	}

	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lorg/apache/commons/lang3/ArrayUtils;addAll([Ljava/lang/Object;[Ljava/lang/Object;)[Ljava/lang/Object;"), index = 1)
	private <T> T[] addMoreKeybinds(T[] array1) {
		return (T[]) Constants.NEW_HOTBAR_KEYS;
	}
}
