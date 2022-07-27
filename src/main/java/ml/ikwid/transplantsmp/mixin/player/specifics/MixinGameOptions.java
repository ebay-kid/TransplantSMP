package ml.ikwid.transplantsmp.mixin.player.specifics;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.util.Constants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(GameOptions.class)
public abstract class MixinGameOptions {
	@Mutable
	@Shadow @Final public KeyBinding[] hotbarKeys;

	@Mutable
	@Shadow @Final public KeyBinding[] allKeys;

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;load()V"))
	private void immediateOverrideKeybind(MinecraftClient client, File optionsFile, CallbackInfo ci) {
		// TransplantSMP.LOGGER.info("Hotbar keys overridden, OG: " + this.hotbarKeys.length + ", All Keys: " + this.allKeys.length);

		this.hotbarKeys = ArrayUtils.addAll(this.hotbarKeys, Constants.NEW_HOTBAR_KEYS);
		this.allKeys = ArrayUtils.addAll(this.allKeys, Constants.NEW_HOTBAR_KEYS);

		// TransplantSMP.LOGGER.info("Hotbar Keys: " + this.hotbarKeys.length + ", All Keys: " + this.allKeys.length);
	}
}
