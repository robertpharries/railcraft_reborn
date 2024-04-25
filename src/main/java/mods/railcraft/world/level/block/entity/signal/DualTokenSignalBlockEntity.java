package mods.railcraft.world.level.block.entity.signal;

import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalReceiver;
import mods.railcraft.api.signal.SingleSignalReceiver;
import mods.railcraft.api.signal.entity.SignalReceiverEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;

public class DualTokenSignalBlockEntity extends TokenSignalBlockEntity
    implements SignalReceiverEntity, DualSignalBlockEntity {

  private final SingleSignalReceiver signalReceiver = new SingleSignalReceiver(this,
      this::syncToClient, __ -> this.level.getLightEngine().checkBlock(this.getBlockPos()));

  public DualTokenSignalBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.DUAL_TOKEN_SIGNAL.get(), blockPos, blockState);
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (!this.level.isClientSide()) {
      this.signalReceiver.refresh();
    }
  }

  @Override
  public void blockRemoved() {
    super.blockRemoved();
    this.signalReceiver.destroy();
  }

  @Override
  public SignalAspect getSecondarySignalAspect() {
    return this.signalReceiver.getPrimarySignalAspect();
  }

  @Override
  public int getLightValue() {
    return Math.max(super.getLightValue(),
        this.getSecondarySignalAspect().getBlockLight());
  }

  @Override
  public SignalReceiver getSignalReceiver() {
    return this.signalReceiver;
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);
    tag.put(CompoundTagKeys.SIGNAL_RECEIVER, this.signalReceiver.serializeNBT(provider));
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.loadAdditional(tag, provider);
    this.signalReceiver.deserializeNBT(provider, tag.getCompound(CompoundTagKeys.SIGNAL_RECEIVER));
  }

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    this.signalReceiver.writeToBuf(data);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.signalReceiver.readFromBuf(data);
  }
}
