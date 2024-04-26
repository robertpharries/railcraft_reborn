package mods.railcraft.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.core.CompoundTagKeys;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;

public record LocomotiveColorComponent(DyeColor primary, DyeColor secondary) {

  public static final Codec<LocomotiveColorComponent> CODEC =
      RecordCodecBuilder.create(instance -> instance.group(
          DyeColor.CODEC.fieldOf(CompoundTagKeys.PRIMARY_COLOR).forGetter(LocomotiveColorComponent::primary),
          DyeColor.CODEC.fieldOf(CompoundTagKeys.SECONDARY_COLOR).forGetter(
              LocomotiveColorComponent::secondary)
      ).apply(instance, LocomotiveColorComponent::new));

  public static final StreamCodec<FriendlyByteBuf, LocomotiveColorComponent> STREAM_CODEC =
      StreamCodec.composite(
          DyeColor.STREAM_CODEC, LocomotiveColorComponent::primary,
          DyeColor.STREAM_CODEC, LocomotiveColorComponent::secondary,
          LocomotiveColorComponent::new);
}
