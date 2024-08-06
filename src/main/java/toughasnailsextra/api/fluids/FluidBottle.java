package toughasnailsextra.api.fluids;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Represents a bottle that can contain a specific type of fluid, allowing players to drink the contents if applicable.
 * This item can handle the fluid using Forge's fluid capabilities and changes its state based on whether it's drinkable.
 */
public class FluidBottle extends Item {
    private final Supplier<? extends Fluid> fluidSupplier;
    private static final int CAPACITY = 250; // milli-buckets
    private boolean drinkable = false;

    /**
     * Constructs a new FluidBottle with a specified fluid.
     * Throws NullPointerException if the fluid supplier is null.
     */
    public FluidBottle(Supplier<? extends Fluid> supplier, Item.Properties properties) {
        super(properties);
        this.fluidSupplier = Objects.requireNonNull(supplier, "Fluid supplier cannot be null");
    }

    /**
     * Sets the drinkability of the fluid bottle.
     */
    public FluidBottle setDrinkablity(boolean isDrinkable) {
        this.drinkable = isDrinkable;
        return this;
    }

    /**
     * Checks if the fluid in the bottle is drinkable.
     */
    public boolean isDrinkable() {
        return drinkable;
    }

    /**
     * Handles the drinking action when the player uses this item.
     * Triggered during use to start the drinking animation if the bottle is drinkable and not on the client side.
     */
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!world.isClientSide && drinkable) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.fail(itemStack);
    }

    /**
     * Finalizes the use of the item, typically when the player finishes drinking.
     * Handles item stack shrinking and gives back an empty glass bottle if necessary.
     */
    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level world, @NotNull LivingEntity entity) {
        if (entity instanceof Player player && isDrinkable()) {

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);
            if (!player.getInventory().add(emptyBottle)) {
                player.drop(emptyBottle, false);
            }

            return stack.isEmpty() ? emptyBottle : stack;
        }
        return stack;
    }

    /**
     * Returns the duration it takes to use this item.
     */
    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return 32;
    }

    /**
     * Gets the animation to display when the player is using this item.
     */
    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.DRINK;
    }

    /**
     * Initializes capabilities for this item, specifically for handling fluids.
     * Attaches a new instance of FluidHandlerBottle to manage the fluid.
     */
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        FluidHandlerBottle fluidHandler = new FluidHandlerBottle(stack, fluidSupplier.get(), CAPACITY);
        fluidHandler.fill(new FluidStack(fluidSupplier.get(), CAPACITY), IFluidHandler.FluidAction.EXECUTE);
        return fluidHandler;
    }

    /**
     * A specialized FluidHandler for managing the fluid within a FluidBottle.
     */
    private static class FluidHandlerBottle extends FluidHandlerItemStackSimple {
        private final Fluid fluid;

        /**
         * Constructs a FluidHandlerBottle to manage the fluid within the item stack.
         */
        public FluidHandlerBottle(ItemStack container, Fluid fluid, int capacity) {
            super(container, capacity);
            this.fluid = fluid;
        }

        /**
         * Determines if this handler can fill with the specified fluid type.
         */
        @Override
        public boolean canFillFluidType(FluidStack fluidStack) {
            return fluidStack.getFluid() == this.fluid;
        }

        /**
         * Sets the container to empty, replacing the current item with a glass bottle.
         */
        @Override
        protected void setContainerToEmpty() {
            super.setContainerToEmpty();
            this.container = new ItemStack(Items.GLASS_BOTTLE);
        }
    }
}
