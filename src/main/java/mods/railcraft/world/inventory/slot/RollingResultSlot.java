package mods.railcraft.world.inventory.slot;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;

public class RollingResultSlot extends ResultSlot {

  public RollingResultSlot(Player player, CraftingContainer craftSlots, Container container,
      int slot, int xPos, int yPos) {
    super(player, craftSlots, container, slot, xPos, yPos);
  }

  @Override
  public void onTake(Player player, ItemStack stack) {
    this.checkTakeAchievements(stack);
  }
}
