package ml.ikwid.transplantsmp.mixin;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.networking.NetworkingConstants;
import ml.ikwid.transplantsmp.common.networking.NetworkingUtil;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class MixinPlayerManager {
	@Inject(method = "onPlayerConnect", at = @At("TAIL"))
	private void sendScreenIfNeeded(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
		ITransplantable transplantable = (ITransplantable) player;
		if(transplantable.getTransplantType() == null) {
			ServerPlayNetworking.send(player, NetworkingConstants.NEEDS_TRANSPLANT, PacketByteBufs.empty());

			TransplantSMP.LOGGER.info("nothing found, needs transplant");
		} else {
			NetworkingUtil.sendTransplantTypeUpdate(transplantable.getTransplantType().toString(), player);
			transplantable.updateTransplants();
		}
	}
}
