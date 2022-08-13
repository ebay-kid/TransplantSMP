package ml.ikwid.transplantsmp.common.networking;

import net.minecraft.util.Identifier;

public class NetworkingIDs {
	// S2C
	public static final Identifier UPDATE_ORGAN_COUNT = new Identifier("transplantsmp:organcount");
	public static final Identifier UPDATE_TRANSPLANT_TYPE = new Identifier("transplantsmp:transplanttype");
	public static final Identifier NEEDS_TRANSPLANT = new Identifier("transplantsmp:needstransplant");

	public static final Identifier HANDSHAKE = new Identifier("transplantsmp:handshake");

	// C2S
	public static final Identifier CHOOSE_TRANSPLANT_TYPE = new Identifier("transplantsmp:choosetransplant");
}
