/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.charge;

import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Implemented by items like the Overalls to prevent Charge based damage.
 */
public interface ChargeProtectionItem {

  /**
   * Called to determine if the item can currently provide protection against Charge damage.
   *
   * @param owner owner of the item
   * @return true if the item is currently providing protection.
   */
  default boolean isZapProtectionActive(ItemStack stack, LivingEntity owner) {
    return true;
  }

  /**
   * Called when charge damage is being done and the target is wearing a Charge protection item. You
   * can block all or just some of the damage to the entity, defaults to all. The damage or other
   * cost done to the item is up to the implementation.
   *
   * @param owner owner of the item
   * @param attackDamage damage to be done to the owner
   * @return A ZapResult object with the resulting stack and damage prevented.
   */
  default ZapResult zap(ItemStack stack, ServerLevel level, LivingEntity owner,
      float attackDamage) {
    AtomicReference<ItemStack> resultStack = new AtomicReference<>(stack);
    if (owner.getRandom().nextInt(150) == 0) {
      stack.hurtAndBreak(1, level, owner, __ -> resultStack.set(ItemStack.EMPTY));
    }
    return new ZapResult(resultStack.get(), attackDamage);
  }

  record ZapResult(ItemStack stack, float damagePrevented) {}
}
