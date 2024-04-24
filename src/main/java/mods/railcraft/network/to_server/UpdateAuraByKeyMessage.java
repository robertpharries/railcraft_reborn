package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.GogglesItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpdateAuraByKeyMessage(CompoundTag tag) implements CustomPacketPayload {

  public static final Type<UpdateAuraByKeyMessage> TYPE =
      new Type<>(RailcraftConstants.rl("update_aura_by_key"));

  public static final StreamCodec<FriendlyByteBuf, UpdateAuraByKeyMessage> STREAM_CODEC =
      CustomPacketPayload.codec(UpdateAuraByKeyMessage::write, UpdateAuraByKeyMessage::read);

  private static UpdateAuraByKeyMessage read(FriendlyByteBuf buf) {
    return new UpdateAuraByKeyMessage(buf.readNbt());
  }

  private void write(FriendlyByteBuf buf) {
    buf.writeNbt(tag);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(UpdateAuraByKeyMessage message, IPayloadContext context) {
    var player = context.player();
    var itemStack = player.getItemBySlot(EquipmentSlot.HEAD);
    if (itemStack.getItem() instanceof GogglesItem) {
      itemStack.setTag(message.tag);
    }
  }
}
