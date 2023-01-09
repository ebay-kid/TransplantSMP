package ml.ikwid.transplantsmp.api;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

/**
 * A generic wrapper for a TransplantType.
 * <p>
 * A new TransplantType should extend this class.
 */
public abstract class TransplantType {
    protected final String name;
    protected final String description;
    protected final int defaultChangeByAmount;

    /**
     * Creates a new TransplantType.
     * @param name                  The name of the TransplantType. This should be unique (perhaps follow the MC Identifier structure of mod_id:name) and should NOT include spaces (throws IllegalArgumentException if it does).
     * @param description           The description of the TransplantType. This will be shown to the player.
     * @param defaultChangeByAmount The default amount to increment/decrement the transplanted amount by. This should be the amount of change after a kill/death (or organ item use/acquire).
     */
    public TransplantType(@NotNull String name, @NotNull String description, int defaultChangeByAmount) {
        if(name.contains(" ")) {
            throw new IllegalArgumentException("TransplantType name cannot contain spaces. Offending type: " + name);
        }
        this.name = name;
        this.description = description;
        this.defaultChangeByAmount = defaultChangeByAmount;
    }

    /**
     * Gets the name of this TransplantType. This will also be the name used in the in-game commands.
     * @return The name of this TransplantType, as passed into the constructor.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the description of this TransplantType.
     * @return The description of this TransplantType, as passed into the constructor.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Gets the default amount to change the transplanted amount by.
     * @return The default amount to change the transplanted amount by, as passed into the constructor.
     */
    public int getDefaultChangeByAmount() {
        return this.defaultChangeByAmount;
    }

    /**
     * <pre>
     * Whether the new organ amount OFFSET is a legal amount for this TransplantType.
     * The new amount is scaled to the amount given by {@link #getDefaultChangeByAmount()}.
     *
     * Example: Heart Transplant.
     * Since in vanilla, hearts go from a scale of 0 (not inc.) to 20 (inc) by halves of hearts, the defaultChangeByAmount is 2.
     *
     * If the new offset is 0, then the player will have 10 hearts: (20 + 0) / 2.
     * If the new offset is -16, then the player will have 2 hearts: (20 - 16) / 2 = 2.
     * If the new offset is 16, then the player will have 18 hearts: (20 + 16) / 2 = 18.
     *
     * An amount is legal if:
     *     1. It is greater than or equal to the minimum amount for this TransplantType.
     *         ex: The least amount of hearts you can have is 1 heart. That corresponds to an offset of -18 (halves).
     *         Therefore, an offset of -20 is illegal.
     *
     *     2. It is less than or equal to the maximum amount for this TransplantType. The maximum amount is left to the implementer.
     *         ex: The most hearts allowed by this mod is a total of 20. That corresponds to an offset of 20 (halves).
     *         Therefore, an offset of 22 is illegal, but an offset of 20 is legal.
     * @param newOffset The new organ amount offset
     * @return true if the new amount is legal, false otherwise.
     */
    public abstract boolean canTransplant(int newOffset);

    /**
     * This method should reset the relevant attributes affected by this TransplantType to their default values on the given CLIENT-SIDE player.
     * @param player The client-side player to reset the attributes of. Guaranteed not-null.
     */
    public abstract void onResetTransplantClient(ClientPlayerEntity player);

    /**
     * This method should update the relevant attributes affected by this TransplantType to their new values on the given CLIENT-SIDE player. The new value is guaranteed to be legal according to {@link #canTransplant(int)}.
     * @param player    The client-side player to update the attributes of.
     * @param newAmount The new amount of organs. This is guaranteed to be a legal amount by {@link #canTransplant(int)}, but is NOT guaranteed to be an increment by the amount from {@link #getDefaultChangeByAmount()}.
     */
    public abstract void onUpdateCountClient(ClientPlayerEntity player, int previousAmount, int newAmount);

    /**
     * This method should reset the relevant attributes affected by this TransplantType to their default values on the given SERVER-SIDE player.
     * @param player The server-side player to reset the attributes of.
     */
    public abstract void onResetTransplantServer(ServerPlayerEntity player);

    /**
     * This method should update the relevant attributes affected by this TransplantType to their new values on the given SERVER-SIDE player. The new value is guaranteed to be legal according to {@link #canTransplant(int)}.
     * @param player    The server-side player to update the attributes of.
     * @param newAmount The new amount of organs. This is guaranteed to be a legal amount by {@link #canTransplant(int)}, but is NOT guaranteed to be an increment by the amount from {@link #getDefaultChangeByAmount()}.
     */
    public abstract void onUpdateCountServer(ServerPlayerEntity player, int previousAmount, int newAmount);

    /**
     * If the player respawns after dying and certain data needs to be set properly (ex. heart players should be respawned with the correct num of hearts), this method should be implemented.
     * <p>
     * This method is called on the server-side, and should NOT handle decrementing the amount of organs after a death.
     * @param player The player that respawned.
     * @param amount The amount of organs the player has.
     */
    public abstract void onPlayerRespawn(ServerPlayerEntity player, int amount);

    /**
     * Calls {@link #getName()}.
     * @return The name of this TransplantType, as passed into the constructor.
     */
    @Override
    public String toString() {
        return this.getName();
    }
}
