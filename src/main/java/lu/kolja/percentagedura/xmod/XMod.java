package lu.kolja.percentagedura.xmod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import lu.kolja.percentagedura.compatibility.Compatibility;
import lu.kolja.percentagedura.compatibility.GTCEUM;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.ModList;

public class XMod {
    public static final List<Compatibility.CompatibilitySupplier> COMPATIBILITY = new ArrayList<>();

    public XMod() {
        register(itemStack -> {
            if (ModList.get().isLoaded("gtceu")) {
                var gtceum = GTCEUM.from(itemStack);
                return gtceum == null ? null : gtceum.registerCompatibility();
            }
            return null;
        });
        // Any FE Storages + AE2 compatibility
        register(itemStack -> {
            LazyOptional<IEnergyStorage> energyStorage = itemStack.getCapability(ForgeCapabilities.ENERGY);
            if (energyStorage.isPresent()) {
                IEnergyStorage energyStorage1 = energyStorage.orElseThrow(NullPointerException::new);
                return Collections.singletonList(new Compatibility(
                        Objects.equals(itemStack.getItem().getCreatorModId(itemStack), "ae2") ? (double) energyStorage1.getEnergyStored() / 2 : energyStorage1.getEnergyStored(),
                        ((double) energyStorage1.getEnergyStored() / (double) energyStorage1.getMaxEnergyStored()),
                        itemStack.getItem().getBarColor(itemStack),
                        itemStack.isBarVisible()
                ));
            }
            return null;
        });
        // Durability
        register(itemStack -> {
            if (itemStack.isDamageableItem()) {
                if (ModList.get().isLoaded("gtceu") && GTCEUM.from(itemStack) == null) {
                    int damage = itemStack.getDamageValue();
                    int maxDamage = itemStack.getMaxDamage();
                    double percentage = ((double) (maxDamage - damage) / (double) maxDamage);
                    return Collections.singletonList(new Compatibility(
                                    maxDamage - damage,
                                    percentage,
                                    itemStack.getBarColor(),
                                    itemStack.isBarVisible())
                    );
                }
            }
            return null;
        });
    }

    private void register(Compatibility.CompatibilitySupplier supplier) {
        COMPATIBILITY.add(supplier);
    }
}
