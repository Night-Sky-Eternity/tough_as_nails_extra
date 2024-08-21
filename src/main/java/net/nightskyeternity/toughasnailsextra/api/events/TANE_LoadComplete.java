package net.nightskyeternity.toughasnailsextra.api.events;

import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.nightskyeternity.toughasnailsextra.api.data.TANE_FluidBottlePair;
import net.nightskyeternity.toughasnailsextra.content.fluid.TANE_Fluids;
import toughasnails.api.item.TANItems;

public class TANE_LoadComplete {
    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        new TANE_FluidBottlePair(TANItems.PURIFIED_WATER_BOTTLE.get(), TANE_Fluids.PURIFIED_WATER.source.get());
        new TANE_FluidBottlePair(TANItems.DIRTY_WATER_BOTTLE.get(), TANE_Fluids.PURIFIED_WATER.source.get());
        new TANE_FluidBottlePair(TANItems.APPLE_JUICE.get(), TANE_Fluids.APPLE_JUICE.source.get());
        new TANE_FluidBottlePair(TANItems.CACTUS_JUICE.get(), TANE_Fluids.CACTUS_JUICE.source.get());
        new TANE_FluidBottlePair(TANItems.CHORUS_FRUIT_JUICE.get(), TANE_Fluids.CHORUS_JUICE.source.get());
        new TANE_FluidBottlePair(TANItems.GLOW_BERRY_JUICE.get(), TANE_Fluids.GLOW_BERRY_JUICE.source.get());
        new TANE_FluidBottlePair(TANItems.MELON_JUICE.get(), TANE_Fluids.MELON_JUICE.source.get());
        new TANE_FluidBottlePair(TANItems.PUMPKIN_JUICE.get(), TANE_Fluids.PUMPKIN_JUICE.source.get());
        new TANE_FluidBottlePair(TANItems.SWEET_BERRY_JUICE.get(), TANE_Fluids.SWEET_BERRY_JUICE.source.get());
    }
}
