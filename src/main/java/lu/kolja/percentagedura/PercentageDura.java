package lu.kolja.percentagedura;

import lu.kolja.percentagedura.render.PercentageDisplay;
import lu.kolja.percentagedura.xmod.XMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(PercentageDura.MODID)
public class PercentageDura {
    public static final String MODID = "percentagedura";

    public PercentageDura() {
        new XMod();
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class Client {
        @SubscribeEvent
        public static void onRegisterItemDecorations(final RegisterItemDecorationsEvent event) {
            for (Item item : ForgeRegistries.ITEMS) event.register(item, new PercentageDisplay());
        }
    }
}
