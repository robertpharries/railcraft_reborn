package mods.railcraft.network.to_server;

import java.util.List;
import java.util.Optional;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.RoutingTableBookItem;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record EditRoutingTableBookMessage(
    InteractionHand hand, List<String> pages,
    Optional<String> title) implements CustomPacketPayload {

  public static final Type<EditRoutingTableBookMessage> TYPE =
      new Type<>(RailcraftConstants.rl("edit_routing_table_book"));

  public static final StreamCodec<FriendlyByteBuf, EditRoutingTableBookMessage> STREAM_CODEC =
      StreamCodec.composite(
          NeoForgeStreamCodecs.enumCodec(InteractionHand.class), EditRoutingTableBookMessage::hand,
          ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), EditRoutingTableBookMessage::pages,
          ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), EditRoutingTableBookMessage::title,
          EditRoutingTableBookMessage::new);

  private static final int BOOK_MAX_PAGES = 50;

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(EditRoutingTableBookMessage message, IPayloadContext context) {
    var player = context.player();
    var senderProfile = player.getGameProfile();
    var itemStack = player.getItemInHand(message.hand);
    if (itemStack.getItem() instanceof RoutingTableBookItem) {
      if (!message.pages.isEmpty()) {
        var listtag = new ListTag();
        message.pages.stream().map(StringTag::valueOf).forEach(listtag::add);
        itemStack.addTagElement("pages", listtag);
      }
      itemStack.addTagElement("author", StringTag.valueOf(senderProfile.getName()));
      itemStack.addTagElement("title", StringTag.valueOf(message.title.orElse("").trim()));
    }
  }
}
