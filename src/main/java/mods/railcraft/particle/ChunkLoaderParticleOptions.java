package mods.railcraft.particle;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public record ChunkLoaderParticleOptions(Vec3 destination) implements ParticleOptions {

  public static final MapCodec<ChunkLoaderParticleOptions> CODEC =
      RecordCodecBuilder.mapCodec(instance -> instance.group(
          Vec3.CODEC.fieldOf("destination").forGetter(ChunkLoaderParticleOptions::destination)
      ).apply(instance, ChunkLoaderParticleOptions::new));

  public static final StreamCodec<FriendlyByteBuf, ChunkLoaderParticleOptions> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.fromCodec(Vec3.CODEC), ChunkLoaderParticleOptions::destination,
          ChunkLoaderParticleOptions::new);

  @Override
  public ParticleType<?> getType() {
    return RailcraftParticleTypes.CHUNK_LOADER.get();
  }
}
