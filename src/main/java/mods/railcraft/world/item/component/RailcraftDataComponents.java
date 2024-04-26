package mods.railcraft.world.item.component;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.bus.api.IEventBus;
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
}
