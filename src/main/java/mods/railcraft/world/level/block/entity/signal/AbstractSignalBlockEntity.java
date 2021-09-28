package mods.railcraft.world.level.block.entity.signal;

import javax.annotation.Nullable;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.world.level.block.entity.RailcraftTickableBlockEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;

public abstract class AbstractSignalBlockEntity extends RailcraftTickableBlockEntity {

  private int prevLightValue;

  public AbstractSignalBlockEntity(TileEntityType<?> type) {
    super(type);
  }

  @Override
  public void tick() {
    super.tick();
    int lightValue = getLightValue();
    if (prevLightValue != lightValue) {
      prevLightValue = lightValue;
      this.level.getLightEngine().checkBlock(worldPosition);
    }
  }

  public int getLightValue() {
    return getSignalAspect().getBlockLight();
  }

  public abstract SignalAspect getSignalAspect();

  @Nullable
  public abstract ITextComponent getPrimaryNetworkName();

  @Override
  public double getViewDistance() {
    return 512.0D;
  }

  @Override
  public AxisAlignedBB getRenderBoundingBox() {
    return TileEntity.INFINITE_EXTENT_AABB;
  }
}
