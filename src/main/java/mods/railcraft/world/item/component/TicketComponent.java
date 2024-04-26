package mods.railcraft.world.item.component;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.core.CompoundTagKeys;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.component.ResolvableProfile;

public record TicketComponent(String destination, Optional<ResolvableProfile> owner) {

  public TicketComponent(String destination, @Nullable GameProfile owner) {
    this(destination, owner == null ? Optional.empty() : Optional.of(new ResolvableProfile(owner)));
  }

  public static final Codec<TicketComponent> CODEC =
      RecordCodecBuilder.create(instance -> instance.group(
          Codec.STRING.fieldOf(CompoundTagKeys.DESTINATION).forGetter(TicketComponent::destination),
          ResolvableProfile.CODEC.optionalFieldOf(CompoundTagKeys.OWNER).forGetter(TicketComponent::owner)
      ).apply(instance, TicketComponent::new));

  public static final StreamCodec<FriendlyByteBuf, TicketComponent> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.STRING_UTF8, TicketComponent::destination,
          ByteBufCodecs.optional(ResolvableProfile.STREAM_CODEC), TicketComponent::owner,
          TicketComponent::new);
}
