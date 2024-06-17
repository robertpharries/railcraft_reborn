/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.carts;

import net.minecraft.world.item.DyeColor;

public interface Paintable {

  default int getPrimaryColor() {
    return getPrimaryDyeColor().getTextureDiffuseColor();
  }

  default int getSecondaryColor() {
    return getSecondaryDyeColor().getTextureDiffuseColor();
  }

  DyeColor getPrimaryDyeColor();

  DyeColor getSecondaryDyeColor();
}
