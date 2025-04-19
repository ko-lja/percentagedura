package lu.kolja.percentagedura;

import lu.kolja.percentagedura.render.DisplayState;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static lu.kolja.percentagedura.PercentageDura.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyMappings {
    public static final String CATEGORY = "key.categories." + MODID;

    public static final Lazy<KeyMapping> CLIENT_MAPPINGS = Lazy.of(() ->
            new KeyMapping(
                    "key." + PercentageDura.MODID + ".switch_bar_display",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_B,
                    CATEGORY
            )
            );

    @SubscribeEvent
    public static void registerMappings(RegisterKeyMappingsEvent event) {
        event.register(CLIENT_MAPPINGS.get());
    }

    public static DisplayState state = DisplayState.ENABLED_PERCENTAGE;
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ForgeClient {

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.END) return;
            while (CLIENT_MAPPINGS.get().consumeClick()) {
                state = DisplayState.getNext();
            }
        }
    }
}