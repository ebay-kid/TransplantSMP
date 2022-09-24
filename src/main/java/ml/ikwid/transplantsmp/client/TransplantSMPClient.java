package ml.ikwid.transplantsmp.client;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.networking.NetworkingHandlerClient;
import ml.ikwid.transplantsmp.common.networking.NetworkingIDs;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class TransplantSMPClient implements ClientModInitializer {
	public static int transplanted = 0;
	public static TransplantType transplantType;
	public static double armHasteBalanceAmount = 1;
	public static boolean balanceArm = false;

	public static KeyBinding hotbarSwapKeybind;

	@Override
	public void onInitializeClient() {
		TransplantSMP.LOGGER.info("hello medical patient, welcome to the deadliest minecraft smp aka a hospital");

		ClientPlayNetworking.registerGlobalReceiver(NetworkingIDs.UPDATE_TRANSPLANT_COUNT_S2C, NetworkingHandlerClient::updateOrganCount);
		ClientPlayNetworking.registerGlobalReceiver(NetworkingIDs.UPDATE_TRANSPLANT_TYPE_S2C, NetworkingHandlerClient::updateTransplantType);
		ClientPlayNetworking.registerGlobalReceiver(NetworkingIDs.NEEDS_TRANSPLANT_S2C, NetworkingHandlerClient::setTransplantNeededScreen);
		ClientPlayNetworking.registerGlobalReceiver(NetworkingIDs.ARM_HASTE_BALANCE_AMOUNT_S2C, NetworkingHandlerClient::updateArmBalanceAmount);
		ClientPlayNetworking.registerGlobalReceiver(NetworkingIDs.BALANCE_ARM_TOGGLE_S2C, NetworkingHandlerClient::updateArmBalanceToggle);

		ClientLoginNetworking.registerGlobalReceiver(NetworkingIDs.HANDSHAKE_S2C, NetworkingHandlerClient::handleHandshakeClientSide);

		hotbarSwapKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.transplantsmp.hotbar_swap", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT, KeyBinding.INVENTORY_CATEGORY));
	}
}
