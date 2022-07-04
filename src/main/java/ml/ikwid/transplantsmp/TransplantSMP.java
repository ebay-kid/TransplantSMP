package ml.ikwid.transplantsmp;

import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.command.CommandRegister;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import ml.ikwid.transplantsmp.common.item.ItemRegister;
import ml.ikwid.transplantsmp.common.networking.NetworkingConstants;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TransplantSMP implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("transplantsmp");

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> CommandRegister.register(dispatcher));
		ItemRegister.register();

		ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.CHOOSE_TRANSPLANT_TYPE, ((server, player, handler, buf, responseSender) -> {
			String chosenType = buf.readString(); // read before main thread always remember this
			server.execute(() -> {
				ITransplantable transplantable = (ITransplantable) player;
				if(transplantable.getTransplantType() == null) {
					TransplantType transplantType = TransplantType.get(chosenType);

					transplantable.setTransplantTypeNoUpdate(transplantType);
					transplantable.setTransplantedAmount(0); // don't update twice.

					PacketByteBuf buf3 = PacketByteBufs.create();
					buf3.writeString(chosenType);
					ServerPlayNetworking.send(player, NetworkingConstants.UPDATE_TRANSPLANT_TYPE, buf3);
					LOGGER.info("time to choose your transplant, " + player.getName().getString());
				} else {
					LOGGER.info("someone's being sussy (" + player.getName().getString() + ")!");
				}
			});
		}));


		LOGGER.info("time for medical transplants");
	}
}
