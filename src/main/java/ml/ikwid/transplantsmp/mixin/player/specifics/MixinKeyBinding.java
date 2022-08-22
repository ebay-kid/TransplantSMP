package ml.ikwid.transplantsmp.mixin.player.specifics;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.Objects;

@Mixin(KeyBinding.class)
public class MixinKeyBinding {
	@Shadow @Final private String category;

	@Shadow @Final private String translationKey;

	@Shadow @Final private static Map<String, Integer> CATEGORY_ORDER_MAP;

	/**
	 * @author 6Times
	 * @reason Number sorting bad
	 */
	@Overwrite
	public int compareTo(KeyBinding keyBinding) {
		if (this.category.equals(keyBinding.getCategory())) {
			if(Objects.equals(this.category, KeyBinding.INVENTORY_CATEGORY)) {
				if (this.translationKey.startsWith("hotbar", 4) && keyBinding.getTranslationKey().startsWith("hotbar", 4)) {
					int self = Integer.parseInt(this.translationKey.substring(11));
					int that = Integer.parseInt(keyBinding.getTranslationKey().substring(11));
					return Integer.compare(self, that);
				}
			}

			return I18n.translate(this.translationKey).compareTo(I18n.translate(keyBinding.getTranslationKey()));
		}
		return CATEGORY_ORDER_MAP.get(this.category).compareTo(CATEGORY_ORDER_MAP.get(keyBinding.getCategory()));
	}
}
