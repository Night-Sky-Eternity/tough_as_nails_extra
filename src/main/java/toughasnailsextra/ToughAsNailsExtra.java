package toughasnailsextra;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import toughasnailsextra.content.fluids.ModFluids;
import org.slf4j.Logger;

@Mod(ToughAsNailsExtra.MOD_ID)
public class ToughAsNailsExtra {
    public static final String MOD_ID = "toughasnailsextra";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ToughAsNailsExtra() {
        LOGGER.info("Initializing ToughAsNailsExtra Mod");
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);


        Register(modEventBus);
        ModFluids.register(modEventBus);
    }

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> EIGHT_BALL = ITEMS.register("eight_ball",
            () -> new Item(new Item.Properties()));

    public void Register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);
    }
}
