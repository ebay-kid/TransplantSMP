package ml.ikwid.transplantsmp.mixin;

import ml.ikwid.transplantsmp.common.item.FakeItem;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class MixinItemEntity {
	private final ItemEntity self = (ItemEntity)(Object) this;

	@Inject(method = "tick", at = @At("HEAD"))
	private void killFakeItem(CallbackInfo ci) {
		if(self.getStack().getItem() instanceof FakeItem) {
			self.kill();
		}
	}
}
