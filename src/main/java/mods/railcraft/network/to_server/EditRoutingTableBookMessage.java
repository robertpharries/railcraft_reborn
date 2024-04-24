package mods.railcraft.network.to_server;

import java.util.List;
import java.util.Optional;
import com.google.common.collect.Lists;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.RoutingTableBookItem;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record EditRoutingTableBookMessage(
    InteractionHand hand, List<String> pages,
    Optional<String> title) implements CustomPacketPayload {

  public static final Type<EditRoutingTableBookMessage> TYPE =
      new Type<>(RailcraftConstants.rl("edit_routing_table_book"));

  public static final StreamCodec<FriendlyByteBuf, EditRoutingTableBookMessage> STREAM_CODEC =
      CustomPacketPayload.codec(EditRoutingTableBookMessage::write, EditRoutingTableBookMessage::read);

  private static final int BOOK_MAX_PAGES = 50;

  private static EditRoutingTableBookMessage read(FriendlyByteBuf buf) {
    var hand = buf.readEnum(InteractionHand.class);
    var pages = buf.readCollection(FriendlyByteBuf
        .limitValue(Lists::newArrayListWithCapacity, BOOK_MAX_PAGES), FriendlyByteBuf::readUtf);
    var title = buf.readOptional(FriendlyByteBuf::readUtf);
    return new EditRoutingTableBookMessage(hand, pages, title);
  }

  private void write(FriendlyByteBuf buf) {
    buf.writeEnum(this.hand);
    buf.writeCollection(this.pages, FriendlyByteBuf::writeUtf);
    buf.writeOptional(this.title, FriendlyByteBuf::writeUtf);
  }

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
