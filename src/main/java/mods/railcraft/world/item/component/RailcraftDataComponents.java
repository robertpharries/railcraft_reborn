package mods.railcraft.world.item.component;

import com.mojang.serialization.Codec;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftDataComponents {

  private static final DeferredRegister.DataComponents deferredRegister =
      DeferredRegister.createDataComponents(RailcraftConstants.ID);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<LocomotiveColorComponent>> LOCOMOTIVE_COLOR =
      deferredRegister.register("locomotive_color", () ->
          DataComponentType.<LocomotiveColorComponent>builder()
              .persistent(LocomotiveColorComponent.CODEC)
              .networkSynchronized(LocomotiveColorComponent.STREAM_CODEC)
              .build());

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<LocomotiveLockComponent>> LOCOMOTIVE_LOCK =
      deferredRegister.register("locomotive_lock", () ->
          DataComponentType.<LocomotiveLockComponent>builder()
              .persistent(LocomotiveLockComponent.CODEC)
              .networkSynchronized(LocomotiveLockComponent.STREAM_CODEC)
              .build());

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<LocomotiveWhistlePitchComponent>> LOCOMOTIVE_WHISTLE_PITCH =
      deferredRegister.register("locomotive_whistle_pitch", () ->
          DataComponentType.<LocomotiveWhistlePitchComponent>builder()
              .persistent(LocomotiveWhistlePitchComponent.CODEC)
              .networkSynchronized(LocomotiveWhistlePitchComponent.STREAM_CODEC)
              .build());

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<LocomotiveOwnerComponent>> LOCOMOTIVE_OWNER =
      deferredRegister.register("locomotive_owner", () ->
          DataComponentType.<LocomotiveOwnerComponent>builder()
              .persistent(LocomotiveOwnerComponent.CODEC)
              .networkSynchronized(LocomotiveOwnerComponent.STREAM_CODEC)
              .build());

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<LocomotiveEnergyComponent>> LOCOMOTIVE_ENERGY =
      deferredRegister.register("locomotive_energy", () ->
          DataComponentType.<LocomotiveEnergyComponent>builder()
              .persistent(LocomotiveEnergyComponent.CODEC)
              .networkSynchronized(LocomotiveEnergyComponent.STREAM_CODEC)
              .build());

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<TicketComponent>> TICKET =
      deferredRegister.register("ticket", () ->
          DataComponentType.<TicketComponent>builder()
              .persistent(TicketComponent.CODEC)
              .networkSynchronized(TicketComponent.STREAM_CODEC)
              .build());

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<AuraComponent>> AURA =
      deferredRegister.register("aura", () ->
          DataComponentType.<AuraComponent>builder()
              .persistent(AuraComponent.CODEC)
              .networkSynchronized(AuraComponent.STREAM_CODEC)
              .build());

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<PairToolComponent>> PAIR_TOOL =
      deferredRegister.register("pair_tool", () ->
          DataComponentType.<PairToolComponent>builder()
              .persistent(PairToolComponent.CODEC)
              .networkSynchronized(PairToolComponent.STREAM_CODEC)
              .build());

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<SeasonComponent>> SEASON =
      deferredRegister.register("season", () ->
          DataComponentType.<SeasonComponent>builder()
              .persistent(SeasonComponent.CODEC)
              .networkSynchronized(SeasonComponent.STREAM_CODEC)
              .build());

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> CLICK_TO_CRAFT =
      deferredRegister.register("click_to_craft", () ->
          DataComponentType.<Boolean>builder()
              .persistent(Codec.BOOL)
              .networkSynchronized(ByteBufCodecs.BOOL)
              .build());

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<RoutingTableBookContent>> ROUTING_TABLE_BOOK =
      deferredRegister.register("routing_table_book", () ->
          DataComponentType.<RoutingTableBookContent>builder()
              .persistent(RoutingTableBookContent.CODEC)
              .networkSynchronized(RoutingTableBookContent.STREAM_CODEC)
              .build());

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<SimpleFluidContent>> FLUID =
      deferredRegister.register("simple_fluid_content", () ->
          DataComponentType.<SimpleFluidContent>builder()
              .persistent(SimpleFluidContent.CODEC)
              .networkSynchronized(SimpleFluidContent.STREAM_CODEC)
              .build());
}
