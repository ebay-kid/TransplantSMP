package ml.ikwid.transplantsmp.common.imixins;

import ml.ikwid.transplantsmp.client.TransplantSMPClient;
import ml.ikwid.transplantsmp.common.TransplantType;

public interface ITransplantable {
	int getTransplantedAmount();

	void setTransplantedAmount(int organs);

	void setTransplantedAmountNoUpdate(int organs);

	void transplantOrgan(boolean gain);

	TransplantType getTransplantType();

	void setTransplantType(TransplantType transplantType);

	void setTransplantTypeNoUpdate(TransplantType transplantType);

	void updateTransplants();

	default int getHotbarDraws() {
		return (this.getTransplantType() == TransplantType.ARM_TRANSPLANT ? this.getTransplantedAmount() / 2 : 0) + 9;
	}

	default int xShift() {
		return -(this.getHotbarDraws() * TransplantSMPClient.SLOT_WIDTH / 2);
	}

	default int getHalvedTransplantedAmount() {
		return this.getTransplantedAmount() / 2;
	}
}
