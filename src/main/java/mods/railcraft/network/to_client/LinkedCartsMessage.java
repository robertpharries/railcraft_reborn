package mods.railcraft.network.to_client;

import java.util.Collection;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.ClientManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record LinkedCartsMessage(
    Collection<LinkedCart> linkedCarts) implements CustomPacketPayload {

  public static final Type<LinkedCartsMessage> TYPE =
      new Type<>(RailcraftConstants.rl("linked_carts"));

  public static final StreamCodec<FriendlyByteBuf, LinkedCartsMessage> STREAM_CODEC =
      CustomPacketPayload.codec(LinkedCartsMessage::write, LinkedCartsMessage::read);

  private static LinkedCartsMessage read(FriendlyByteBuf buf) {
    return new LinkedCartsMessage(buf.readList(LinkedCart::read));
  }

  private void write(FriendlyByteBuf buf) {
    buf.writeCollection(this.linkedCarts, (b, cart) -> cart.write(b));
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(LinkedCartsMessage message, IPayloadContext context) {
    ClientManager.getShuntingAuraRenderer().setLinkedCarts(message.linkedCarts);
  }

  public record LinkedCart(int entityId, @Nullable UUID trainId, int linkAId, int linkBId) {

    public LinkedCart(RollingStock extension) {
      this(
          extension.entity().getId(),
          extension.train().id(),
          extension.backLink()
              .map(RollingStock::entity)
              .map(AbstractMinecart::getId)
              .orElse(-1),
          extension.frontLink()
              .map(RollingStock::entity)
              .map(AbstractMinecart::getId)
              .orElse(-1));
    }

    public static LinkedCart read(FriendlyByteBuf buf) {
      return new LinkedCart(
          buf.readVarInt(),
          buf.readNullable(buf1 -> buf1.readUUID()),
          buf.readVarInt(),
          buf.readVarInt());
    }

    public void write(FriendlyByteBuf buf) {
      buf.writeVarInt(this.entityId);
      buf.writeNullable(this.trainId, (buf1, uuid) -> buf1.writeUUID(uuid));
      buf.writeVarInt(this.linkAId);
      buf.writeVarInt(this.linkBId);
    }
  }
}
