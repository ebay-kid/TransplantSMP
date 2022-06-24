package ml.ikwid.transplantsmp;

import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.command.Register;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.networking.NetworkingConstants;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;

public class TransplantSMP implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("transplantsmp");
	public static final int NEW_HOTBAR_START_LOC = 44;

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> Register.register(dispatcher)));
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.getPlayer();
			ITransplantable transplantable = (ITransplantable) player;

			if(transplantable.getTransplantType() == null) {
				ServerPlayNetworking.send(player, NetworkingConstants.NEEDS_TRANSPLANT, PacketByteBufs.empty());
			}
		});

		ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.CHOOSE_TRANSPLANT_TYPE, ((server, player, handler, buf, responseSender) -> server.execute(() -> {
			ITransplantable transplantable = (ITransplantable) player;
			if(transplantable.getTransplantType() == null) {
				transplantable.setTransplantType(TransplantType.valueOf(buf.readString()));
			}
		})));
		LOGGER.info("time for medical transplants");
	}
}
