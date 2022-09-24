package ml.ikwid.transplantsmp.mixin.player;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.gamerule.GameruleRegister;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.networking.NetworkingUtil;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerManager.class)
public abstract class MixinPlayerManager {
	@Shadow @Final private MinecraftServer server;

	@Inject(method = "onPlayerConnect", at = @At("TAIL"))
	private void sendScreenIfNeeded(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
		ITransplantable transplantable = (ITransplantable) player;
		if(transplantable.getTransplantType() == null) {
			NetworkingUtil.sendTransplantNeedsChoose(player);
			TransplantSMP.LOGGER.info("nothing found, sent needs transplant");
		} else {
			transplantable.updateTransplants(true, true);
		}

		NetworkingUtil.sendArmBalanceToggleUpdate(List.of(player), this.server.getOverworld().getGameRules().getBoolean(GameruleRegister.SHOULD_BALANCE_ARM));
		NetworkingUtil.sendArmHasteBalanceAmountUpdate(List.of(player), this.server.getOverworld().getGameRules().get(GameruleRegister.ARM_HASTE_BALANCE_AMOUNT).get());
	}
}
