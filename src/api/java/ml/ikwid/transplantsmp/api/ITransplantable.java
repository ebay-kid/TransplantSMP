package ml.ikwid.transplantsmp.api;

/**
 * This interface is injected into {@link net.minecraft.entity.player.PlayerEntity} (and Server/Client versions) with implementations of the methods below.
 */
public interface ITransplantable {
	/**
	 * Gets the transplanted amount in internal units.
	 * @return The transplanted amount in internal units.
	 */
	int getRawTransplantedAmount();

	/**
	 * Gets the transplanted amount, scaled to {@link TransplantType#getDefaultChangeByAmount()}.
	 * @return The transplanted amount, scaled to {@link TransplantType#getDefaultChangeByAmount()}.
	 */
	int getTransplantedAmount();

	/**
	 * Wrapper that sets the number of transplanted organs using internal units. This method will return true if:
	 * <ul>
	 *     The amount passed in is legal for the current {@link TransplantType}.
	 * and false otherwise. If false, the change is not executed. This method should not be called when {@link ITransplantable#getTransplantType()} is null.
	 *
	 * @param organs      The number of organs (internal units) to set to.
	 * @param updateCount Whether to send a count update.
	 * @return True if the new amount is legal and therefore, call was successful, false otherwise (and the call was not executed).
	 */
	boolean setTransplantedAmount(int organs, boolean updateCount);

	/**
	 * Returns the {@link TransplantType} the player is currently using.
	 * @return The {@link TransplantType} the player is currently using. Null if it has not been set yet.
	 */
	TransplantType getTransplantType();

	/**
	 * Sets the {@link TransplantType} the player is currently using.
	 * @param transplantType The {@link TransplantType} to set this player to.
	 * @param updateType     Whether to send a type update. This should be true.
	 */
	void setTransplantType(TransplantType transplantType, boolean updateType);

	/**
	 * Automatically increments/decrements the organ count by one (internal unit) depending on the gain boolean.
	 * @param gain True to increment by one internal unit, false to decrement.
	 */
	void transplantOrgan(boolean gain);

	/**
	 * Updates the player's count and/or type. This runs callbacks on type updates and count updates (see required method impls. in {@link TransplantType}).
	 * <pre>
	 * There is no newType parameter because
	 * the previous type is only necessary
	 * to call {@link TransplantType#onResetTransplantServer(net.minecraft.server.network.ServerPlayerEntity)}
	 * or {@link TransplantType#onResetTransplantClient(net.minecraft.client.network.ClientPlayerEntity)}
	 * </pre>
	 * The newAmt parameter is passed in for convenience and is the same as {@link ITransplantable#getTransplantedAmount()}.
	 *
	 * @param updateCount Whether a count update occurred.
	 * @param updateType  Whether a {@link TransplantType} update occurred.
	 * @param prevType    The previous {@link TransplantType}, only used if updateType is true.
	 * @param prevAmt     The previous amount, only used if updateCount is true.
	 * @param newAmt      The new amount, only used if updateCount is true. Passed in for convenience.
	 */
	void updateTransplants(boolean updateCount, boolean updateType, TransplantType prevType, int prevAmt, int newAmt);

	/**
	 * This sets an indicator that the player is still setting their {@link TransplantType}, and to expect that {@link ITransplantable#getTransplantType()} will be null or otherwise unusable.
	 * @param isSettingTransplant Whether the player is setting their {@link TransplantType}. True indicates that {@link ITransplantable#getTransplantType()} will be null or otherwise unusable. False indicates that {@link ITransplantable#getTransplantType()} will be usable.
	 */
	void setIsSettingTransplant(boolean isSettingTransplant);

	/**
	 * Returns whether the player is still setting their {@link TransplantType}. If true, expect that {@link ITransplantable#getTransplantType()} will be null or otherwise unusable.
	 * @return Whether the player is still setting their {@link TransplantType}. If true, expect that {@link ITransplantable#getTransplantType()} will be null or otherwise unusable. If false, {@link ITransplantable#getTransplantType()} should be usable.
	 */
	boolean getIsSettingTransplant();
}
