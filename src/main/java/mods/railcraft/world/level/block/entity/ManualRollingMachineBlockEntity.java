package mods.railcraft.world.level.block.entity;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.world.inventory.ManualRollingMachineMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ManualRollingMachineBlockEntity extends RailcraftBlockEntity implements MenuProvider {

  private int recipieRequiredTime = 12222222;
  private int currentTick = 0;
  private Runnable callback;
  private boolean shouldFire = false;
  // KEY INFO:
  // 1. required time | 2. currentTick (UNSETTABLE)
  // 3. shouldFire - 1 == true
  protected final ContainerData data = new ContainerData() {
    public int get(int key) {
      return switch (key) {
        case 0 -> ManualRollingMachineBlockEntity.this.recipieRequiredTime;
        case 1 -> ManualRollingMachineBlockEntity.this.currentTick;
        case 2 -> ManualRollingMachineBlockEntity.this.shouldFire ? 1 : 0;
        default -> 0;
      };
    }

    public void set(int key, int value) {
      switch (key) {
        case 0 -> ManualRollingMachineBlockEntity.this.recipieRequiredTime = value;
        case 2 -> {
          if (value != 1) {
            ManualRollingMachineBlockEntity.this.resetProgress();
            break;
          }
          ManualRollingMachineBlockEntity.this.shouldFire = true;
        }
        default -> {
        }
      }
    }

    public int getCount() {
      return 3;
    }
  };

  public ManualRollingMachineBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.MANUAL_ROLLING_MACHINE.get(), blockPos, blockState);
  }

  public boolean updateRollingStatus() {
    if (this.rollingProgress() == 1F) {
      this.shouldFire = false;
      if (callback != null) {
        callback.run();
      }
      return true;
    }
    return false;
  }

  public void setOnFinishedCallback(Runnable callback) {
    this.callback = callback;
  }

  public float rollingProgress() {
    return Mth.clamp((float) this.currentTick / (float) this.recipieRequiredTime, 0.0F, 1.0F);
  }

  public void resetProgress() {
    this.shouldFire = false;
    this.currentTick = 0;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      ManualRollingMachineBlockEntity blockEntity) {
    if (!blockEntity.shouldFire) {
      return;
    }
    blockEntity.currentTick++;
    blockEntity.updateRollingStatus();
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
    return new ManualRollingMachineMenu(containerId, inventory, this.data, this);
  }
}
