package net.myitian.sushigofluids;

import com.buuz135.sushigocrafting.item.AmountItem;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

public record AmountCapabilityProvider(ItemStack itemStack,
                                       AmountItem amountItem,
                                       Fluid fluid) implements ICapabilityProvider {
    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction direction) {
        if (cap == ForgeCapabilities.FLUID_HANDLER_ITEM) {
            return LazyOptional.of(() -> new AmountFluidHandlerItem(itemStack, amountItem, fluid)).cast();
        }
        return LazyOptional.empty();
    }

    private record AmountFluidHandlerItem(ItemStack itemStack,
                                          AmountItem amountItem,
                                          Fluid fluid) implements IFluidHandlerItem {
        private static int clamp0(int v, int max) {
            return v < max ? Math.max(v, 0) : max;
        }

        public int getCurrentAmount() {
            return amountItem.getCurrentAmount(itemStack);
        }

        public int getMaxAmount() {
            return amountItem.getMaxCombineAmount();
        }

        public boolean isFluidValid(FluidStack fluidStack) {
            return fluid.isSame(fluidStack.getFluid());
        }

        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int i) {
            return new FluidStack(fluid, getCurrentAmount());
        }

        @Override
        public int getTankCapacity(int i) {
            return getMaxAmount();
        }

        @Override
        public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
            return isFluidValid(fluidStack);
        }

        @Override
        public int fill(FluidStack fluidStack, FluidAction fluidAction) {
            if (!isFluidValid(fluidStack))
                return 0;
            int max = getMaxAmount();
            int amount = clamp0(getCurrentAmount(), max);
            int maxFill = max - amount;
            int fill = Math.min(maxFill, fluidStack.getAmount());
            if (fluidAction.execute()) {
                itemStack.getOrCreateTag().putInt(AmountItem.NBT_AMOUNT, amount + fill);
            }
            return fill;
        }

        @Override
        public @NotNull FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
            if (!isFluidValid(fluidStack))
                return FluidStack.EMPTY;
            return drain(fluidStack.getAmount(), fluidAction);
        }

        @Override
        public @NotNull FluidStack drain(int i, FluidAction fluidAction) {
            int amount = clamp0(getCurrentAmount(), getMaxAmount());
            int drained = Math.min(amount, i);
            if (fluidAction.execute()) {
                amount -= drained;
                itemStack.getOrCreateTag().putInt(AmountItem.NBT_AMOUNT, amount);
                if (amount <= 0)
                    itemStack.shrink(1);
            }
            return new FluidStack(fluid, drained);
        }

        @Override
        public @NotNull ItemStack getContainer() {
            int amount = getCurrentAmount();
            return amount <= 0 ? ItemStack.EMPTY : itemStack;
        }
    }
}
