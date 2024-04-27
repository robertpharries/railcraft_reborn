package mods.railcraft.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.season.Season;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record SeasonComponent(Season season) {

  public static final Codec<SeasonComponent> CODEC =
      RecordCodecBuilder.create(instance -> instance.group(
          Season.CODEC.fieldOf(CompoundTagKeys.SEASON).forGetter(SeasonComponent::season)
      ).apply(instance, SeasonComponent::new));

  public static final StreamCodec<FriendlyByteBuf, SeasonComponent> STREAM_CODEC =
      StreamCodec.composite(
          Season.STREAM_CODEC, SeasonComponent::season,
          SeasonComponent::new);
}
