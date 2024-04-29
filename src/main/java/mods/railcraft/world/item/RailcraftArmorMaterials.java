package mods.railcraft.world.item;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.tags.RailcraftTags;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

public class RailcraftArmorMaterials {

  public static final Holder<ArmorMaterial> OVERALLS = register("overalls",
      Util.make(new EnumMap<>(ArmorItem.Type.class), defense -> {
        defense.put(ArmorItem.Type.BOOTS, 1);
        defense.put(ArmorItem.Type.LEGGINGS, 2);
        defense.put(ArmorItem.Type.CHESTPLATE, 3);
        defense.put(ArmorItem.Type.HELMET, 1);
        defense.put(ArmorItem.Type.BODY, 3);
      }), 8, SoundEvents.ARMOR_EQUIP_LEATHER, 0, 0,
      () -> Ingredient.of(Blocks.BLUE_WOOL));

  public static final Holder<ArmorMaterial> GOGGLES = register("goggles",
      Util.make(new EnumMap<>(ArmorItem.Type.class), defense -> {
        defense.put(ArmorItem.Type.BOOTS, 1);
        defense.put(ArmorItem.Type.LEGGINGS, 2);
        defense.put(ArmorItem.Type.CHESTPLATE, 3);
        defense.put(ArmorItem.Type.HELMET, 1);
        defense.put(ArmorItem.Type.BODY, 3);
      }), 8, SoundEvents.ARMOR_EQUIP_LEATHER, 0, 0,
      () -> Ingredient.of(RailcraftTags.Items.STEEL_INGOT));

  public static final Holder<ArmorMaterial> STEEL = register("steel",
      Util.make(new EnumMap<>(ArmorItem.Type.class), defense -> {
        defense.put(ArmorItem.Type.BOOTS, 2);
        defense.put(ArmorItem.Type.LEGGINGS, 5);
        defense.put(ArmorItem.Type.CHESTPLATE, 6);
        defense.put(ArmorItem.Type.HELMET, 2);
        defense.put(ArmorItem.Type.BODY, 5);
      }), 8, SoundEvents.ARMOR_EQUIP_IRON, 0.8F, 0,
      () -> Ingredient.of(RailcraftTags.Items.STEEL_INGOT));

  private static Holder<ArmorMaterial> register(String name,
      EnumMap<ArmorItem.Type, Integer> defense, int enchantmentValue,
      Holder<SoundEvent> equipSound, float toughness, float knockbackResistance,
      Supplier<Ingredient> repairIngredient) {
    var layers = List.of(new ArmorMaterial.Layer(RailcraftConstants.rl(name)));
    return register(name, defense, enchantmentValue, equipSound, toughness, knockbackResistance, repairIngredient, layers);
  }

  private static Holder<ArmorMaterial> register(String name,
      EnumMap<ArmorItem.Type, Integer> defense, int enchantmentValue,
      Holder<SoundEvent> equipSound, float toughness, float knockbackResistance,
      Supplier<Ingredient> repairIngredient, List<ArmorMaterial.Layer> layers) {
    var enummap = new EnumMap<ArmorItem.Type, Integer>(ArmorItem.Type.class);
    for (var type : ArmorItem.Type.values()) {
      enummap.put(type, defense.get(type));
    }
    return Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL, RailcraftConstants.rl(name),
        new ArmorMaterial(enummap, enchantmentValue, equipSound, repairIngredient, layers, toughness, knockbackResistance));
  }
}
