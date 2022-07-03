package ml.ikwid.transplantsmp.common.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class ItemRegister {
	public static final OrganItem ORGAN_ITEM = new OrganItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(16).rarity(Rarity.EPIC));
	public static void register() {
		Registry.register(Registry.ITEM, new Identifier("transplantsmp", "organ_item"), ORGAN_ITEM);
	}
}
