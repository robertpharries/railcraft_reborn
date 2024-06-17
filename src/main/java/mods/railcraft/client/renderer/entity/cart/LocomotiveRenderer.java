package mods.railcraft.client.renderer.entity.cart;

import mods.railcraft.season.Seasons;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.DyeColor;

public abstract class LocomotiveRenderer<T extends Locomotive>
    extends CustomMinecartRenderer<T> {

  public LocomotiveRenderer(EntityRendererProvider.Context context) {
    super(context);
  }

  protected int getPrimaryColor(T loco) {
    return Seasons.isGhostTrain(loco)
        ? DyeColor.LIGHT_GRAY.getTextureDiffuseColor()
        : loco.getPrimaryColor();
  }

  protected int getSecondaryColor(T loco) {
    return Seasons.isGhostTrain(loco)
        ? DyeColor.LIGHT_GRAY.getTextureDiffuseColor()
        : loco.getSecondaryColor();
  }
}
