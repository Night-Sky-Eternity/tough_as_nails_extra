package toughasnailsextra.content.fluids;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import toughasnailsextra.ToughAsNailsExtra;
import toughasnailsextra.api.fluids.ClientFluidExtension;
import toughasnailsextra.api.fluids.CustomFluid;

public class ModFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create( ForgeRegistries.Keys.FLUID_TYPES, ToughAsNailsExtra.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, ToughAsNailsExtra.MOD_ID);

    public static final CustomFluid BLUEBERRY_JUICE = new CustomFluid("blueberry_juice", FLUID_TYPES, FLUIDS, ToughAsNailsExtra.BLOCKS)
            .setBucketProperties(ToughAsNailsExtra.ITEMS)
            .setClientExtension(new ClientFluidExtension().setTintColor(0xA17319e0))
            .setRenderType(RenderType.translucent())
            .create();

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
        FLUIDS.register(eventBus);
    }
}
