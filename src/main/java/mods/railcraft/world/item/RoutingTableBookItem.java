package mods.railcraft.world.item;

import java.util.function.Predicate;
import mods.railcraft.client.ScreenFactories;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RoutingTableBookItem extends Item {

  public static final Predicate<ItemStack> FILTER =
      stack -> stack != null && stack.getItem() instanceof RoutingTableBookItem;

  public RoutingTableBookItem(Properties properties) {
    super(properties);
  }

  @Override
  public Component getName(ItemStack stack) {
    var name = super.getName(stack);
    var content = stack.get(RailcraftDataComponents.ROUTING_TABLE_BOOK);
    if (content != null) {
      var title = content.title();
      if (title.isPresent()) {
        name = name.copy().append(" - " + title);
      }
    }
    return name;
  }

  @Override
  public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
    return false;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player,
      InteractionHand usedHand) {
    var itemStack = player.getItemInHand(usedHand);
    if (level.isClientSide()) {
      ScreenFactories.openRoutingTableBookScreen(player, itemStack, usedHand);
    }
    return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
  }
}
