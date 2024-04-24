package mods.railcraft.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public record TuningAuraParticleOptions(Vec3 destination, int color) implements ParticleOptions {

  public static final MapCodec<TuningAuraParticleOptions> CODEC =
      RecordCodecBuilder.mapCodec(instance -> instance.group(
          Vec3.CODEC.fieldOf("destination").forGetter(TuningAuraParticleOptions::destination),
          Codec.INT.fieldOf("color").forGetter(TuningAuraParticleOptions::color)
      ).apply(instance, TuningAuraParticleOptions::new));

  public static final StreamCodec<FriendlyByteBuf, TuningAuraParticleOptions> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.fromCodec(Vec3.CODEC), TuningAuraParticleOptions::destination,
          ByteBufCodecs.INT, TuningAuraParticleOptions::color,
          TuningAuraParticleOptions::new);

  @Override
  public ParticleType<?> getType() {
    return RailcraftParticleTypes.TUNING_AURA.get();
  }
}
