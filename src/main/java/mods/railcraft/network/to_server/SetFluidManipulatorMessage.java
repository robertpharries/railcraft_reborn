package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.manipulator.FluidManipulatorBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.ManipulatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetFluidManipulatorMessage(
    BlockPos blockPos,
    ManipulatorBlockEntity.RedstoneMode redstoneMode) implements CustomPacketPayload {

  public static final Type<SetFluidManipulatorMessage> TYPE =
      new Type<>(RailcraftConstants.rl("set_fluid_manipulator"));

  public static final StreamCodec<FriendlyByteBuf, SetFluidManipulatorMessage> STREAM_CODEC =
      CustomPacketPayload.codec(SetFluidManipulatorMessage::write, SetFluidManipulatorMessage::read);

  private static SetFluidManipulatorMessage read(FriendlyByteBuf buf) {
    return new SetFluidManipulatorMessage(buf.readBlockPos(),
        buf.readEnum(ManipulatorBlockEntity.RedstoneMode.class));
  }

  private void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(this.blockPos);
    buf.writeEnum(this.redstoneMode);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetFluidManipulatorMessage message, IPayloadContext context) {
    var level = context.player().level();
    LevelUtil.getBlockEntity(level, message.blockPos, FluidManipulatorBlockEntity.class)
        .ifPresent(manipulator -> {
          manipulator.setRedstoneMode(message.redstoneMode);
          manipulator.setChanged();
        });
  }
}
