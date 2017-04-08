/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2017
 http://railcraft.info

 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at http://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/
package mods.railcraft.common.blocks.ore;

import mods.railcraft.common.blocks.machine.RailcraftBlockMetadata;
import mods.railcraft.common.items.Metal;
import mods.railcraft.common.plugins.forge.CraftingPlugin;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
@RailcraftBlockMetadata(variant = EnumOreMetal.class)
public class BlockOreMetal extends BlockOreMetalBase<EnumOreMetal> {
    public BlockOreMetal() {
        setDefaultState(blockState.getBaseState().withProperty(getVariantProperty(), EnumOreMetal.COPPER));
    }

    @Override
    public void defineRecipes() {
        registerOreRecipe(Metal.COPPER);
        registerOreRecipe(Metal.TIN);
        registerOreRecipe(Metal.LEAD);
        registerOreRecipe(Metal.SILVER);
    }

    private static void registerOreRecipe(Metal metal) {
        CraftingPlugin.addFurnaceRecipe(Metal.Form.ORE.getStack(metal), metal.getStack(Metal.Form.INGOT), 0.7F);
    }
}
