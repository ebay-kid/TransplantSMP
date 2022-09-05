package ml.ikwid.transplantsmp.client;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.networking.NetworkingHandlerClient;
import ml.ikwid.transplantsmp.common.networking.NetworkingIDs;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Environment(EnvType.CLIENT)
public class TransplantSMPClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		TransplantSMP.LOGGER.info("hello medical patient, welcome to the deadliest minecraft smp aka a hospital");

		ClientPlayNetworking.registerGlobalReceiver(NetworkingIDs.UPDATE_TRANSPLANT_COUNT_S2C, NetworkingHandlerClient::updateOrganCount);
		ClientPlayNetworking.registerGlobalReceiver(NetworkingIDs.UPDATE_TRANSPLANT_TYPE_S2C, NetworkingHandlerClient::updateTransplantType);
		ClientPlayNetworking.registerGlobalReceiver(NetworkingIDs.NEEDS_TRANSPLANT_S2C, NetworkingHandlerClient::setTransplantNeededScreen);

		ClientLoginNetworking.registerGlobalReceiver(NetworkingIDs.HANDSHAKE_S2C, NetworkingHandlerClient::handleHandshakeClientSide);
	}
}
