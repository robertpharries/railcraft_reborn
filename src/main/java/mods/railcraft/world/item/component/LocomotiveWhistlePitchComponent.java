package mods.railcraft.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.core.CompoundTagKeys;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record LocomotiveWhistlePitchComponent(float whistlePitch) {

  public static final Codec<LocomotiveWhistlePitchComponent> CODEC =
      RecordCodecBuilder.create(instance -> instance.group(
          Codec.FLOAT.fieldOf(CompoundTagKeys.WHISTLE_PITCH).forGetter(
              LocomotiveWhistlePitchComponent::whistlePitch)
      ).apply(instance, LocomotiveWhistlePitchComponent::new));

  public static final StreamCodec<FriendlyByteBuf, LocomotiveWhistlePitchComponent> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.FLOAT, LocomotiveWhistlePitchComponent::whistlePitch,
          LocomotiveWhistlePitchComponent::new);
}
