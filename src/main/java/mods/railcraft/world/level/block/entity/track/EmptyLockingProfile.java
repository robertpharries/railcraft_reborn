package mods.railcraft.world.level.block.entity.track;

import mods.railcraft.world.level.block.track.outfitted.LockingModeController;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public enum EmptyLockingProfile implements LockingModeController {

  INSTANCE;

  @Override
  public CompoundTag serializeNBT(HolderLookup.Provider provider) {
    return new CompoundTag();
  }

  @Override
  public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {}

  @Override
  public void locked(AbstractMinecart cart) {}

  @Override
  public void passed(AbstractMinecart cart) {}

  @Override
  public void released(AbstractMinecart cart) {}
}
