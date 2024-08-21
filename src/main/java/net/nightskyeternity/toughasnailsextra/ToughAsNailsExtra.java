package net.nightskyeternity.toughasnailsextra;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.nightskyeternity.toughasnailsextra.api.events.TANE_LoadComplete;
import net.nightskyeternity.toughasnailsextra.api.events.TANE_onClientSetup;
import net.nightskyeternity.toughasnailsextra.api.events.TANE_AttachedCapabilities;
import net.nightskyeternity.toughasnailsextra.content.fluid.TANE_Fluids;
import org.slf4j.Logger;

@Mod(ToughAsNailsExtra.MOD_ID)
public class ToughAsNailsExtra {
    public static final String MOD_ID = "toughasnailsextra";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ToughAsNailsExtra() {
        LOGGER.info("Initializing ToughAsNailsExtra Mod");
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new TANE_AttachedCapabilities());
        modEventBus.addListener(TANE_LoadComplete::onLoadComplete);
        modEventBus.register(TANE_onClientSetup.class);

        // Register the class with client-specific event handlers

        // Registering other components
        REGISTERER.register(modEventBus);
        TANE_Fluids.register(modEventBus);

    }

    public static class REGISTERER {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

        public static void register(IEventBus modEventBus) {
            ITEMS.register(modEventBus);
            BLOCKS.register(modEventBus);
        }
    }
}
