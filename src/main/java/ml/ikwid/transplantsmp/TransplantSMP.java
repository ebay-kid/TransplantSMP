package ml.ikwid.transplantsmp;

import ml.ikwid.transplantsmp.common.command.CommandRegister;
import ml.ikwid.transplantsmp.common.item.ItemRegister;
import ml.ikwid.transplantsmp.common.networking.NetworkingIDs;
import ml.ikwid.transplantsmp.common.networking.NetworkingHandlerServer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TransplantSMP implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("transplantsmp");

	public static String VERSION;
	public static int[] SEMVER;

	@Override
	public void onInitialize() {
		FabricLoader.getInstance().getModContainer("transplantsmp").ifPresent(modContainer -> {
			VERSION = modContainer.getMetadata().getVersion().getFriendlyString();
			String[] versionSplit = VERSION.split("\\.");
			SEMVER = new int[versionSplit.length];

			for(int i = 0; i < SEMVER.length; i++) {
				SEMVER[i] = Integer.parseInt(versionSplit[i]);
			}
		});

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> CommandRegister.register(dispatcher));
		ItemRegister.register();

		ServerPlayNetworking.registerGlobalReceiver(NetworkingIDs.CHOOSE_TRANSPLANT_TYPE_C2S, NetworkingHandlerServer::chosenTransplantType);

		ServerLoginConnectionEvents.QUERY_START.register(NetworkingHandlerServer::handshake);
		ServerLoginNetworking.registerGlobalReceiver(NetworkingIDs.HANDSHAKE_S2C, NetworkingHandlerServer::handleHandshakeServerSide);

		LOGGER.info("time for medical transplants");
	}
}
