package net.nightskyeternity.toughasnailsextra.api.events;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.nightskyeternity.toughasnailsextra.api.fluid.TANE_CustomFluid;

import static net.nightskyeternity.toughasnailsextra.ToughAsNailsExtra.LOGGER;

public class TANE_onClientSetup {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        LOGGER.info("among us");
        for (TANE_CustomFluid fluid : TANE_CustomFluid.ALL_FLUIDS) {
            ItemBlockRenderTypes.setRenderLayer(fluid.source.get(), fluid.properties.getRenderType());
            ItemBlockRenderTypes.setRenderLayer(fluid.flowing.get(), fluid.properties.getRenderType());
        }
    }
}
