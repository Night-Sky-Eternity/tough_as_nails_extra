package toughasnailsextra;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
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

    @SubscribeEvent
    public void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() == Items.GLASS_BOTTLE) {
            FluidHandlerItemStackSimple emptyBottle = new FluidHandlerItemStackSimple(event.getObject(), 250) {
                @Override
                public boolean canFillFluidType(FluidStack fluid) {
                    return (fluid.getFluid() == ModFluids.BLUEBERRY_JUICE.source.get() && ModFluids.BLUEBERRY_JUICE.bottle.get() != null);
                }

                @Override
                public boolean canDrainFluidType(FluidStack fluid) {
                    return false;
                }

                @Override
                public int fill(@NotNull FluidStack resource, FluidAction action) {
                    if (resource.getFluid() == ModFluids.BLUEBERRY_JUICE.source.get() && ModFluids.BLUEBERRY_JUICE.bottle.get() != null) {
                        int amountFilled = super.fill(resource, action);
                        if (amountFilled > 0 && action.execute()) {
                            this.container = new ItemStack(ModFluids.BLUEBERRY_JUICE.bottle.get());
                        }
                        return amountFilled;
                    }
                    return 0;
                }

            };
            event.addCapability(new ResourceLocation(MOD_ID, "fluid_handler"), emptyBottle);
        }
    }





    // TODO, create own class for that thing
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);


    public void Register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);
    }
}
