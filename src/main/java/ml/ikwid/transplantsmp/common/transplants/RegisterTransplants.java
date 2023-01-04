package ml.ikwid.transplantsmp.common.transplants;

import ml.ikwid.transplantsmp.api.TransplantTypes;

public class RegisterTransplants {
    public static final HeartTransplant HEART_TRANSPLANT = new HeartTransplant("heart_transplant", """
		Hearts.
		
		Killing gives you hearts.
		Dying loses hearts.
		
		Standard Lifesteal.
	""", 2);

	public static final ArmTransplant ARM_TRANSPLANT = new ArmTransplant("arm_transplant", """
		Armor bars.

		Killing allows you to utilize more armor bars.
		Dying removes armor bars.
		
		To utilize this, you can wear 2 sets of armor.
		The second set is not externally visible.
		The extra bars only work up to the # of armor bars you have.
		(i.e. wearing 2 sets of diamond but only having 15 armor bars,
		only 15 armor bars will be utilized)
		
		Having < 10 bars will reduce the effect of the first set of armor.
	""", 1);

	public static final SkinTransplant SKIN_TRANSPLANT = new SkinTransplant("skin_transplant", """
		Armor bars.

		Killing allows you to utilize more armor bars.
		Dying removes armor bars.
		
		To utilize this, you can wear 2 sets of armor.
		The second set is not externally visible.
		The extra bars only work up to the # of armor bars you have.
		(i.e. wearing 2 sets of diamond but only having 15 armor bars,
		only 15 armor bars will be utilized)
		
		Having < 10 bars will reduce the effect of the first set of armor.
	""", 2);

	public static final StomachTransplant STOMACH_TRANSPLANT = new StomachTransplant("stomach_transplant", """
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
	""", 2);

    public static void registerAll() {
        TransplantTypes.register(HEART_TRANSPLANT);
		TransplantTypes.register(ARM_TRANSPLANT);
		TransplantTypes.register(SKIN_TRANSPLANT);
		TransplantTypes.register(STOMACH_TRANSPLANT);
    }
}
