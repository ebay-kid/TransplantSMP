package ml.ikwid.transplantsmp.common.util;

import ml.ikwid.transplantsmp.mixin.player.MixinPlayerInventory;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

/**
 * See {@link MixinPlayerInventory} for the explanation of the inventory indices
 * <p>
 * Index values are 0 based
 */
public class Constants {
	public static final int EXTRA_HOTBAR_START_LOC = 9;
	public static final int EXTRA_ARMOR_START_LOC = EXTRA_HOTBAR_START_LOC + 36; // 9 for the hotbar, 27 for the rest of inv
	public static final int NEW_ARMOR_START_LOC = EXTRA_ARMOR_START_LOC + 4;
	public static final int OFF_HAND = NEW_ARMOR_START_LOC + 4;

	// Other constants for render use
	public static final int OUTER_SLOT_WIDTH = 22;
	public static final int OUTER_SLOT_HEIGHT = 22;
	public static final int HOTBAR_SPACE_IN_INV_SCREEN = 25;

	public static final KeyBinding[] NEW_HOTBAR_KEYS = new KeyBinding[] {
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

    public static final int TRANSPLANT_GIVES = 2;
}
