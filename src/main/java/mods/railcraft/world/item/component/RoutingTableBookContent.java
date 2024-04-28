package mods.railcraft.world.item.component;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mods.railcraft.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.BookContent;
import net.minecraft.world.item.component.TooltipProvider;

public record RoutingTableBookContent(
    List<Filterable<String>> pages,
    String author,
    Optional<String> title)
    implements BookContent<String, RoutingTableBookContent>, TooltipProvider {

  private static final int PAGE_EDIT_LENGTH = 1024;
  private static final int MAX_PAGES = 50;
  private static final Codec<Filterable<String>> PAGE_CODEC =
      Filterable.codec(Codec.string(0, PAGE_EDIT_LENGTH));
  private static final Codec<List<Filterable<String>>> PAGES_CODEC =
      PAGE_CODEC.sizeLimitedListOf(MAX_PAGES);
  public static final Codec<RoutingTableBookContent> CODEC =
      RecordCodecBuilder.create(instance -> instance.group(
          PAGES_CODEC.fieldOf("pages").forGetter(RoutingTableBookContent::pages),
          Codec.STRING.fieldOf("author").forGetter(RoutingTableBookContent::author),
          Codec.STRING.optionalFieldOf("title").forGetter(RoutingTableBookContent::title)
      ).apply(instance, RoutingTableBookContent::new));
  public static final StreamCodec<ByteBuf, RoutingTableBookContent> STREAM_CODEC =
      StreamCodec.composite(
          Filterable.streamCodec(ByteBufCodecs.stringUtf8(PAGE_EDIT_LENGTH))
              .apply(ByteBufCodecs.list(MAX_PAGES)), RoutingTableBookContent::pages,
          ByteBufCodecs.STRING_UTF8, RoutingTableBookContent::author,
          ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), RoutingTableBookContent::title,
          RoutingTableBookContent::new);

  public RoutingTableBookContent(List<Filterable<String>> pages,
      String author, Optional<String> title) {
    if (pages.size() > MAX_PAGES) {
      throw new IllegalArgumentException("Got " + pages.size() + " pages, but maximum is " + MAX_PAGES);
    } else {
      this.pages = pages;
      this.author = author;
      this.title = title;
    }
  }

  public Stream<String> getPages(boolean filter) {
    return this.pages.stream().map(x -> x.get(filter));
  }

  public RoutingTableBookContent withReplacedPages(List<Filterable<String>> pages) {
    return new RoutingTableBookContent(pages, this.author, this.title);
  }

  public List<Filterable<String>> pages() {
    return this.pages;
  }

  @Override
  public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer,
      TooltipFlag tooltipFlag) {
    if (!author.isEmpty()) {
      consumer.accept(Component.translatable(Translations.Tips.ROUTING_TABLE_BOOK_LAST_EDIT, author)
          .withStyle(ChatFormatting.GRAY));
    }
  }
}
