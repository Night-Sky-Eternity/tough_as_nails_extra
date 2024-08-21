package net.nightskyeternity.toughasnailsextra.api.data;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import java.util.HashMap;
import java.util.Map;

public class TANE_FluidBottlePair {
    private static final Map<Fluid, Item> fluidToItemMap = new HashMap<>();
    private static final Map<Item, Fluid> itemToFluidMap = new HashMap<>();

    private Item item;
    private Fluid fluid;

    public TANE_FluidBottlePair(Item bottle, Fluid fluid) {
        set(bottle, fluid);
    }

    public TANE_FluidBottlePair() {
    }

    public TANE_FluidBottlePair set(Item bottle, Fluid fluid) {
        // Check if the fluid already has a different bottle assigned
        if (fluidToItemMap.containsKey(fluid)) {
            Item existingBottle = fluidToItemMap.get(fluid);
            if (!existingBottle.equals(bottle)) {
                // Remove the existing bottle from the item to fluid map
                itemToFluidMap.remove(existingBottle);
                // Change the bottle for the fluid
                fluidToItemMap.put(fluid, bottle);
            }
        } else {
            // Check if the bottle already has a different fluid assigned
            if (itemToFluidMap.containsKey(bottle)) {
                Fluid existingFluid = itemToFluidMap.get(bottle);
                if (!existingFluid.equals(fluid)) {
                    // Remove the existing fluid from the fluid to item map
                    fluidToItemMap.remove(existingFluid);
                }
            }
            // Add the new fluid and bottle mapping
            fluidToItemMap.put(fluid, bottle);
        }
        // Always map the bottle to the fluid last to ensure the mapping is up to date
        itemToFluidMap.put(bottle, fluid);
        this.item = bottle;
        this.fluid = fluid;
        return this;
    }

    public TANE_FluidBottlePair removeFromFluid(Fluid fluid) {
        if (fluidToItemMap.containsKey(fluid)) {
            Item bottle = fluidToItemMap.remove(fluid);
            itemToFluidMap.remove(bottle);
        }
        return this;
    }

    public TANE_FluidBottlePair removeFromBottle(Item bottle) {
        if (itemToFluidMap.containsKey(bottle)) {
            Fluid fluid = itemToFluidMap.remove(bottle);
            fluidToItemMap.remove(fluid);
        }
        return this;
    }

    public static boolean fluidHasBottle(Fluid fluid) {
        return fluidToItemMap.containsKey(fluid);
    }

    public static boolean itemIsBottle(Item bottle) {
        return itemToFluidMap.containsKey(bottle);
    }

    public static Item getItemForFluid(Fluid fluid) {
        return fluidToItemMap.getOrDefault(fluid, null);
    }

    public static Fluid getFluidForItem(Item item) {
        return itemToFluidMap.getOrDefault(item, null);
    }
}
