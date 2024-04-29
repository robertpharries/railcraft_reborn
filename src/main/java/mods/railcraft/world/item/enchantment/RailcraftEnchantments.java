package mods.railcraft.world.item.enchantment;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.tags.RailcraftTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftEnchantments {

  private static final DeferredRegister<Enchantment> deferredRegister =
      DeferredRegister.create(BuiltInRegistries.ENCHANTMENT, RailcraftConstants.ID);

  public static final DeferredHolder<Enchantment, RailcraftDamageEnchantment> WRECKING =
      deferredRegister.register("wrecking",
          () -> new RailcraftDamageEnchantment(1, 11, 20, 0.75f));

  public static final DeferredHolder<Enchantment, RailcraftDamageEnchantment> IMPLOSION =
      deferredRegister.register("implosion",
          () -> new RailcraftDamageEnchantment(5, 8, 20, 2.5f, Creeper.class::isInstance));

  public static final DeferredHolder<Enchantment, DestructionEnchantment> DESTRUCTION =
      deferredRegister.register("destruction",
          () -> new DestructionEnchantment(Enchantment.definition(RailcraftTags.Items.CROWBAR,
              1, 3, Enchantment.dynamicCost(5, 10),
              Enchantment.dynamicCost(15, 10), 8, EquipmentSlot.MAINHAND)));

  public static final DeferredHolder<Enchantment, SmackEnchantment> SMACK =
      deferredRegister.register("smack",
          () -> new SmackEnchantment(Enchantment.definition(RailcraftTags.Items.CROWBAR,
              1, 4, Enchantment.dynamicCost(9, 8),
              Enchantment.dynamicCost(29, 8), 8, EquipmentSlot.MAINHAND)));

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}
