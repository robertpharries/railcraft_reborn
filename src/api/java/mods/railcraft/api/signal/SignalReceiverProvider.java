/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signal;

import mods.railcraft.api.core.BlockEntityLike;

public interface SignalReceiverProvider extends BlockEntityLike {

  SignalReceiver getSignalReceiver();
}