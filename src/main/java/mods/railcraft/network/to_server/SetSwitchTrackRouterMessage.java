package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.util.routing.RouterBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.SwitchTrackRouterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetSwitchTrackRouterMessage(
    BlockPos blockPos,
    RouterBlockEntity.Railway railway,
    SwitchTrackRouterBlockEntity.Lock lock) implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<SetSwitchTrackRouterMessage> TYPE =
      new Type<>(RailcraftConstants.rl("set_switch_track_router"));

  public static final StreamCodec<FriendlyByteBuf, SetSwitchTrackRouterMessage> STREAM_CODEC =
      CustomPacketPayload.codec(SetSwitchTrackRouterMessage::write, SetSwitchTrackRouterMessage::read);

  private static SetSwitchTrackRouterMessage read(FriendlyByteBuf buf) {
    var blockPos = buf.readBlockPos();
    var railway = buf.readEnum(RouterBlockEntity.Railway.class);
    var lock = buf.readEnum(SwitchTrackRouterBlockEntity.Lock.class);
    return new SetSwitchTrackRouterMessage(blockPos, railway, lock);
  }

  private void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(this.blockPos);
    buf.writeEnum(this.railway);
    buf.writeEnum(this.lock);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetSwitchTrackRouterMessage message, IPayloadContext context) {
    var player = context.player();
    var level = player.level();
    var senderProfile = player.getGameProfile();
    level.getBlockEntity(message.blockPos, RailcraftBlockEntityTypes.SWITCH_TRACK_ROUTER.get())
        .filter(switchTrackRouter -> switchTrackRouter.canAccess(senderProfile))
        .ifPresent(switchTrackRouter -> {
          switchTrackRouter.setLock(
              message.lock.equals(SwitchTrackRouterBlockEntity.Lock.UNLOCKED)
                  ? null
                  : senderProfile);
          switchTrackRouter.setRailway(
              message.railway.equals(RouterBlockEntity.Railway.PUBLIC)
                  ? null
                  : senderProfile);
          switchTrackRouter.syncToClient();
          switchTrackRouter.setChanged();
        });
  }
}
