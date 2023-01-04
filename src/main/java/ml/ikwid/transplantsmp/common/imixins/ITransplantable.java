package ml.ikwid.transplantsmp.common.imixins;

import ml.ikwid.transplantsmp.api.TransplantType;

public interface ITransplantable {
	/*
	default boolean illegalTransplantAmount() {
		int transplants = this.getTransplantedAmount();

		if(this.getTransplantType() == TransplantType.ARM_TRANSPLANT) {
			return transplants > 18 || transplants <= -18;
		}
		return transplants > 20 || transplants <= -20;
	}
	 */

	int getRawTransplantedAmount();
	int getTransplantedAmount();

	boolean setTransplantedAmount(int organs, boolean updateCount, boolean updateType);

	default void transplantOrgan(boolean gain) {
		this.setTransplantedAmount(this.getTransplantedAmount() + (gain ? 1 : -1), true, false);
	}

	TransplantType getTransplantType();

	void setTransplantType(TransplantType transplantType, boolean updateType);

	void updateTransplants(boolean updateCount, boolean updateType, TransplantType prevType, int prevAmt, int newAmt);

	/*
	 * @return the number of slots required to be drawn for the hotbar.
	 */
	/*
	default int getHotbarDraws() {
		return (this.getTransplantType() == TransplantType.ARM_TRANSPLANT ? this.getHalvedTransplantedAmount() : 0) + 9;
	}
	 */

	/*
	 * For Arm Transplant users, their increase (or decrease, for that matter) of hotbar slots should
	 * still keep their hotbar centered.
	 *
	 * @return the shift required for the hotbar. This should be ADDED to the vanilla value.
	 */
	/*
	default int xShift() {
		return -((this.getHotbarDraws() - 9) * Constants.OUTER_SLOT_WIDTH / 2);
	}
	 */

	/*
	default int getHalvedTransplantedAmount() {
		return this.getTransplantedAmount() / 2;
	}
	 */

	void setIsSettingTransplant(boolean isSettingTransplant);

	boolean getIsSettingTransplant();
}
