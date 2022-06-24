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
		this.transplanted = organs;
		this.updateTransplants();
	}

	@Override
	public void transplantOrgan(boolean gain) {
		this.transplanted += (gain ? 2 : -2);
		this.updateTransplants();
	}

	public abstract void updateTransplants();

	@Override
	public TransplantType getTransplantType() {
		return this.transplantType;
	}

	@Override
	public void setTransplantType(TransplantType transplantType) {
		this.transplantType = transplantType;
	}
}
