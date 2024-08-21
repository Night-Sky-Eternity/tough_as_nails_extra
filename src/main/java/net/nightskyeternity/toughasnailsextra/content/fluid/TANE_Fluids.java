package net.nightskyeternity.toughasnailsextra.content.fluid;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nightskyeternity.toughasnailsextra.ToughAsNailsExtra;
import net.nightskyeternity.toughasnailsextra.api.fluid.TANE_CustomFluid;
import net.nightskyeternity.toughasnailsextra.api.fluid.TANE_DefaultClientFluidTypeExtensions;
import org.jetbrains.annotations.NotNull;

public class TANE_Fluids {
    // Constant for the Mod ID. Replace with your own Mod ID if used in a different context.
    private static final String MOD_ID = ToughAsNailsExtra.MOD_ID;

    // Deferred register instances for fluids and fluid types, tied to the MOD_ID.
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, MOD_ID);

    // Private constructor to prevent instantiation
    private TANE_Fluids() {
        throw new UnsupportedOperationException( this.getClass().getName() + "is a utility class and cannot be instantiated");
    }

    // Content of the mod :
    public static final TANE_CustomFluid PURIFIED_WATER = new TANE_CustomFluid("purified_water", FLUID_TYPES, FLUIDS, TANE_CustomFluid.Properties.create()
            .setClientExtension(new TANE_DefaultClientFluidTypeExtensions().setTintColor(0xA147DAFF).setFogColor(0x1C5563).setFogRange(6f, 11f))){
                @Override
                protected RegistryObject<LiquidBlock> block(String name, @NotNull DeferredRegister<Block> blockRegisterer, BlockBehaviour.Properties properties) {
                    return blockRegisterer.register(name, () -> new LiquidBlock(Fluids.WATER, properties));
                }
            }.setBlock(ToughAsNailsExtra.REGISTERER.BLOCKS).setBucket(ToughAsNailsExtra.REGISTERER.ITEMS);

    public static final TANE_CustomFluid DIRTY_WATER = new TANE_CustomFluid("dirty_water", FLUID_TYPES, FLUIDS, TANE_CustomFluid.Properties.create()
            .setClientExtension(new TANE_DefaultClientFluidTypeExtensions().setTintColor(0xFF4B5F4D).setFogColor(0x263027).setFogRange(1f, 4f)))
            .setBlock(ToughAsNailsExtra.REGISTERER.BLOCKS).setBucket(ToughAsNailsExtra.REGISTERER.ITEMS);

    public static final TANE_CustomFluid APPLE_JUICE = new TANE_CustomFluid("apple_juice", FLUID_TYPES, FLUIDS, TANE_CustomFluid.Properties.create()
            .setClientExtension(new TANE_DefaultClientFluidTypeExtensions().setTintColor(0xC1E9EC7A).setFogColor(0xAE9158).setFogRange(4f, 8f)));

    public static final TANE_CustomFluid CACTUS_JUICE = new TANE_CustomFluid("cactus_juice", FLUID_TYPES, FLUIDS, TANE_CustomFluid.Properties.create()
            .setClientExtension(new TANE_DefaultClientFluidTypeExtensions().setTintColor(0xFF70BE53).setFogColor(0x608625).setFogRange(4f, 8f)));

    public static final TANE_CustomFluid CHORUS_JUICE = new TANE_CustomFluid("chorus_juice", FLUID_TYPES, FLUIDS, TANE_CustomFluid.Properties.create()
            .setClientExtension(new TANE_DefaultClientFluidTypeExtensions().setTintColor(0xFFC683D5).setFogColor(0x75529C).setFogRange(4f, 8f)));

    public static final TANE_CustomFluid GLOW_BERRY_JUICE = new TANE_CustomFluid("glow_berry_juice", FLUID_TYPES, FLUIDS, TANE_CustomFluid.Properties.create()
            .setClientExtension(new TANE_DefaultClientFluidTypeExtensions().setTintColor(0xFFD7FF2D).setFogColor(0xD7BB18).setFogRange(4f, 8f)));

    public static final TANE_CustomFluid MELON_JUICE = new TANE_CustomFluid("melon_juice", FLUID_TYPES, FLUIDS, TANE_CustomFluid.Properties.create()
            .setClientExtension(new TANE_DefaultClientFluidTypeExtensions().setTintColor(0xFFEF7B5D).setFogColor(0xA33546).setFogRange(4f, 8f)));

    public static final TANE_CustomFluid PUMPKIN_JUICE = new TANE_CustomFluid("pumpkin_juice", FLUID_TYPES, FLUIDS, TANE_CustomFluid.Properties.create()
            .setClientExtension(new TANE_DefaultClientFluidTypeExtensions().setTintColor(0xFFEBBB6B).setFogColor(0xB66442).setFogRange(4f, 8f)));

    public static final TANE_CustomFluid SWEET_BERRY_JUICE = new TANE_CustomFluid("sweet_berry_juice", FLUID_TYPES, FLUIDS, TANE_CustomFluid.Properties.create()
            .setClientExtension(new TANE_DefaultClientFluidTypeExtensions().setTintColor(0xFF870C0E).setFogColor(0x67001E).setFogRange(4f, 8f)));




    /**
     * Registers the fluid and fluid type registries to the mod event bus.
     *
     * @param modEventBus the event bus to which the registries should be registered
     */
    public static void register(IEventBus modEventBus) {
        FLUID_TYPES.register(modEventBus);
        FLUIDS.register(modEventBus);
    }
}
