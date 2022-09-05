package ml.ikwid.transplantsmp.common.networking;

import net.minecraft.util.Identifier;

public class NetworkingIDs {
	// S2C
	public static final Identifier UPDATE_TRANSPLANT_COUNT_S2C = new Identifier("transplantsmp:organcount");
	public static final Identifier UPDATE_TRANSPLANT_TYPE_S2C = new Identifier("transplantsmp:transplanttype");
	public static final Identifier NEEDS_TRANSPLANT_S2C = new Identifier("transplantsmp:needstransplant");

	public static final Identifier HANDSHAKE_S2C = new Identifier("transplantsmp:handshake");

	// C2S
	public static final Identifier CHOOSE_TRANSPLANT_TYPE_C2S = new Identifier("transplantsmp:choosetransplant");
}
