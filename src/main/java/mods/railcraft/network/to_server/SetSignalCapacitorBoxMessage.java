package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.signal.SignalCapacitorBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetSignalCapacitorBoxMessage(
    BlockPos blockPos, short ticksToPower,
    SignalCapacitorBoxBlockEntity.Mode mode) implements CustomPacketPayload {

  public static final Type<SetSignalCapacitorBoxMessage> TYPE =
      new Type<>(RailcraftConstants.rl("set_signal_capacitor_box"));

  public static final StreamCodec<FriendlyByteBuf, SetSignalCapacitorBoxMessage> STREAM_CODEC =
      CustomPacketPayload.codec(SetSignalCapacitorBoxMessage::write, SetSignalCapacitorBoxMessage::read);

  private static SetSignalCapacitorBoxMessage read(FriendlyByteBuf buf) {
    return new SetSignalCapacitorBoxMessage(buf.readBlockPos(), buf.readShort(),
        buf.readEnum(SignalCapacitorBoxBlockEntity.Mode.class));
  }

  private void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(this.blockPos);
    buf.writeShort(this.ticksToPower);
    buf.writeEnum(this.mode);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetSignalCapacitorBoxMessage message, IPayloadContext context) {
    context.player().level()
        .getBlockEntity(message.blockPos, RailcraftBlockEntityTypes.SIGNAL_CAPACITOR_BOX.get())
        .ifPresent(signalBox -> {
          signalBox.setTicksToPower(message.ticksToPower);
          signalBox.setMode(message.mode);
          signalBox.syncToClient();
          signalBox.setChanged();
        });
  }
}
