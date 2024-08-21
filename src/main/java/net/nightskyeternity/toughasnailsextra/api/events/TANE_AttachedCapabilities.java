package net.nightskyeternity.toughasnailsextra.api.events;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import org.jetbrains.annotations.NotNull;
import net.nightskyeternity.toughasnailsextra.api.data.TANE_FluidBottlePair;

import static net.nightskyeternity.toughasnailsextra.ToughAsNailsExtra.*;

public class TANE_AttachedCapabilities {
    @SubscribeEvent
    public void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() == Items.GLASS_BOTTLE) {
            FluidHandlerItemStackSimple emptyBottle = new FluidHandlerItemStackSimple(event.getObject(), 250) {
                @Override
                public boolean canFillFluidType(FluidStack fluid) {
                    return TANE_FluidBottlePair.fluidHasBottle(fluid.getFluid());
                }

                @Override
                public boolean canDrainFluidType(FluidStack fluid) {
                    return false;
                }

                @Override
                public int fill(@NotNull FluidStack resource, FluidAction action) {
                    int amountFilled = super.fill(resource, action);
                    if (amountFilled > 0 && action.execute()) {
                        this.container = new ItemStack(TANE_FluidBottlePair.getItemForFluid(resource.getFluid()));
                    }
                    return amountFilled;
                }

            };
            event.addCapability(new ResourceLocation(MOD_ID, "fluid_handler"), emptyBottle);
        }
        if (TANE_FluidBottlePair.itemIsBottle(event.getObject().getItem())) {
            FluidHandlerItemStackSimple fluidBottle = new FluidHandlerItemStackSimple(event.getObject(), 250) {
                @Override
                public boolean canFillFluidType(FluidStack fluid) {
                    return false;
                }

                @Override
                public @NotNull FluidStack getFluid() {
                    return new FluidStack(TANE_FluidBottlePair.getFluidForItem(this.container.getItem()), 250);
                }

                @Override
                protected void setContainerToEmpty() {
                    super.setContainerToEmpty();
                    this.container = new ItemStack(Items.GLASS_BOTTLE);
                }
            };
            event.addCapability(new ResourceLocation(MOD_ID, "fluid_handler"), fluidBottle);
        }

    }
}
