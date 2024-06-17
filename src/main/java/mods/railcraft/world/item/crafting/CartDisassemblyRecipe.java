package mods.railcraft.world.item.crafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public abstract class CartDisassemblyRecipe extends CustomRecipe {

  private final Item ingredient;
  private final Item result;

  public CartDisassemblyRecipe(Item ingredient, Item result, CraftingBookCategory category) {
    super(category);
    this.ingredient = ingredient;
    this.result = result;
  }

  @Override
  public boolean matches(CraftingInput craftingInput, Level level) {
    boolean ingredientsMatch = false;
    for (int i = 0; i < craftingInput.size(); i++) {
      var stack = craftingInput.getItem(i);
      if (!ingredientsMatch && stack.is(this.ingredient)) {
        ingredientsMatch = true;
      }
    }
    return craftingInput.ingredientCount() == 1 && ingredientsMatch;
  }

  @Override
  public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
    return this.getResultItem(provider).copy();
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width >= 1 && height >= 1;
  }

  @Override
  public ItemStack getResultItem(HolderLookup.Provider provider) {
    return new ItemStack(this.result);
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    NonNullList<Ingredient> ingredients = NonNullList.create();
    ingredients.add(Ingredient.of(this.ingredient));
    return ingredients;
  }

  @Override
  public NonNullList<ItemStack> getRemainingItems(CraftingInput craftingInput) {
    var grid = NonNullList.withSize(craftingInput.size(), ItemStack.EMPTY);
    for (int i = 0; i < craftingInput.size(); i++) {
      var itemStack = craftingInput.getItem(i);
      if (itemStack.is(this.ingredient)) {
        grid.set(i, new ItemStack(Items.MINECART));
      }
    }
    return grid;
  }
}
