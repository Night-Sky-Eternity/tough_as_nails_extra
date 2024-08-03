package toughasnailsextra.api.fluids;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * A configuration class for client-side fluid properties in a Minecraft mod, allowing customization of
 * visual aspects such as textures and fog effects associated with custom fluids.
 */
public class ClientFluidExtension {
    private ResourceLocation stillTexture = new ResourceLocation("block/water_still");
    private ResourceLocation flowingTexture = new ResourceLocation("block/water_flow");
    private ResourceLocation overlayTexture = new ResourceLocation("block/water_overlay");
    private int tintColor = 0xFF3F76E4;
    private int fogColor = 0x050533;
    private final Vector2f fogRange = new Vector2f(1f, 6f);

    /**
     * Sets the texture for still water. This texture will be used when the fluid is not flowing.
     *
     * @param stillTexture the {@link ResourceLocation} representing the texture for still water
     * @return the current instance of {@code ClientFluidExtension} for method chaining
     */
    public ClientFluidExtension setStillTexture(ResourceLocation stillTexture) {
        this.stillTexture = stillTexture;
        return this;
    }

    /**
     * Sets the texture for flowing water. This texture will be used when the fluid is in motion.
     *
     * @param flowingTexture the {@link ResourceLocation} representing the texture for flowing water
     * @return the current instance of {@code ClientFluidExtension} for method chaining
     */
    public ClientFluidExtension setFlowingTexture(ResourceLocation flowingTexture) {
        this.flowingTexture = flowingTexture;
        return this;
    }

    /**
     * Sets the overlay texture for the water. This is typically used for additional visual effects.
     *
     * @param overlayTexture the {@link ResourceLocation} representing the overlay texture for water
     * @return the current instance of {@code ClientFluidExtension} for method chaining
     */
    public ClientFluidExtension setOverlayTexture(ResourceLocation overlayTexture) {
        this.overlayTexture = overlayTexture;
        return this;
    }

    /**
     * Sets the tint color of the fluid, affecting its overall coloration when rendered in the game.
     *
     * @param tintColor the new tint color in ARGB format
     * @return the current instance of {@code ClientFluidExtension} for method chaining
     */
    public ClientFluidExtension setTintColor(int tintColor) {
        this.tintColor = tintColor;
        return this;
    }

    /**
     * Sets the fog color of the fluid, which affects how the fluid appears when the player is submerged.
     *
     * @param fogColor the new fog color in ARGB format
     * @return the current instance of {@code ClientFluidExtension} for method chaining
     */
    public ClientFluidExtension setFogColor(int fogColor) {
        this.fogColor = fogColor;
        return this;
    }

    /**
     * Sets the fog range, defining the start and end points of fog within the fluid.
     * Ensures that the start is not greater than the end by swapping values if necessary.
     *
     * @param startFog the starting distance of the fog
     * @param endFog   the ending distance of the fog
     * @return the current instance of {@code ClientFluidExtension} for method chaining
     */
    public ClientFluidExtension setFogRange(float startFog, float endFog) {
        if (startFog > endFog) {
            float temp = startFog;
            startFog = endFog;
            endFog = temp;
        }
        this.fogRange.set(startFog, endFog);
        return this;
    }

    /**
     * Returns the current configured properties as an {@code IClientFluidTypeExtensions} instance.
     * This allows the properties to be used by the client-side fluid rendering and effect systems.
     *
     * @return the configured {@code IClientFluidTypeExtensions} instance
     */
    public IClientFluidTypeExtensions getExtension() {
        return new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return stillTexture;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return flowingTexture;
            }

            @Override
            public @Nullable ResourceLocation getOverlayTexture() {
                return overlayTexture;
            }

            @Override
            public int getTintColor() {
                return tintColor;
            }

            @Override
            public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance,
                                                    float darkenWorldAmount, Vector3f fluidFogColor) {
                return new Vector3f(((fogColor >> 16) & 0xFF) / 255.0f,
                        ((fogColor >> 8) & 0xFF) / 255.0f,
                        ((fogColor) & 0xFF) / 255.0f);
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick,
                                        float nearDistance, float farDistance, FogShape shape) {
                RenderSystem.setShaderFogStart(fogRange.x);
                RenderSystem.setShaderFogEnd(fogRange.y);
            }
        };
    }
}