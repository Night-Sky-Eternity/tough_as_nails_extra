package net.nightskyeternity.toughasnailsextra.api.fluid;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class TANE_DefaultClientFluidTypeExtensions implements IClientFluidTypeExtensions {
    private ResourceLocation stillTexture = new ResourceLocation("block/water_still");
    private ResourceLocation flowingTexture = new ResourceLocation("block/water_flow");
    private ResourceLocation overlayTexture = new ResourceLocation("block/water_overlay");
    private int tintColor = 0xFF3F76E4;
    private int fogColor = 0x050533;
    private final Vector2f fogRange = new Vector2f(1f, 6f);

    /**
     * Sets the texture for still water. This texture is applied when the fluid is static.
     *
     * @param stillTexture the {@link ResourceLocation} representing the texture for still water
     * @return the current instance of {@code TANE_DefaultClientFluidTypeExtensions} for method chaining
     */
    public TANE_DefaultClientFluidTypeExtensions setStillTexture(ResourceLocation stillTexture) {
        this.stillTexture = stillTexture;
        return this;
    }

    /**
     * Sets the texture for flowing water. This texture is applied when the fluid is moving.
     *
     * @param flowingTexture the {@link ResourceLocation} representing the texture for flowing water
     * @return the current instance of {@code TANE_DefaultClientFluidTypeExtensions} for method chaining
     */
    public TANE_DefaultClientFluidTypeExtensions setFlowingTexture(ResourceLocation flowingTexture) {
        this.flowingTexture = flowingTexture;
        return this;
    }

    /**
     * Sets the overlay texture for the water. This texture is used for additional visual effects above the water.
     *
     * @param overlayTexture the {@link ResourceLocation} representing the overlay texture for water
     * @return the current instance of {@code TANE_DefaultClientFluidTypeExtensions} for method chaining
     */
    public TANE_DefaultClientFluidTypeExtensions setOverlayTexture(ResourceLocation overlayTexture) {
        this.overlayTexture = overlayTexture;
        return this;
    }

    /**
     * Sets the tint color of the fluid, impacting its visual appearance in the game.
     *
     * @param tintColor the new tint color in ARGB format
     * @return the current instance of {@code TANE_DefaultClientFluidTypeExtensions} for method chaining
     */
    public TANE_DefaultClientFluidTypeExtensions setTintColor(int tintColor) {
        this.tintColor = tintColor;
        return this;
    }

    /**
     * Sets the fog color of the fluid, which affects the visual appearance when the player is submerged in the fluid.
     *
     * @param fogColor the new fog color in ARGB format
     * @return the current instance of {@code TANE_DefaultClientFluidTypeExtensions} for method chaining
     */
    public TANE_DefaultClientFluidTypeExtensions setFogColor(int fogColor) {
        this.fogColor = fogColor;
        return this;
    }

    /**
     * Sets the fog range, defining the minimum and maximum distances where fog starts and ends within the fluid.
     * Automatically adjusts the range if the start distance is greater than the end distance.
     *
     * @param startFog the minimum distance where the fog starts
     * @param endFog   the maximum distance where the fog ends
     * @return the current instance of {@code TANE_DefaultClientFluidTypeExtensions} for method chaining
     */
    public TANE_DefaultClientFluidTypeExtensions setFogRange(float startFog, float endFog) {
        if (startFog > endFog) {
            float temp = startFog;
            startFog = endFog;
            endFog = temp;
        }
        this.fogRange.set(startFog, endFog);
        return this;
    }

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
}
