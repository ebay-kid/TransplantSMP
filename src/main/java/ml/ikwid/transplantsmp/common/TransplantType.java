package ml.ikwid.transplantsmp.common;

import java.util.Objects;

public enum TransplantType {
	/**
	 * Hearts
	 */
	HEART_TRANSPLANT("Heart Transplant", """
		Hearts.
		
		Killing gives you hearts.
		Dying loses hearts.
		
		Standard Lifesteal.
		"""),
	/**
	 * Hotbar slots
	 */
	ARM_TRANSPLANT("Arm Transplant", """
		Hotbar slots.
		
		Killing gives you more hotbar slots (to the right).
		Dying removes hotbar slots (right -> left).
		
		Can be hotkeyed.
		"""),
	/**
	 * Armor bars
	 */
	SKIN_TRANSPLANT("Skin Transplant", """
		Armor bars.

		Killing allows you to utilize more armor bars.
		Dying removes armor bars.
		
		To utilize this, you can wear 2 sets of armor.
		The second set is not externally visible.
		The extra bars only work up to the # of armor bars you have.
		(i.e. wearing 2 sets of diamond but only having 15 armor bars,
		only 15 armor bars will be utilized)
		
		Having < 10 bars will reduce the effect of the first set of armor.
		"""),
	/**
	 * Hunger bars
	 */
	STOMACH_TRANSPLANT("Stomach Transplant", """
		Hunger bars.
		
		Killing gives you more hunger bars.
		Dying removes hunger bars.
		
		If you have above 10 hunger bars,
		the vanilla requirements to sprint/heal are kept;
		with 15 hunger bars, you can heal while you have > 9 hunger bars.
		The max saturation = the # of hunger bars filled up in vanilla,
		i.e. 9 hunger bars = 9 bars of saturation.
		This is kept the same, so you can stack up on saturation.
		
		If you have < 10 hunger bars, the requirement to heal scales down.
		""");

	public static final TransplantType[] transplantTypes = values();
	private final String name, description;

	TransplantType(String name, String description) {
		this.name = name;
		this.description = description;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public static TransplantType get(String string) {
		for(TransplantType transplantType : transplantTypes) {
			if(Objects.equals(string, transplantType.toString())) {
				return transplantType;
			}
		}
		return null;
	}
}
