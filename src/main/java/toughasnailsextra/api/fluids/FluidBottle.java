package toughasnailsextra.api.fluids;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public class FluidBottle extends Item {
    private final Supplier<? extends Fluid> fluidSupplier;

    public FluidBottle(Supplier<? extends Fluid> supplier, Item.Properties builder) {
        super(builder);
        this.fluidSupplier = supplier;
    }


}
