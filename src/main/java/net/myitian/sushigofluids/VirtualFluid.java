package net.myitian.sushigofluids;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class VirtualFluid extends FlowingFluid {
    protected FluidType fluidType;

    public VirtualFluid(FluidType.Properties fluidType,
                        ResourceLocation res,
                        int color) {
        super();
        this.fluidType = new VirtualFluidType(fluidType, res, color);
    }

    @Override
    public @NotNull FluidType getFluidType() {
        return fluidType;
    }

    @Override
    public @NotNull Fluid getFlowing() {
        return this;
    }

    @Override
    public @NotNull Fluid getSource() {
        return this;
    }

    @Override
    protected boolean canConvertToSource(@NotNull Level arg) {
        return false;
    }

    @Override
    protected void beforeDestroyingBlock(@NotNull LevelAccessor arg, @NotNull BlockPos arg2, @NotNull BlockState arg3) {
    }

    @Override
    protected int getSlopeFindDistance(@NotNull LevelReader arg) {
        return 0;
    }

    @Override
    protected int getDropOff(@NotNull LevelReader arg) {
        return 0;
    }

    @Override
    public @NotNull Item getBucket() {
        return Items.AIR;
    }

    @Override
    protected boolean canBeReplacedWith(@NotNull FluidState arg, @NotNull BlockGetter arg2, @NotNull BlockPos arg3, @NotNull Fluid arg4, @NotNull Direction arg5) {
        return false;
    }

    @Override
    public int getTickDelay(@NotNull LevelReader arg) {
        return 0;
    }

    @Override
    protected float getExplosionResistance() {
        return 0;
    }

    @Override
    protected @NotNull BlockState createLegacyBlock(@NotNull FluidState arg) {
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean isSource(@NotNull FluidState arg) {
        return true;
    }

    @Override
    public int getAmount(@NotNull FluidState arg) {
        return 0;
    }
}
