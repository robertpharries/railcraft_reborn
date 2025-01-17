package mods.railcraft.world.module;

import mods.railcraft.api.core.NetworkSerializable;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.common.util.INBTSerializable;

/**
 * Similar to an entity component system, the module system is intended for modularisation of logic
 * for reuse, organisation and compositional purposes. It is preferred to put logic into a module
 * rather than using inheritance as that is limited by the fact you can only extend a single class.
 *
 * @author Sm0keySa1m0n
 */
public interface Module extends NetworkSerializable, INBTSerializable<CompoundTag> {

  ModuleProvider getProvider();

  void serverTick();

  @Override
  default void writeToBuf(RegistryFriendlyByteBuf out) {}

  @Override
  default void readFromBuf(RegistryFriendlyByteBuf in) {}

  @Override
  default CompoundTag serializeNBT(HolderLookup.Provider provider) {
    return new CompoundTag();
  }

  @Override
  default void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {}
}
