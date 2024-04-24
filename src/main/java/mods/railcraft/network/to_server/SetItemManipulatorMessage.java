package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.manipulator.ItemManipulatorBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.ManipulatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetItemManipulatorMessage(
    BlockPos blockPos,
    ManipulatorBlockEntity.RedstoneMode redstoneMode,
    ManipulatorBlockEntity.TransferMode transferMode) implements CustomPacketPayload {

  public static final Type<SetItemManipulatorMessage> TYPE =
      new Type<>(RailcraftConstants.rl("set_item_manipulator"));

  public static final StreamCodec<FriendlyByteBuf, SetItemManipulatorMessage> STREAM_CODEC =
      CustomPacketPayload.codec(SetItemManipulatorMessage::write, SetItemManipulatorMessage::read);

  private static SetItemManipulatorMessage read(FriendlyByteBuf buf) {
    return new SetItemManipulatorMessage(buf.readBlockPos(),
        buf.readEnum(ManipulatorBlockEntity.RedstoneMode.class),
        buf.readEnum(ManipulatorBlockEntity.TransferMode.class));
  }

  private void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(this.blockPos);
    buf.writeEnum(this.redstoneMode);
    buf.writeEnum(this.transferMode);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetItemManipulatorMessage message, IPayloadContext context) {
    var level = context.player().level();
    LevelUtil.getBlockEntity(level, message.blockPos, ItemManipulatorBlockEntity.class)
        .ifPresent(manipulator -> {
          manipulator.setRedstoneMode(message.redstoneMode);
          manipulator.setTransferMode(message.transferMode);
          manipulator.setChanged();
        });
  }
}
