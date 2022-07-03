package ml.ikwid.transplantsmp.common.imixins;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.client.TransplantSMPClient;
import ml.ikwid.transplantsmp.common.TransplantType;

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

	default int getHotbarDraws() {
		int draws = (this.getTransplantType() == TransplantType.ARM_TRANSPLANT ? this.getHalvedTransplantedAmount() : 0) + 9;
		// TransplantSMP.LOGGER.info("draws: " + draws);
		return draws;
	}

	default int xShift() {
		int shift = -((this.getHotbarDraws() - 9) * TransplantSMPClient.SLOT_WIDTH / 2);
		// TransplantSMP.LOGGER.info("xshift: " + shift);
		return shift;
	}

	default int getHalvedTransplantedAmount() {
		return this.getTransplantedAmount() / 2;
	}
}
