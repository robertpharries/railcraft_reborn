package mods.railcraft.world.item.component;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.core.CompoundTagKeys;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.component.ResolvableProfile;

public record LocomotiveOwnerComponent(ResolvableProfile owner) {

  public LocomotiveOwnerComponent(GameProfile owner) {
    this(new ResolvableProfile(owner));
  }

  public static final Codec<LocomotiveOwnerComponent> CODEC =
      RecordCodecBuilder.create(instance -> instance.group(
          ResolvableProfile.CODEC.fieldOf(CompoundTagKeys.OWNER).forGetter(
              LocomotiveOwnerComponent::owner)
      ).apply(instance, LocomotiveOwnerComponent::new));

  public static final StreamCodec<FriendlyByteBuf, LocomotiveOwnerComponent> STREAM_CODEC =
      StreamCodec.composite(
          ResolvableProfile.STREAM_CODEC, LocomotiveOwnerComponent::owner,
          LocomotiveOwnerComponent::new);

}
