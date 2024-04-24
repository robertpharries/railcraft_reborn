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

public record SetRoutingDetectorMessage(
    BlockPos blockPos,
    RouterBlockEntity.Railway railway,
    SwitchTrackRouterBlockEntity.Lock lock) implements CustomPacketPayload {

  public static final Type<SetRoutingDetectorMessage> TYPE =
      new Type<>(RailcraftConstants.rl("set_routing_detector"));

  public static final StreamCodec<FriendlyByteBuf, SetRoutingDetectorMessage> STREAM_CODEC =
      CustomPacketPayload.codec(SetRoutingDetectorMessage::write, SetRoutingDetectorMessage::read);

  private static SetRoutingDetectorMessage read(FriendlyByteBuf in) {
    var blockPos = in.readBlockPos();
    var railway = in.readEnum(RouterBlockEntity.Railway.class);
    var lock = in.readEnum(SwitchTrackRouterBlockEntity.Lock.class);
    return new SetRoutingDetectorMessage(blockPos, railway, lock);
  }

  private void write(FriendlyByteBuf out) {
    out.writeBlockPos(this.blockPos);
    out.writeEnum(this.railway);
    out.writeEnum(this.lock);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetRoutingDetectorMessage message, IPayloadContext context) {
    var player = context.player();
    var level = player.level();
    var senderProfile = player.getGameProfile();
    level.getBlockEntity(message.blockPos, RailcraftBlockEntityTypes.ROUTING_DETECTOR.get())
        .filter(routingDetector -> routingDetector.canAccess(senderProfile))
        .ifPresent(routingDetector -> {
          routingDetector.setLock(
              message.lock.equals(SwitchTrackRouterBlockEntity.Lock.UNLOCKED)
                  ? null
                  : senderProfile);
          routingDetector.setRailway(
              message.railway.equals(RouterBlockEntity.Railway.PUBLIC)
                  ? null
                  : senderProfile);
          routingDetector.syncToClient();
          routingDetector.setChanged();
        });
  }
}
