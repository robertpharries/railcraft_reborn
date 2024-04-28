package mods.railcraft.world.item.enchantment;

import java.lang.ref.WeakReference;
import java.util.function.Predicate;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.Predicates;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;

public class RailcraftDamageEnchantment extends Enchantment {

  private final Predicate<? super Entity> check;
  private final float damageBonusPerLevel;
  private WeakReference<Entity> target;

  //TODO: Check if this is the correct tag
  public RailcraftDamageEnchantment(int baseEnchantability, int levelEnchantability,
      int thresholdEnchantability, float damageBonusPerLevel, Predicate<? super Entity> check) {
    super(Enchantment.definition(RailcraftTags.Items.CROWBAR,
        1, 5, Enchantment.dynamicCost(baseEnchantability, levelEnchantability),
        Enchantment.dynamicCost(baseEnchantability + thresholdEnchantability, levelEnchantability),
        4, EquipmentSlot.MAINHAND));
    this.damageBonusPerLevel = damageBonusPerLevel;
    this.check = check;
  }

  public RailcraftDamageEnchantment(int baseEnchantability, int levelEnchantability,
      int thresholdEnchantability, float damageBonusPerLevel) {
    this(baseEnchantability, levelEnchantability, thresholdEnchantability,
        damageBonusPerLevel, Predicates.alwaysTrue());
  }

  @Override
  public float getDamageBonus(int level, EntityType<?> entityType, ItemStack enchantedItem) {
    float modifier = 0;
    if (target != null && check.test(target.get())) {
      modifier = level * damageBonusPerLevel;
    }
    target = null;
    return modifier;
  }

  @Override
  public boolean checkCompatibility(Enchantment enchantment) {
    return !(enchantment instanceof RailcraftDamageEnchantment)
        && !(enchantment instanceof DamageEnchantment);
  }
}
