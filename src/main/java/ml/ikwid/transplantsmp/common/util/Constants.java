package ml.ikwid.transplantsmp.common.util;

import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class Constants {
	// See mixin/armtransplant/MixinPlayerInventory.java for the explanation
	// Index value (0 based)
	public static final int NEW_HOTBAR_START_LOC = 9;
	public static final int NEW_ARMOR_START_LOC = 36 + NEW_HOTBAR_START_LOC;
	public static final int SECOND_ARMOR_START_LOC = NEW_ARMOR_START_LOC + 1;
	public static final int OFF_HAND = SECOND_ARMOR_START_LOC + 4;

	// Other constants for render use
	public static final int OUTER_SLOT_WIDTH = 21;
	public static final int INNER_SLOT_WIDTH = 18;
	public static final int OUTER_SLOT_HEIGHT = 22;
	public static final int HOTBAR_SPACE_IN_INV_SCREEN = 25;



	public static final KeyBinding[] NEW_HOTBAR_KEYS = new KeyBinding[] {
		new KeyBinding("key.hotbar.1", GLFW.GLFW_KEY_1, KeyBinding.INVENTORY_CATEGORY),
		new KeyBinding("key.hotbar.2", GLFW.GLFW_KEY_2, KeyBinding.INVENTORY_CATEGORY),
		new KeyBinding("key.hotbar.3", GLFW.GLFW_KEY_3, KeyBinding.INVENTORY_CATEGORY),
		new KeyBinding("key.hotbar.4", GLFW.GLFW_KEY_4, KeyBinding.INVENTORY_CATEGORY),
		new KeyBinding("key.hotbar.5", GLFW.GLFW_KEY_5, KeyBinding.INVENTORY_CATEGORY),
		new KeyBinding("key.hotbar.6", GLFW.GLFW_KEY_6, KeyBinding.INVENTORY_CATEGORY),
		new KeyBinding("key.hotbar.7", GLFW.GLFW_KEY_7, KeyBinding.INVENTORY_CATEGORY),
		new KeyBinding("key.hotbar.8", GLFW.GLFW_KEY_8, KeyBinding.INVENTORY_CATEGORY),
		new KeyBinding("key.hotbar.9", GLFW.GLFW_KEY_9, KeyBinding.INVENTORY_CATEGORY),
		new KeyBinding("key.hotbar.10", GLFW.GLFW_KEY_BACKSLASH, KeyBinding.INVENTORY_CATEGORY),
		new KeyBinding("key.hotbar.11", GLFW.GLFW_KEY_ENTER, KeyBinding.INVENTORY_CATEGORY),
		new KeyBinding("key.hotbar.12", GLFW.GLFW_KEY_RIGHT_SHIFT, KeyBinding.INVENTORY_CATEGORY),
		new KeyBinding("key.hotbar.13", GLFW.GLFW_KEY_RIGHT_CONTROL, KeyBinding.INVENTORY_CATEGORY),
		new KeyBinding("key.hotbar.14", GLFW.GLFW_KEY_RIGHT_BRACKET, KeyBinding.INVENTORY_CATEGORY),
		new KeyBinding("key.hotbar.15", GLFW.GLFW_KEY_LEFT_BRACKET, KeyBinding.INVENTORY_CATEGORY),
		new KeyBinding("key.hotbar.16", GLFW.GLFW_KEY_APOSTROPHE, KeyBinding.INVENTORY_CATEGORY),
		new KeyBinding("key.hotbar.17", GLFW.GLFW_KEY_PERIOD, KeyBinding.INVENTORY_CATEGORY),
		new KeyBinding("key.hotbar.18", GLFW.GLFW_KEY_COMMA, KeyBinding.INVENTORY_CATEGORY)
	};
}
