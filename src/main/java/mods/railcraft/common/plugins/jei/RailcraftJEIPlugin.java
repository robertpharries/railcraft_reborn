/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2017
 http://railcraft.info

 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at http://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/

package mods.railcraft.common.plugins.jei;

import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mods.railcraft.api.crafting.ICokeOvenRecipe;
import mods.railcraft.common.blocks.RailcraftBlocks;
import mods.railcraft.common.blocks.machine.equipment.EquipmentVariant;
import mods.railcraft.common.blocks.tracks.outfitted.ItemTrackOutfitted;
import mods.railcraft.common.gui.containers.ContainerCokeOven;
import mods.railcraft.common.items.RailcraftItems;
import mods.railcraft.common.plugins.forge.LocalizationPlugin;
import mods.railcraft.common.plugins.jei.cokeoven.CokeOvenCategory;
import mods.railcraft.common.plugins.jei.cokeoven.CokeOvenRecipeMaker;
import mods.railcraft.common.plugins.jei.cokeoven.CokeOvenWrapper;
import mods.railcraft.common.plugins.jei.crafting.FluidRecipeInterpreter;
import mods.railcraft.common.plugins.jei.crafting.ShapedFluidRecipeWrapper;
import mods.railcraft.common.plugins.jei.crafting.ShapelessFluidRecipeWrapper;
import mods.railcraft.common.plugins.jei.rolling.RollingMachineRecipeCategory;
import mods.railcraft.common.util.crafting.CokeOvenCraftingManager;
import mods.railcraft.common.util.crafting.ShapedFluidRecipe;
import mods.railcraft.common.util.crafting.ShapelessFluidRecipe;
import mods.railcraft.common.util.inventory.InvTools;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by CovertJaguar on 10/7/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
@JEIPlugin
public class RailcraftJEIPlugin implements IModPlugin {
    public static final String ROLLING = "railcraft.rolling";
    public static final String BLAST_FURNACE = "railcraft.blast.furnace";
    public static final String ROCK_CRUSHER = "railcraft.rock.crusher";
    public static final String COKE = "railcraft.coke";

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {
    }

    @Override
    public void register(IModRegistry registry) {
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        FluidRecipeInterpreter.init(registry.getJeiHelpers().getStackHelper(), registry.getIngredientRegistry());
        registry.handleRecipes(ShapedFluidRecipe.class, ShapedFluidRecipeWrapper::new, VanillaRecipeCategoryUid.CRAFTING);
        registry.handleRecipes(ShapelessFluidRecipe.class, ShapelessFluidRecipeWrapper::new, VanillaRecipeCategoryUid.CRAFTING);

        registry.addRecipes(CokeOvenRecipeMaker.getCokeOvenRecipe(registry), COKE);

        registry.addRecipeCatalyst(RailcraftBlocks.STEAM_OVEN.getStack(), VanillaRecipeCategoryUid.SMELTING);
        registry.addRecipeCatalyst(RailcraftBlocks.COKE_OVEN.getStack(), COKE);
        registry.addRecipeCatalyst(EquipmentVariant.ROLLING_MACHINE_MANUAL.getStack(), ROLLING);
        registry.addRecipeCatalyst(EquipmentVariant.ROLLING_MACHINE_POWERED.getStack(), ROLLING);
        registry.addRecipeCatalyst(RailcraftBlocks.BLAST_FURNACE.getStack(), BLAST_FURNACE);
        registry.addRecipeCatalyst(RailcraftBlocks.ROCK_CRUSHER.getStack(), ROCK_CRUSHER);

        jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(RailcraftItems.CROWBAR_SEASONS);
        jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(RailcraftItems.BLEACHED_CLAY);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        registry.addRecipeCategories(new RollingMachineRecipeCategory(guiHelper));
        registry.addRecipeCategories(new CokeOvenCategory(guiHelper));
    }

//    @Override
//    public void register(IModRegistry registry) {
//        IJeiHelpers jeiHelpers = registry.getJeiHelpers();
//        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
//        FluidRecipeInterpreter.init(jeiHelpers.getStackHelper(), registry.getIngredientRegistry());
//        registry.handleRecipes();
//        addRecipeHandlers(new RollingMachineRecipeHandler(jeiHelpers));
//        registry.addRecipeHandlers(new ShapedFluidRecipeHandler());
//        registry.addRecipeHandlers(new ShapelessFluidRecipeHandler(jeiHelpers));
//
//        registry.addRecipeClickArea(GuiRollingMachine.class, 90, 45, 23, 9, ROLLING);
//        registry.addRecipeClickArea(GuiRollingMachinePowered.class, 90, 36, 23, 9, ROLLING);
//
//        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
//        recipeTransferRegistry.addRecipeTransferHandler(ContainerRollingMachine.class, ROLLING, 2, 9, 11, 36);
//        recipeTransferRegistry.addRecipeTransferHandler(ContainerRollingMachinePowered.class, ROLLING, 2, 9, 11, 36);
//
//        boolean rolling = false;
//        ItemStack stack = RailcraftBlocks.EQUIPMENT.getStack(EquipmentVariant.ROLLING_MACHINE_MANUAL);
//        if (!InvTools.isEmpty(stack)) {
//            registry.addRecipeCatalyst(stack, ROLLING);
//            rolling = true;
//        }
//        stack = RailcraftBlocks.EQUIPMENT.getStack(EquipmentVariant.ROLLING_MACHINE_POWERED);
//        if (!InvTools.isEmpty(stack)) {
//            registry.addRecipeCatalyst(stack, ROLLING);
//            rolling = true;
//        }
//
//        if (rolling)
//            registry.addRecipes(RollingMachineRecipeMaker.getRecipes(registry.getJeiHelpers()));
//
//        RailcraftObjects.processBlockVariants((block, variant) -> addDescription(registry, block.getStack(variant)));
//        RailcraftObjects.processItemVariants((item, variant) -> addDescription(registry, item.getStack(variant)));
//    }

    private void addDescription(IModRegistry registry, ItemStack stack) {
        if (!InvTools.isEmpty(stack)) {
            String locTag = stack.getUnlocalizedName() + ".desc";
            if (LocalizationPlugin.hasTag(locTag))
                registry.addIngredientInfo(stack, ItemStack.class, locTag);
        }
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
        Item trackOutfitted = RailcraftBlocks.TRACK_OUTFITTED.item();
        if (trackOutfitted != null)
            subtypeRegistry.registerSubtypeInterpreter(trackOutfitted, stack -> ((ItemTrackOutfitted) stack.getItem()).getSuffix(stack));
    }
}
