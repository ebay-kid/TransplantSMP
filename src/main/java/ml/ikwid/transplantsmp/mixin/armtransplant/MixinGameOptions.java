package ml.ikwid.transplantsmp.mixin.armtransplant;

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

	@Inject(method = "<init>", at = @At("TAIL"))
	private void immediateOverrideKeybind(MinecraftClient client, File optionsFile, CallbackInfo ci) {
		KeyBinding[] newHotbarKeys = new KeyBinding[18];
		System.arraycopy(this.hotbarKeys, 0, newHotbarKeys, 0, this.hotbarKeys.length);
		this.hotbarKeys = ArrayUtils.addAll(newHotbarKeys, Constants.NEW_HOTBAR_KEYS);

		KeyBinding[] newAllKeys = new KeyBinding[this.allKeys.length + 9];
		System.arraycopy(this.allKeys, 0, newAllKeys, 0, this.allKeys.length);
		this.allKeys = ArrayUtils.addAll(newAllKeys, Constants.NEW_HOTBAR_KEYS);
	}
}
