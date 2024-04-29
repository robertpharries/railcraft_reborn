package mods.railcraft.world.item;

import java.util.List;
import mods.railcraft.Translations.Tips;
import mods.railcraft.api.charge.ChargeProtectionItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class OverallsItem extends ArmorItem implements ChargeProtectionItem {

  public OverallsItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
    super(material, type, properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      List<Component> tooltipComponents, TooltipFlag isAdvanced) {
    super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
    tooltipComponents.add(Component.translatable(Tips.OVERALLS).withStyle(ChatFormatting.GRAY));
  }
}
