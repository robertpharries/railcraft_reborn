package mods.railcraft.world.level.block.entity.signal;

import javax.annotation.Nullable;
import mods.railcraft.api.signals.SignalReceiver;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.SignalControllerNetwork;
import mods.railcraft.api.signals.SimpleSignalReceiverNetwork;
import mods.railcraft.plugins.PowerPlugin;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

public class SignalReceiverBoxBlockEntity extends ActionSignalBoxBlockEntity
    implements SignalReceiver, IAspectProvider, SignalEmitter {

  private static final int FORCED_UPDATE = 512;
  private final SimpleSignalReceiverNetwork receiver =
      new SimpleSignalReceiverNetwork(this, this::syncToClient);

  public SignalReceiverBoxBlockEntity() {
    super(RailcraftBlockEntityTypes.SIGNAL_RECEIVER_BOX.get());
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level.isClientSide()) {
      receiver.tickClient();
      return;
    }
    receiver.tickServer();
    if (this.clock(FORCED_UPDATE)) {
      updateNeighbors();
      syncToClient();
    }
  }

  @Override
  public void onControllerAspectChange(SignalControllerNetwork con, SignalAspect aspect) {
    this.updateNeighbors();
    this.syncToClient();
  }

  @Override
  public void updateNeighbors() {
    super.updateNeighbors();
    this.updateNeighborBoxes();
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.put("network", receiver.serializeNBT());
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    receiver.deserializeNBT(data.getCompound("network"));
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    receiver.writeSyncData(data);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    receiver.readSyncData(data);
  }

  @Override
  public int getSignal(Direction side) {
    TileEntity tile = this.level.getBlockEntity(this.getBlockPos().relative(side.getOpposite()));
    if (tile instanceof AbstractSignalBoxBlockEntity)
      return PowerPlugin.NO_POWER;
    return isEmittingRedstone(side) ? PowerPlugin.FULL_POWER : PowerPlugin.NO_POWER;
  }

  @Override
  public boolean isEmittingRedstone(Direction side) {
    return doesActionOnAspect(receiver.getAspect());
  }

  @Override
  public void doActionOnAspect(SignalAspect aspect, boolean trigger) {
    super.doActionOnAspect(aspect, trigger);
    updateNeighbors();
  }

  @Override
  public SignalAspect getBoxSignalAspect(@Nullable Direction side) {
    return receiver.getAspect();
  }

  @Override
  public SimpleSignalReceiverNetwork getSignalReceiverNetwork() {
    return receiver;
  }

  @Override
  public SignalAspect getTriggerAspect() {
    return getBoxSignalAspect(null);
  }

  @Override
  public boolean canTransferAspect() {
    return true;
  }
}
