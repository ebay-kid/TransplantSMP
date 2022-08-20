package ml.ikwid.transplantsmp.client;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.networking.NetworkingHandlerClient;
import ml.ikwid.transplantsmp.common.networking.NetworkingIDs;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class TransplantSMPClient implements ClientModInitializer {
	public static TransplantType transplantType;
	public static int transplants = 0;

	@Override
	public void onInitializeClient() {
		TransplantSMP.LOGGER.info("hello medical patient, welcome to the deadliest minecraft smp aka a hospital");

		ClientPlayNetworking.registerGlobalReceiver(NetworkingIDs.UPDATE_ORGAN_COUNT, NetworkingHandlerClient::updateOrganCount);
		ClientPlayNetworking.registerGlobalReceiver(NetworkingIDs.UPDATE_TRANSPLANT_TYPE, NetworkingHandlerClient::updateTransplantType);
		ClientPlayNetworking.registerGlobalReceiver(NetworkingIDs.NEEDS_TRANSPLANT, NetworkingHandlerClient::setTransplantNeededScreen);

		ClientLoginNetworking.registerGlobalReceiver(NetworkingIDs.HANDSHAKE, NetworkingHandlerClient::handleHandshakeClientSide);

		ClientTickEvents.START_CLIENT_TICK.register((listener) -> {
			if(MinecraftClient.getInstance().player == null) {
				TransplantSMP.LOGGER.info("null");
			}
			if(listener.player == null) {
				TransplantSMP.LOGGER.info("heh");
			}
		});
	}
}
