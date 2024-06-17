package mods.railcraft.world.item.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.core.RecipeJsonKeys;
import mods.railcraft.data.recipes.builders.RollingRecipeBuilder;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;

public class RollingRecipe implements Recipe<CraftingInput> {

  private final ShapedRecipePattern pattern;
  private final ItemStack result;
  private final int processTime;

  public RollingRecipe(ShapedRecipePattern pattern, ItemStack result, int processTime) {
    this.pattern = pattern;
    this.result = result;
    this.processTime = processTime;
  }

  /**
   * Get how long the user should wait before this gets crafted.
   *
   * @return tick cost, in int.
   */
  public int getProcessTime() {
    return this.processTime;
  }

  public int getWidth() {
    return this.pattern.width();
  }

  public int getHeight() {
    return this.pattern.height();
  }

  @Override
  public boolean matches(CraftingInput inventory, Level level) {
    return this.pattern.matches(inventory);
  }

  @Override
  public ItemStack assemble(CraftingInput inventory, HolderLookup.Provider provider) {
    return this.getResultItem(provider).copy();
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width >= this.pattern.width() && height >= this.pattern.height();
  }

  @Override
  public ItemStack getResultItem(HolderLookup.Provider provider) {
    return this.result;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return this.pattern.ingredients();
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RailcraftRecipeSerializers.ROLLING.get();
  }

  @Override
  public RecipeType<?> getType() {
    return RailcraftRecipeTypes.ROLLING.get();
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public ItemStack getToastSymbol() {
    return new ItemStack(RailcraftBlocks.MANUAL_ROLLING_MACHINE.get());
  }

  public static class Serializer implements RecipeSerializer<RollingRecipe> {

    private static final MapCodec<RollingRecipe> CODEC =
        RecordCodecBuilder.mapCodec(instance -> instance.group(
            ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
            ItemStack.CODEC.fieldOf(RecipeJsonKeys.RESULT)
                .forGetter(recipe -> recipe.result),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf(RecipeJsonKeys.PROCESS_TIME,
                    RollingRecipeBuilder.DEFAULT_PROCESSING_TIME)
                .forGetter(recipe -> recipe.processTime)
        ).apply(instance, RollingRecipe::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, RollingRecipe> STREAM_CODEC =
        StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

    @Override
    public MapCodec<RollingRecipe> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, RollingRecipe> streamCodec() {
      return STREAM_CODEC;
    }

    private static RollingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
      var pattern = ShapedRecipePattern.STREAM_CODEC.decode(buffer);
      int processTime = buffer.readVarInt();
      var result = ItemStack.STREAM_CODEC.decode(buffer);
      return new RollingRecipe(pattern, result, processTime);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, RollingRecipe recipe) {
      ShapedRecipePattern.STREAM_CODEC.encode(buffer, recipe.pattern);
      buffer.writeVarInt(recipe.processTime);
      ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
    }
  }
}
