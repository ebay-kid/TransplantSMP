package ml.ikwid.transplantsmp.common;

import java.util.Objects;

public enum TransplantType {
	HEART_TRANSPLANT("Heart Transplant", """
		Hearts.
		
		Killing gives you hearts.
		Dying loses hearts.
		
		Standard Lifesteal.
		"""),
	ARM_TRANSPLANT("Arm Transplant", """
		Hotbar slots.
		
		Killing gives you more hotbar slots (to the right).
		Dying removes hotbar slots in the same order as they were added (right -> left).
		
		Can be hotkeyed.
		"""),
	SKIN_TRANSPLANT("Skin Transplant", """
		Armor bars.

		Killing allows you to utilize more armor bars.
		Dying removes armor bars.
		
		To utiliize this, you are allowed to wear 2 sets of armor.
		The second set must be unenchanted (and is not externally visible).
		The second set will only have the effect of the number of bars you already have;
		(i.e. wearing 2 sets of diamond but only having 15 armor bars means only 15 armor bars will be applied).
		
		Having less than 10 bars by dying will reduce the effect of the first set of armor.
		"""),
	STOMACH_TRANSPLANT("Stomach Transplant", """
		Hunger bars.
		
		Killing gives you more hunger bars.
		Dying removes hunger bars.
		
		If you have above 10 hunger bars, the vanilla requirements to sprint/heal are kept;
		so having 15 hunger bars will allow you to heal as long as you have > 9 hunger bars.
		
		If you have less than 10 hunger bars, the requirement to heal will be scaled down.
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
