package mods.railcraft.world.inventory;

import mods.railcraft.gui.widget.FluidGaugeWidget;
import mods.railcraft.util.inventory.filters.StackFilters;
import mods.railcraft.world.level.block.entity.FluidManipulatorBlockEntity;
import net.minecraft.entity.player.PlayerInventory;

public class FluidManipulatorMenu extends ManipulatorMenu<FluidManipulatorBlockEntity> {

  private final FluidGaugeWidget fluidGauge;

  public FluidManipulatorMenu(int id, PlayerInventory inventory,
      FluidManipulatorBlockEntity manipulator) {
    super(RailcraftMenuTypes.FLUID_MANIPULATOR.get(), id, inventory, manipulator);
    this.addWidget(this.fluidGauge =
        new FluidGaugeWidget(manipulator.getTankManager().get(0), 17, 21, 176, 0, 16, 47));
  }

  public FluidGaugeWidget getFluidGauge() {
    return this.fluidGauge;
  }

  @Override
  protected void addSlots(FluidManipulatorBlockEntity manipulator) {
    this.addSlot(new SlotFluidFilter(manipulator.getFluidFilter(), 0, 116, 26));
    this.addSlot(new SlotStackFilter(StackFilters.FLUID_CONTAINER, manipulator, 0, 152, 26));
    this.addSlot(new SlotOutput(manipulator, 1, 152, 62));
    this.addSlot(new SlotOutput(manipulator, 2, 116, 62));
  }
}