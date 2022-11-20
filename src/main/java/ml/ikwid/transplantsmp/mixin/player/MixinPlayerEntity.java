package ml.ikwid.transplantsmp.mixin.player;

import ml.ikwid.transplantsmp.TransplantSMP;
import ml.ikwid.transplantsmp.client.TransplantSMPClient;
import ml.ikwid.transplantsmp.common.TransplantType;
import ml.ikwid.transplantsmp.common.gamerule.GameruleRegister;
import ml.ikwid.transplantsmp.common.imixins.IStomachTransplanted;
import ml.ikwid.transplantsmp.common.imixins.ITransplantable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Unique
@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity implements ITransplantable {
	@Unique
	protected TransplantType transplantType;
	@Unique
	protected int transplanted = 0;

	@Unique
	private final PlayerEntity self = (PlayerEntity)(Object) this;

	@Override
	public int getTransplantedAmount() {
		return this.transplanted;
	}

	@Override
	public void setTransplantedAmount(int organs, boolean updateCount, boolean updateType) {
		int prev = this.getTransplantedAmount();
		this.transplanted = organs;
		if(illegalTransplantAmount()) {
			this.transplanted = prev;
			TransplantSMP.LOGGER.info("illegal amount of " + organs);
			return;
		}
		this.updateTransplants(updateCount, updateType);
	}

	@Override
	public TransplantType getTransplantType() {
		return this.transplantType;
	}

	@Override
	public void setTransplantType(TransplantType transplantType, boolean updateType) {
		this.transplantType = transplantType;
		this.updateTransplants(updateType, updateType);
	}

	@Override
	public void updateTransplants(boolean updateCount, boolean updateType) {
		if (updateType) {
			EntityAttributeInstance attribute = this.self.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
			if (attribute == null) {
				TransplantSMP.LOGGER.warn("attribute shouldn't be null uhhh you're kinda screwed");
			} else {
				attribute.setBaseValue(20);
			}

			IStomachTransplanted hungerMgr = (IStomachTransplanted) (this.self.getHungerManager());
			hungerMgr.setMaxFoodLevel(20);
		}

		if (updateCount) {
			switch (this.getTransplantType()) {
				case HEART_TRANSPLANT:
					EntityAttributeInstance attribute = this.self.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
					if (attribute == null) {
						TransplantSMP.LOGGER.warn("attribute shouldn't be null uhhh you're kinda screwed");
						return;
					}
					attribute.setBaseValue(20 + this.getTransplantedAmount());
					break;
				case ARM_TRANSPLANT:

				case SKIN_TRANSPLANT:

					break;
				case STOMACH_TRANSPLANT:
					IStomachTransplanted hungerMgr = (IStomachTransplanted) (this.self.getHungerManager());
					hungerMgr.setMaxFoodLevel(20 + this.getTransplantedAmount());
					break;
			}
		}
	}

	@Environment(EnvType.CLIENT)
	@Redirect(method = "getAttackCooldownProgressPerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D"))
	private double balanceArmClient(PlayerEntity instance, EntityAttribute entityAttribute) {
		double attrVal = instance.getAttributeValue(entityAttribute);
		if(TransplantSMPClient.balanceArm && this.getTransplantType() == TransplantType.ARM_TRANSPLANT) {
			attrVal += ((1.0d + this.getTransplantedAmount()) * TransplantSMPClient.armHasteBalanceAmount / 20d);
		}
		return attrVal;
	}

	@Environment(EnvType.SERVER)
	@Redirect(method = "getAttackCooldownProgressPerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D"))
	private double balanceArmServer(PlayerEntity instance, EntityAttribute entityAttribute) {
		double attrVal = instance.getAttributeValue(entityAttribute);
		if(Objects.requireNonNull(self.getServer()).getOverworld().getGameRules().getBoolean(GameruleRegister.SHOULD_BALANCE_ARM) && this.getTransplantType() == TransplantType.ARM_TRANSPLANT) {
			attrVal += ((1.0d + this.getTransplantedAmount()) * self.getServer().getOverworld().getGameRules().get(GameruleRegister.ARM_HASTE_BALANCE_AMOUNT).get() / 20d);
		}
		return attrVal;
	}
}
