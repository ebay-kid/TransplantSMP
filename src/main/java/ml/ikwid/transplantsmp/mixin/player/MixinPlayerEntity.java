package ml.ikwid.transplantsmp.mixin.player;

import ml.ikwid.transplantsmp.TransplantSMP;
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

	private final PlayerEntity self = (PlayerEntity)(Object) this;

	@Override
	public int getTransplantedAmount() {
		return this.transplanted;
	}

	@Override
	public void setTransplantedAmount(int organs, boolean updateGeneral, boolean updateAll) {
		int prev = this.getTransplantedAmount();
		this.transplanted = organs;
		if(illegalTransplantAmount()) {
			this.transplanted = prev;
			TransplantSMP.LOGGER.info("illegal amount of " + organs);
			return;
		}
		this.updateTransplants(updateGeneral, updateAll);
	}

	/**
	 * Re-shifts the hotbar slots to account for the new transplant counts
	 */
	@Override
	public void updateTransplants(boolean updateCount, boolean updateType) {
	}

	@Override
	public TransplantType getTransplantType() {
		return this.transplantType;
	}

	@Override
	public void setTransplantType(TransplantType transplantType, boolean updateGeneral) {
		this.transplantType = transplantType;
		this.updateTransplants(updateGeneral, updateGeneral);
	}
}
