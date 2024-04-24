package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetLocomotiveMessage(
    int entityId, Locomotive.Mode mode,
    Locomotive.Speed speed, Locomotive.Lock lock,
    boolean reverse) implements CustomPacketPayload {

  public static final Type<SetLocomotiveMessage> TYPE =
      new Type<>(RailcraftConstants.rl("set_locomotive"));

  public static final StreamCodec<FriendlyByteBuf, SetLocomotiveMessage> STREAM_CODEC =
      CustomPacketPayload.codec(SetLocomotiveMessage::write, SetLocomotiveMessage::read);

  private static SetLocomotiveMessage read(FriendlyByteBuf buf) {
    return new SetLocomotiveMessage(buf.readVarInt(),
        buf.readEnum(Locomotive.Mode.class),
        buf.readEnum(Locomotive.Speed.class),
        buf.readEnum(Locomotive.Lock.class), buf.readBoolean());
  }

  private void write(FriendlyByteBuf buf) {
    buf.writeVarInt(this.entityId);
    buf.writeEnum(this.mode);
    buf.writeEnum(this.speed);
    buf.writeEnum(this.lock);
    buf.writeBoolean(this.reverse);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetLocomotiveMessage message, IPayloadContext context) {
    var player = context.player();
    var entity = player.level().getEntity(message.entityId);
    if (entity instanceof Locomotive locomotive && locomotive.canControl(player)) {
      locomotive.applyAction(player, false, loco -> {
        loco.setMode(message.mode);
        loco.setSpeed(message.speed);
        loco.setReverse(message.reverse);
        if (!loco.isLocked() || loco.getOwnerOrThrow().equals(player.getGameProfile())) {
          loco.setLock(message.lock);
          loco.setOwner(message.lock == Locomotive.Lock.UNLOCKED
              ? null
              : player.getGameProfile());
        }
      });
    }
  }
}
