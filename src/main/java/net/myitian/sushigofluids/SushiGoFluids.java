package net.myitian.sushigofluids;

import com.buuz135.sushigocrafting.item.AmountItem;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@Mod(SushiGoFluids.MOD_ID)
public class SushiGoFluids {
    public static final String MOD_ID = "sushigofluids";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final HashMap<AmountItem, VirtualFluid> FLUID_HASH_MAP = new HashMap<>();
    public static final ResourceLocation AMOUNT_CAPABILITY = new ResourceLocation(SushiGoFluids.MOD_ID, "amount");

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, MOD_ID);
    public static final RegistryObject<VirtualFluid> SOY_SAUCE = FLUIDS.register("soy_sauce",
            () -> new VirtualFluid(FluidType.Properties.create()
                    .descriptionId("item.sushigocrafting.soy_sauce")
                    .temperature(20),
                    new ResourceLocation(MOD_ID, "block/fluid"),
                    0xEE2D1D17));
    public static final RegistryObject<VirtualFluid> WASABI_PASTE = FLUIDS.register("wasabi_paste",
            () -> new VirtualFluid(FluidType.Properties.create()
                    .descriptionId("item.sushigocrafting.wasabi_paste")
                    .temperature(20),
                    new ResourceLocation(MOD_ID, "block/fluid"),
                    0xFF92A348));

    public SushiGoFluids() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        FLUIDS.register(bus);
        bus.addListener(this::onFMLCommonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onFMLCommonSetup(FMLCommonSetupEvent event) {
        FLUID_HASH_MAP.put(SushiContent.Items.SOY_SAUCE.get(), SOY_SAUCE.get());
        FLUID_HASH_MAP.put(SushiContent.Items.WASABI_PASTE.get(), WASABI_PASTE.get());
    }

    @SubscribeEvent
    public void onAttachingCapabilities(final AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack itemStack = event.getObject();
        if (!(itemStack.getItem() instanceof AmountItem amountItem))
            return;
        VirtualFluid fluid = FLUID_HASH_MAP.get(amountItem);
        if (fluid == null)
            return;
        event.addCapability(AMOUNT_CAPABILITY, new AmountCapabilityProvider(itemStack, amountItem, fluid));
    }
}
