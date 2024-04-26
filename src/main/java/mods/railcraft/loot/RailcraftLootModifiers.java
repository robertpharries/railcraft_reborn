package mods.railcraft.loot;

import java.util.function.Supplier;
import com.mojang.serialization.MapCodec;
import mods.railcraft.api.core.RailcraftConstants;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class RailcraftLootModifiers {

  private static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> deferredRegister =
      DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS,
          RailcraftConstants.ID);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  public static final Supplier<MapCodec<? extends IGlobalLootModifier>> DUNGEON_LOOT_MODIFIER =
      deferredRegister.register("dungeon_loot_modifier", () -> DungeonLootModifier.CODEC);
}
