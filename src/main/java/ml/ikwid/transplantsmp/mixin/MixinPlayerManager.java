package ml.ikwid.transplantsmp.mixin;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.networking.NetworkingUtil;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class MixinPlayerManager {
	@Inject(method = "onPlayerConnect", at = @At("TAIL"))
	private void sendScreenIfNeeded(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
		ITransplantable transplantable = (ITransplantable) player;
		if(transplantable.getTransplantType() == null) {
			NetworkingUtil.sendTransplantNeedsChoose(player);

			TransplantSMP.LOGGER.info("nothing found, sent needs transplant");
		} else {
			transplantable.updateTransplants(true, true);
		}
	}
}
