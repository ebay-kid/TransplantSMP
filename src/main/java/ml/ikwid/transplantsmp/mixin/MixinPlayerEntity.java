package ml.ikwid.transplantsmp.mixin;

import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Unique
@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity implements ITransplantable {
	@Unique
	protected TransplantType transplantType;
	@Unique
	protected int transplanted = 0;

	@Override
	public int getTransplantedAmount() {
		return this.transplanted;
	}

	@Override
	public void setTransplantedAmount(int organs) {
		int prev = this.getTransplantedAmount();
		this.transplanted = organs;
		if(illegalTransplantAmount()) {
			this.transplanted = prev;
			return;
		}
		this.updateTransplants();
	}

	@Override
	public void setTransplantedAmountNoUpdate(int organs) {
		int prev = this.getTransplantedAmount();
		this.transplanted = organs;
		if(illegalTransplantAmount()) {
			this.transplanted = prev;
		}
	}

	public abstract void updateTransplants();

	@Override
	public TransplantType getTransplantType() {
		return this.transplantType;
	}

	@Override
	public void setTransplantType(TransplantType transplantType) {
		this.transplantType = transplantType;
		this.updateTransplants();
	}

	@Override
	public void setTransplantTypeNoUpdate(TransplantType transplantType) {
		this.transplantType = transplantType;
	}
}
