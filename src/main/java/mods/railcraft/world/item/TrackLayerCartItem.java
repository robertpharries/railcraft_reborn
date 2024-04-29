package mods.railcraft.world.item;

import java.util.List;
import mods.railcraft.Translations;
import mods.railcraft.world.entity.vehicle.TrackLayer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class TrackLayerCartItem extends CartItem {

  public TrackLayerCartItem(Properties properties) {
    super(TrackLayer::new, properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      List<Component> tooltipComponents, TooltipFlag isAdvanced) {
    super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
    tooltipComponents.add(Component.translatable(Translations.Tips.TRACK_LAYER)
        .withStyle(ChatFormatting.GRAY));
  }
}
