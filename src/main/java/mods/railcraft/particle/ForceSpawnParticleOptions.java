package mods.railcraft.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ForceSpawnParticleOptions(int color) implements ParticleOptions {

  public static final MapCodec<ForceSpawnParticleOptions> CODEC =
      RecordCodecBuilder.mapCodec(instance -> instance.group(
          Codec.INT.fieldOf("color").forGetter(ForceSpawnParticleOptions::color)
      ).apply(instance, ForceSpawnParticleOptions::new));

  public static final StreamCodec<FriendlyByteBuf, ForceSpawnParticleOptions> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.VAR_INT, ForceSpawnParticleOptions::color,
          ForceSpawnParticleOptions::new);

  @Override
  public ParticleType<?> getType() {
    return RailcraftParticleTypes.FORCE_SPAWN.get();
  }
}
