package mods.railcraft.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.world.item.GogglesItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record AuraComponent(GogglesItem.Aura aura) {

  public static final Codec<AuraComponent> CODEC =
      RecordCodecBuilder.create(instance -> instance.group(
          GogglesItem.Aura.CODEC.fieldOf(CompoundTagKeys.AURA).forGetter(AuraComponent::aura)
      ).apply(instance, AuraComponent::new));

  public static final StreamCodec<FriendlyByteBuf, AuraComponent> STREAM_CODEC =
      StreamCodec.composite(
          GogglesItem.Aura.STREAM_CODEC, AuraComponent::aura,
          AuraComponent::new);
}
