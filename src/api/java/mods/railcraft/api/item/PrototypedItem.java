package mods.railcraft.api.item;

import org.apache.commons.lang3.NotImplementedException;
import net.minecraft.world.item.ItemStack;

/**
 * This interface marks an item that can have another item "added" to its NBT. Filter Items and Tank
 * Carts and Cargo Carts all do this. The benefit is that PrototypeRecipe can then be used to set
 * the prototype item.
 */
public interface PrototypedItem {

  default boolean isValidPrototype(ItemStack stack) {
    return true;
  }

  default ItemStack setPrototype(ItemStack filter, ItemStack prototype) {
    //FIXME
    filter = filter.copy();
    /*CompoundTag tag = new CompoundTag();
    prototype.save(tag);
    filter.addTagElement("prototype", tag);*/
    throw new NotImplementedException();
    //return filter;
  }

  default ItemStack getPrototype(ItemStack stack) {
    //FIXME
    throw new NotImplementedException();
    /*CompoundTag tag = stack.getTagElement("prototype");
    return tag == null ? ItemStack.EMPTY : ItemStack.of(tag);*/
  }
}
