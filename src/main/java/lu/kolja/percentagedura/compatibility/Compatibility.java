package lu.kolja.percentagedura.compatibility;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import net.minecraft.world.item.ItemStack;

public record Compatibility(double amount, double percentage, int color, boolean isActive) {

    @FunctionalInterface
    public interface CompatibilitySupplier {
        @Nullable
        List<Compatibility> compatibility(ItemStack itemStack);
    }
}
