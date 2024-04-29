package mods.railcraft.advancements;

import java.util.Optional;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.world.level.block.track.TrackTypes;
import net.minecraft.world.item.ItemStack;

public record TrackItemPredicate(
    Optional<Boolean> highSpeed,
    Optional<Boolean> electric,
    Optional<TrackType> type) {

  private static final Codec<TrackType> TRACK_TYPE_CODEC = TrackTypes.REGISTRY.byNameCodec();

  public static final Codec<TrackItemPredicate> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
          Codec.BOOL.optionalFieldOf("highSpeed")
              .forGetter(TrackItemPredicate::highSpeed),
          Codec.BOOL.optionalFieldOf("electric")
              .forGetter(TrackItemPredicate::electric),
          TRACK_TYPE_CODEC.optionalFieldOf("track_type")
              .forGetter(TrackItemPredicate::type))
          .apply(instance, TrackItemPredicate::new));

  public boolean matches(ItemStack stack) {
    var type = TrackUtil.getTrackType(stack);
    if (this.highSpeed.isPresent() && type.isHighSpeed() != this.highSpeed.get()) {
      return false;
    }
    if (this.electric.isPresent() && type.isElectric() != this.electric.get()) {
      return false;
    }
    if (!(this.type.isEmpty() || this.type.get().equals(type))) {
      return false;
    }
    return TrackUtil.isRail(stack);
  }
}
