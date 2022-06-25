package ml.ikwid.transplantsmp;

import io.netty.buffer.ByteBuf;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.command.Register;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.networking.NetworkingConstants;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;

public class TransplantSMP implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("transplantsmp");
	public static final int NEW_HOTBAR_START_LOC = 44;

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> Register.register(dispatcher));

		ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.CHOOSE_TRANSPLANT_TYPE, ((server, player, handler, buf, responseSender) -> {
			String chosenType = buf.readString();
			server.execute(() -> {
				ITransplantable transplantable = (ITransplantable) player;
				if(transplantable.getTransplantType() == null) {
					TransplantType transplantType = TransplantType.get(chosenType);

					transplantable.setTransplantType(transplantType);
					transplantable.setTransplantedAmount(0);

					PacketByteBuf buf3 = PacketByteBufs.create();
					buf3.writeString(chosenType);
					ServerPlayNetworking.send(player, NetworkingConstants.UPDATE_TRANSPLANT_TYPE, buf3);
				} else {
					LOGGER.info("someone's being sussy (" + player.getName().getString() + ")!");
				}
			});
		}));
		LOGGER.info("time for medical transplants");
	}
}
