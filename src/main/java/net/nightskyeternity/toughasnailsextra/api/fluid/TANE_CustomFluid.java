package net.nightskyeternity.toughasnailsextra.api.fluid;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TANE_CustomFluid {
    public static final List<TANE_CustomFluid> ALL_FLUIDS = new ArrayList<>();
    public final String name;
    public final Properties properties;
    public ForgeFlowingFluid.Properties fluidProperties;
    public Supplier<? extends FluidType> type;
    public Supplier<? extends FlowingFluid> source;
    public Supplier<? extends FlowingFluid> flowing;
    public Supplier<? extends Item> bucket;
    public Supplier<? extends LiquidBlock> block;

    public TANE_CustomFluid(String name, DeferredRegister<FluidType> fluidTypeRegisterer, DeferredRegister<Fluid> fluidRegisterer, @NotNull Properties properties) {
        ALL_FLUIDS.add(this);
        this.properties = properties;
        this.name = name;

        this.type = type(name, fluidTypeRegisterer, properties.getTypeProperties(), properties.getClientFluidTypeExtensions());
        this.source = source(name + "_source", fluidRegisterer);
        this.flowing = flowing(name + "_flowing", fluidRegisterer);

        DefaultFluidProperties defaultProperties = properties.getDefaultFluidProperties();

        this.fluidProperties = new ForgeFlowingFluid.Properties(this.type, this.source, this.flowing)
                .explosionResistance(defaultProperties.explosionResistance)
                .levelDecreasePerBlock(defaultProperties.levelDecreasePerBlock)
                .tickRate(defaultProperties.tickRate)
                .slopeFindDistance(defaultProperties.slopeFindDistance);
    }

    protected RegistryObject<FluidType> type(String name, @NotNull DeferredRegister<FluidType> fluidTypeRegisterer, FluidType.Properties typeProperties, IClientFluidTypeExtensions clientFluidTypeExtensions) {
        return fluidTypeRegisterer.register(name, () -> new FluidType(typeProperties) {
            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(clientFluidTypeExtensions);
            }
        });
    }

    protected RegistryObject<ForgeFlowingFluid.Source> source(String name, @NotNull DeferredRegister<Fluid> fluidRegisterer) {
        return fluidRegisterer.register(name, () -> new ForgeFlowingFluid.Source(fluidProperties));
    }

    protected RegistryObject<ForgeFlowingFluid.Flowing> flowing(String name, @NotNull DeferredRegister<Fluid> fluidRegisterer) {
        return fluidRegisterer.register(name, () -> new ForgeFlowingFluid.Flowing(fluidProperties));
    }

    protected RegistryObject<LiquidBlock> block(String name, @NotNull DeferredRegister<Block> blockRegisterer, BlockBehaviour.Properties properties) {
        return blockRegisterer.register(name, () -> new LiquidBlock(this.source, properties));
    }

    protected RegistryObject<BucketItem> bucket(String name, @NotNull DeferredRegister<Item> itemRegisterer, Item.Properties properties) {
        return itemRegisterer.register(name, () -> new BucketItem(this.source, properties));
    }

    public TANE_CustomFluid setBlock(@NotNull DeferredRegister<Block> blockRegisterer, BlockBehaviour.Properties properties) {
        this.block = block(name, blockRegisterer, properties);
        this.fluidProperties.block(this.block);
        return this;
    }

    public TANE_CustomFluid setBlock(@NotNull DeferredRegister<Block> blockRegisterer) {
        return setBlock(blockRegisterer, BlockBehaviour.Properties.copy(Blocks.WATER));
    }

    public TANE_CustomFluid setBucket(@NotNull DeferredRegister<Item> itemRegisterer, Item.Properties properties) {
        this.bucket = bucket(name + "_bucket", itemRegisterer, properties);
        this.fluidProperties.bucket(this.bucket);
        return this;
    }

    public TANE_CustomFluid setBucket(@NotNull DeferredRegister<Item> itemRegisterer) {
        return setBucket(itemRegisterer, new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET));
    }

    public static final class Properties {
        private FluidType.Properties typeProperties;
        private IClientFluidTypeExtensions clientFluidTypeExtensions;
        private DefaultFluidProperties defaultFluidProperties;
        private RenderType renderType;

        private Properties() {
        }

        @Contract(value = " -> new", pure = true)
        public static @NotNull Properties create() {
            return new Properties();
        }

        public TANE_CustomFluid.Properties setTypeProperties(FluidType.Properties typeProperties) {
            this.typeProperties = typeProperties;
            return this;
        }

        public TANE_CustomFluid.Properties setClientExtension(IClientFluidTypeExtensions clientFluidTypeExtensions) {
            this.clientFluidTypeExtensions = clientFluidTypeExtensions;
            return this;
        }

        public TANE_CustomFluid.Properties setDefaultFluidProperties(DefaultFluidProperties defaultFluidProperties) {
            this.defaultFluidProperties = defaultFluidProperties;
            return this;
        }

        public TANE_CustomFluid.Properties setRenderType(RenderType renderType) {
            this.renderType = renderType;
            return this;
        }

        public FluidType.Properties getTypeProperties() {
            return typeProperties != null ? typeProperties : FluidType.Properties.create().canExtinguish(true).supportsBoating(true).canHydrate(true);
        }

        public IClientFluidTypeExtensions getClientFluidTypeExtensions() {
            return clientFluidTypeExtensions != null ? clientFluidTypeExtensions : new TANE_DefaultClientFluidTypeExtensions();
        }

        public DefaultFluidProperties getDefaultFluidProperties() {
            return defaultFluidProperties != null ? defaultFluidProperties : new DefaultFluidProperties();
        }

        public RenderType getRenderType() {
            return renderType != null ? renderType : RenderType.translucent();
        }
    }

    public static class DefaultFluidProperties {
        private int levelDecreasePerBlock = 1;
        private float explosionResistance = 1;
        private int slopeFindDistance = 4;
        private int tickRate = 5;

        public DefaultFluidProperties levelDecreasePerBlock(int levelDecreasePerBlock) {
            this.levelDecreasePerBlock = levelDecreasePerBlock;
            return this;
        }

        public DefaultFluidProperties explosionResistance(float explosionResistance) {
            this.explosionResistance = explosionResistance;
            return this;
        }

        public DefaultFluidProperties slopeFindDistance(int slopeFindDistance) {
            this.slopeFindDistance = slopeFindDistance;
            return this;
        }

        public DefaultFluidProperties tickRate(int tickRate) {
            this.tickRate = tickRate;
            return this;
        }
    }
}
