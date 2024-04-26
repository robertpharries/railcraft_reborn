package mods.railcraft.world.item;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import com.mojang.authlib.GameProfile;
import mods.railcraft.Translations;
import mods.railcraft.api.item.Filter;
import mods.railcraft.api.item.MinecartFactory;
import mods.railcraft.world.item.component.LocomotiveColorComponent;
import mods.railcraft.world.item.component.LocomotiveOwnerComponent;
import mods.railcraft.world.item.component.LocomotiveWhistlePitchComponent;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class LocomotiveItem extends CartItem implements Filter {

  private final DyeColor defaultPrimary;
  private final DyeColor defaultSecondary;

  public LocomotiveItem(MinecartFactory minecartPlacer, DyeColor primary, DyeColor secondary,
      Properties properties) {
    super(minecartPlacer, properties);
    this.defaultPrimary = primary;
    this.defaultSecondary = secondary;
  }

  @Override
  public boolean matches(ItemStack matcher, ItemStack target) {
    return target.is(this) && getColor(matcher).equals(getColor(target));
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip,
      TooltipFlag adv) {
    var owner = getOwner(stack);
    if (owner != null && StringUtils.isNotBlank(owner.getName())) {
      tooltip.add(Component.translatable(Translations.Tips.LOCOMOTIVE_ITEM_OWNER)
          .withStyle(ChatFormatting.AQUA)
          .append(" ")
          .append(Component.literal(owner.getName()).withStyle(ChatFormatting.GRAY)));
    }

    tooltip.add(Component.translatable(Translations.Tips.LOCOMOTIVE_ITEM_PRIMARY)
        .withStyle(ChatFormatting.AQUA)
        .append(" ")
        .append(Component.translatable("color.minecraft." + getColor(stack).primary().getName())
        .withStyle(ChatFormatting.GRAY)));

    tooltip.add(Component.translatable(Translations.Tips.LOCOMOTIVE_ITEM_SECONDARY)
        .withStyle(ChatFormatting.AQUA)
        .append(" ")
        .append(Component.translatable("color.minecraft." + getColor(stack).secondary().getName())
        .withStyle(ChatFormatting.GRAY)));

    float whistle = getWhistlePitch(stack);
    if (whistle < 0) {
      tooltip.add(Component.translatable(Translations.Tips.LOCOMOTIVE_ITEM_NO_WHISTLE)
          .withStyle(ChatFormatting.GRAY));
    } else {
      tooltip.add(Component.translatable(Translations.Tips.LOCOMOTIVE_ITEM_WHISTLE)
          .withStyle(ChatFormatting.AQUA)
          .append(" ")
          .append(Component.literal(String.format("%.2f", whistle))
              .withStyle(ChatFormatting.GRAY)));
    }
  }

  public static void setItemColorData(ItemStack stack, DyeColor primaryColor,
      DyeColor secondaryColor) {
    stack.set(RailcraftDataComponents.LOCOMOTIVE_COLOR, new LocomotiveColorComponent(primaryColor, secondaryColor));
  }

  public static void setItemWhistleData(ItemStack stack, float whistlePitch) {
    stack.set(RailcraftDataComponents.LOCOMOTIVE_WHISTLE_PITCH,
        new LocomotiveWhistlePitchComponent(whistlePitch));
  }

  public static float getWhistlePitch(ItemStack stack) {
    return stack.getOrDefault(RailcraftDataComponents.LOCOMOTIVE_WHISTLE_PITCH, new LocomotiveWhistlePitchComponent(-1)).whistlePitch();
  }

  public static void setOwnerData(ItemStack stack, GameProfile owner) {
    stack.set(RailcraftDataComponents.LOCOMOTIVE_OWNER, new LocomotiveOwnerComponent(owner));
  }

  @Nullable
  public static GameProfile getOwner(ItemStack stack) {
    if (stack.has(RailcraftDataComponents.LOCOMOTIVE_OWNER)) {
      return stack.get(RailcraftDataComponents.LOCOMOTIVE_OWNER).owner().gameProfile();
    }
    return null;
  }

  public static LocomotiveColorComponent getColor(ItemStack stack) {
    var item = (LocomotiveItem) stack.getItem();
    return stack.getOrDefault(RailcraftDataComponents.LOCOMOTIVE_COLOR,
        new LocomotiveColorComponent(item.defaultPrimary, item.defaultSecondary));
  }
}
