package toughasnailsextra.api.fluids;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidType;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import toughasnailsextra.ToughAsNailsExtra;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.List;
import java.util.ArrayList;

/**
 * This class provides a builder pattern for constructing custom fluids in a Minecraft mod. It allows for the
 * configuration of fluid properties, block interactions, and bucket items associated with the fluid.
 */

public class CustomFluid {
    public static final List<CustomFluid> ALL_FLUIDS = new ArrayList<>();

    public RegistryObject<FluidType> type;
    public FluidType.Properties typeProperties;
    public RegistryObject<LiquidBlock> block;
    public RegistryObject<BucketItem> bucket;
    public ForgeFlowingFluid.Properties properties;
    public RegistryObject<ForgeFlowingFluid.Source> source;
    public RegistryObject<ForgeFlowingFluid.Flowing> flowing;
    public AdditionalProperties additionalProperties;
    public ClientFluidExtension clientFluidExtension;
    public String name;
    public BlockBehaviour.Properties blockProperties;
    public Item.Properties itemProperties;
    public DeferredRegister<FluidType> fluidTypeRegisterer;
    public DeferredRegister<Fluid> fluidRegisterer;
    public DeferredRegister<Block> fluidBlockRegisterer;
    public DeferredRegister<Item> bucketItemRegisterer;
    public RenderType renderType = RenderType.solid();

    public CustomFluid(String name, DeferredRegister<FluidType> fluidTypeRegisterer, DeferredRegister<Fluid> fluidRegisterer, DeferredRegister<Block> fluidBlockRegisterer) {
        this.name = name;
        this.fluidTypeRegisterer = fluidTypeRegisterer;
        this.fluidRegisterer = fluidRegisterer;
        this.fluidBlockRegisterer = fluidBlockRegisterer;
        ALL_FLUIDS.add(this);
    }

    public CustomFluid setAdditionalProperties(AdditionalProperties additionalProperties) {
        this.additionalProperties = additionalProperties;
        return this;
    }

    public CustomFluid setFluidTypeProperties(FluidType.Properties typeProperties) {
        this.typeProperties = typeProperties;
        return this;
    }

    public CustomFluid setClientExtension(ClientFluidExtension clientFluidExtension) {
        this.clientFluidExtension = clientFluidExtension;
        return this;
    }

    public CustomFluid setBlockProperties(BlockBehaviour.Properties blockProperties) {
        this.blockProperties = blockProperties;
        return this;
    }

    public CustomFluid setBucketProperties(DeferredRegister<Item> bucketItemRegisterer, @Nullable Item.Properties itemProperties) {
        this.bucketItemRegisterer = bucketItemRegisterer;
        this.itemProperties = Objects.requireNonNullElseGet(itemProperties, () -> new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET));
        return this;
    }

    public CustomFluid setBucketProperties(DeferredRegister<Item> bucketItemRegisterer) {
        return this.setBucketProperties(bucketItemRegisterer, null);
    }

    public CustomFluid setRenderType(RenderType renderType) {
        this.renderType = renderType;
        return this;
    }

    public CustomFluid create() {
        if (this.typeProperties == null) {
            this.typeProperties = FluidType.Properties.create().supportsBoating(true).canConvertToSource(true).canExtinguish(true);
        }

        this.type = this.fluidTypeRegisterer.register(name, () -> new FluidType(this.typeProperties) {
            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(Objects.requireNonNullElseGet(clientFluidExtension, ClientFluidExtension::new).getExtension());
            }
        });

        this.source = this.fluidRegisterer.register(name + "_source", () -> new ForgeFlowingFluid.Source(this.properties));
        this.flowing = this.fluidRegisterer.register(name + "_flowing", () -> new ForgeFlowingFluid.Flowing(this.properties));

        this.properties = new ForgeFlowingFluid.Properties(this.type, this.source, this.flowing);

        if (additionalProperties != null) {
            this.properties.explosionResistance(additionalProperties.explosionResistance)
                    .levelDecreasePerBlock(additionalProperties.levelDecreasePerBlock)
                    .slopeFindDistance(additionalProperties.slopeFindDistance).tickRate(additionalProperties.tickRate);
        }

        this.block = this.fluidBlockRegisterer.register(name, () -> new LiquidBlock(this.source, Objects.requireNonNullElseGet(blockProperties, () -> BlockBehaviour.Properties.copy(Blocks.WATER))));
        this.properties.block(this.block);

        if (bucketItemRegisterer != null) {
            this.bucket = this.bucketItemRegisterer.register(name + "_bucket", () -> new BucketItem(this.source, itemProperties));
            this.properties.bucket(this.bucket);
        }
        
        return this;
    }


    @Mod.EventBusSubscriber(modid = ToughAsNailsExtra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            for (CustomFluid fluid : CustomFluid.ALL_FLUIDS) {
                ItemBlockRenderTypes.setRenderLayer(fluid.source.get(), fluid.renderType);
                ItemBlockRenderTypes.setRenderLayer(fluid.flowing.get(), fluid.renderType);
            }
        }
    }

    /**
     * Provides configurable properties for fluids in a Minecraft mod. This class allows
     * modification of various attributes that affect fluid behavior, such as flow speed,
     * resistance to explosions, and update rate.
     */
    public static class AdditionalProperties {
        private int levelDecreasePerBlock = 1;
        private float explosionResistance = 1;
        private int slopeFindDistance = 4;
        private int tickRate = 5;

        /**
         * Sets the rate at which the fluid level decreases per block as it flows.
         * This affects how far the fluid can travel before it stops flowing.
         *
         * @param levelDecreasePerBlock the level decrease per block, greater values mean shorter flow distance
         * @return this {@code FluidProperties} instance for chaining setter calls
         */
        public AdditionalProperties levelDecreasePerBlock(int levelDecreasePerBlock) {
            this.levelDecreasePerBlock = levelDecreasePerBlock;
            return this;
        }

        /**
         * Sets the explosion resistance of the fluid. Higher values provide more resistance against explosions.
         *
         * @param explosionResistance the explosion resistance of the fluid, where higher values indicate stronger resistance
         * @return this {@code FluidProperties} instance for chaining setter calls
         */
        public AdditionalProperties explosionResistance(float explosionResistance) {
            this.explosionResistance = explosionResistance;
            return this;
        }

        /**
         * Sets the maximum distance the fluid will search downhill across horizontal surfaces to find an edge to flow down.
         * This determines how the fluid behaves on slopes and how far it looks for a path downwards.
         *
         * @param slopeFindDistance the maximum search distance for downward slopes
         * @return this {@code FluidProperties} instance for chaining setter calls
         */
        public AdditionalProperties slopeFindDistance(int slopeFindDistance) {
            this.slopeFindDistance = slopeFindDistance;
            return this;
        }

        /**
         * Sets the tick rate for the fluid, defining how often the fluid updates.
         * Lower values make the fluid update more frequently.
         *
         * @param tickRate the rate at which the fluid updates, with lower values indicating more frequent updates
         * @return this {@code FluidProperties} instance for chaining setter calls
         */
        public AdditionalProperties tickRate(int tickRate) {
            this.tickRate = tickRate;
            return this;
        }
    }
}

