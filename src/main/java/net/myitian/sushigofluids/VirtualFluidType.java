package net.myitian.sushigofluids;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Consumer;

public class VirtualFluidType extends FluidType {
    private final ResourceLocation res;
    private final int color;

    public VirtualFluidType(Properties properties, ResourceLocation res, int color) {
        super(properties);
        this.res = res;
        this.color = color;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return res;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return res;
            }

            @Override
            public int getTintColor() {
                return color;
            }
        });
    }
}
