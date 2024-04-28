package mods.railcraft.network.to_server;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.RoutingTableBookItem;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import mods.railcraft.world.item.component.RoutingTableBookContent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.Filterable;
import net.minecraft.server.network.FilteredText;
import net.minecraft.server.network.TextFilter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
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

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(EditRoutingTableBookMessage message, IPayloadContext context) {
    var player = context.player();
    var senderProfile = player.getGameProfile();
    var itemStack = player.getItemInHand(message.hand);
    if (itemStack.getItem() instanceof RoutingTableBookItem) {
      filterTextPacket((ServerPlayer) player, message.pages)
          .thenAcceptAsync(pages -> {
            var content = pages.stream().map(x -> filterableFromOutgoing(player, x)).toList();
            itemStack.set(RailcraftDataComponents.ROUTING_TABLE_BOOK,
                new RoutingTableBookContent(content, senderProfile.getName(), message.title));
          });
    }
  }

  private static <T, R> CompletableFuture<R> filterTextPacket(ServerPlayer player, T message,
      BiFunction<TextFilter, T, CompletableFuture<R>> processor) {
    return processor.apply(player.getTextFilter(), message).thenApply(x -> x);
  }

  private static CompletableFuture<List<FilteredText>> filterTextPacket(ServerPlayer player,
      List<String> text) {
    return filterTextPacket(player, text, TextFilter::processMessageBundle);
  }

  private static Filterable<String> filterableFromOutgoing(Player player, FilteredText text) {
    return player.isTextFilteringEnabled() ? Filterable.passThrough(text.filteredOrEmpty()) : Filterable.from(text);
  }
}
