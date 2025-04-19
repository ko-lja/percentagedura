package lu.kolja.percentagedura.compatibility;

import java.util.ArrayList;
import java.util.List;

import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.IElectricItem;
import com.gregtechceu.gtceu.api.item.IComponentItem;
import com.gregtechceu.gtceu.api.item.IGTTool;
import com.gregtechceu.gtceu.api.item.component.IDurabilityBar;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;

public record GTCEUM(ItemStack itemStack) {
    public static final int COLOR_BAR_ENERGY = FastColor.ARGB32.color(255, 47, 155, 237);
    public static final int COLOR_BAR_DURABILITY = FastColor.ARGB32.color(255, 37, 199, 4);

    public static GTCEUM from(ItemStack itemStack) {
        if (itemStack.getItem() instanceof IGTTool || itemStack.getItem() instanceof IComponentItem) {
            return new GTCEUM(itemStack);
        }
        return null;
    }

    public List<Compatibility> registerCompatibility() {
        if (itemStack.getItem() instanceof IGTTool gtTool) {
            var compatibility = new ArrayList<Compatibility>();
            if (itemStack.isDamageableItem()) {
                int damage = itemStack.getDamageValue();
                int maxDamage = itemStack.getMaxDamage();
                double percentage = ((double) (maxDamage - damage) / (double) maxDamage);
                compatibility.add(new Compatibility(maxDamage - damage, percentage, COLOR_BAR_DURABILITY, true));
            }
            if (gtTool.isElectric()) {
                long charge = gtTool.getCharge(itemStack);
                long maxCharge = gtTool.getMaxCharge(itemStack);
                double percentage = ((double) charge / (double) maxCharge);
                compatibility.add(new Compatibility(charge, percentage, COLOR_BAR_ENERGY, true));
            }
            return compatibility;
        } else if (itemStack.getItem() instanceof IComponentItem componentItem) {
            var compatibility = new ArrayList<Compatibility>();
            IDurabilityBar bar = null;
            for (var component : componentItem.getComponents()) {
                if (component instanceof IDurabilityBar durabilityBar) bar = durabilityBar;
            }
            if (bar != null) {
                int damage = itemStack.getDamageValue();
                int maxDamage = itemStack.getMaxDamage();
                double percentage = ((double) (maxDamage - damage) / (double) maxDamage);
                compatibility.add(new Compatibility(maxDamage - damage, percentage, COLOR_BAR_DURABILITY, true));
            }

            IElectricItem electricItem = GTCapabilityHelper.getElectricItem(itemStack);
            if (electricItem != null) {
                long charge = electricItem.getCharge();
                long maxCharge = electricItem.getMaxCharge();
                double percentage = ((double) charge / (double) maxCharge);
                compatibility.add(new Compatibility(charge, percentage, COLOR_BAR_ENERGY, true));
            }
            return compatibility;
        }
        return null;
    }
}
