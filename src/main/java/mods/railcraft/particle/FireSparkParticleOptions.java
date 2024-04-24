package mods.railcraft.particle;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public record FireSparkParticleOptions(Vec3 destination) implements ParticleOptions {

  public static final MapCodec<FireSparkParticleOptions> CODEC =
      RecordCodecBuilder.mapCodec(instance -> instance.group(
          Vec3.CODEC.fieldOf("destination").forGetter(FireSparkParticleOptions::destination)
      ).apply(instance, FireSparkParticleOptions::new));

  public static final StreamCodec<FriendlyByteBuf, FireSparkParticleOptions> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.fromCodec(Vec3.CODEC), FireSparkParticleOptions::destination,
          FireSparkParticleOptions::new);

  @Override
  public ParticleType<?> getType() {
    return RailcraftParticleTypes.FIRE_SPARK.get();
  }
}
