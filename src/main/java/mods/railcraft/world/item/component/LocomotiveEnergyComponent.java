package mods.railcraft.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.core.CompoundTagKeys;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record LocomotiveEnergyComponent(int energy) {

  public static final Codec<LocomotiveEnergyComponent> CODEC =
      RecordCodecBuilder.create(instance -> instance.group(
          Codec.INT.fieldOf(CompoundTagKeys.ENERGY).forGetter(LocomotiveEnergyComponent::energy)
      ).apply(instance, LocomotiveEnergyComponent::new));

  public static final StreamCodec<FriendlyByteBuf, LocomotiveEnergyComponent> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.VAR_INT, LocomotiveEnergyComponent::energy,
          LocomotiveEnergyComponent::new);
}
