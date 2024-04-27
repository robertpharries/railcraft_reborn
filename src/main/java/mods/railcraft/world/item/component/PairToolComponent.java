package mods.railcraft.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.core.CompoundTagKeys;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record PairToolComponent(GlobalPos peerPos) {

  public static final Codec<PairToolComponent> CODEC =
      RecordCodecBuilder.create(instance -> instance.group(
          GlobalPos.CODEC.fieldOf(CompoundTagKeys.PEER_POS).forGetter(PairToolComponent::peerPos)
      ).apply(instance, PairToolComponent::new));

  public static final StreamCodec<FriendlyByteBuf, PairToolComponent> STREAM_CODEC =
      StreamCodec.composite(
          GlobalPos.STREAM_CODEC, PairToolComponent::peerPos,
          PairToolComponent::new);
}
