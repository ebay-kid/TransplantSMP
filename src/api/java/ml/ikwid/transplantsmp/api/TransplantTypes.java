package ml.ikwid.transplantsmp.api;

import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;

/**
 * Contains all {@link TransplantType} entries and registration methods.
 */
public final class TransplantTypes {
    /**
     * Prevent instantiation.
     */
    private TransplantTypes() {
    }

    private static final ReferenceArraySet<TransplantType> transplantTypes = new ReferenceArraySet<>(4); // 4 to handle the default types.

    /**
     * Gets the {@link TransplantType} with the given name.
     * @param name The name of the {@link TransplantType}, as registered in the constructor.
     * @return The {@link TransplantType} with the given name, or null if none exists.
     */
    @Nullable
    public static TransplantType get(String name) {
        for(TransplantType transplantType : getTransplantTypes()) {
            if(Objects.equals(name, transplantType.toString())) {
                return transplantType;
            }
        }
        return null;
    }

    /**
     * Get the Set of all registered {@link TransplantType}. This is NOT a copy and should not be modified.
     * @return The Set of all registered {@link TransplantType}.
     */
    public static Set<TransplantType> getTransplantTypes() {
        return transplantTypes;
    }

    /**
     * Registers a {@link TransplantType}. This will throw an IllegalArgumentException if the TransplantType is already registered.
     * @param transplantType An instance of the {@link TransplantType} to register.
     */
    public static void register(@NotNull TransplantType transplantType) {
        if(!transplantTypes.add(transplantType)) {
            throw new IllegalArgumentException("TransplantType " + transplantType + " is already registered!");
        }
    }
}
