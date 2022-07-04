package ml.ikwid.transplantsmp.common.imixins;

import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.util.Constants;

public interface ITransplantable {
	int TRANSPLANT_GIVES = 2;
	default boolean illegalTransplantAmount() {
		int transplants = this.getTransplantedAmount();

		if(this.getTransplantType() == TransplantType.ARM_TRANSPLANT) {
			return transplants >= 10 || transplants <= -10;
		}
		return transplants >= 11 || transplants <= -11;
	}

	int getTransplantedAmount();

	void setTransplantedAmount(int organs);

	void setTransplantedAmountNoUpdate(int organs);

	default void transplantOrgan(boolean gain) {
		this.setTransplantedAmount(this.getTransplantedAmount() + (gain ? TRANSPLANT_GIVES : -TRANSPLANT_GIVES));
	}

	TransplantType getTransplantType();

	void setTransplantType(TransplantType transplantType);

	void setTransplantTypeNoUpdate(TransplantType transplantType);

	void updateTransplants();

	/**
	 * @return the number of slots required to be drawn for the hotbar.
	 */
	default int getHotbarDraws() {
		return (this.getTransplantType() == TransplantType.ARM_TRANSPLANT ? this.getHalvedTransplantedAmount() : 0) + 9;
	}

	/**
	 * For Arm Transplant users, their increase (or decrease, for that matter) of hotbar slots should
	 * still keep their hotbar centered.
	 *
	 * @return the shift required for the hotbar.
	 */
	default int xShift() {
		return -((this.getHotbarDraws() - 9) * Constants.OUTER_SLOT_WIDTH / 2);
	}

	default int getHalvedTransplantedAmount() {
		return this.getTransplantedAmount() / 2;
	}
}
