package mods.railcraft.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record LocomotiveLockComponent(Locomotive.Lock lock) {

  public static final Codec<LocomotiveLockComponent> CODEC =
      RecordCodecBuilder.create(instance -> instance.group(
          Locomotive.Lock.CODEC.fieldOf(CompoundTagKeys.LOCK).forGetter(LocomotiveLockComponent::lock)
      ).apply(instance, LocomotiveLockComponent::new));

  public static final StreamCodec<FriendlyByteBuf, LocomotiveLockComponent> STREAM_CODEC =
      StreamCodec.composite(
          Locomotive.Lock.STREAM_CODEC, LocomotiveLockComponent::lock,
          LocomotiveLockComponent::new);
}
