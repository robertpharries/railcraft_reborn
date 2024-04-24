package mods.railcraft.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.core.RecipeJsonKeys;
import mods.railcraft.data.recipes.builders.BlastFurnaceRecipeBuilder;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class BlastFurnaceRecipe extends AbstractCookingRecipe {

  private final int slagOutput;

  public BlastFurnaceRecipe(Ingredient ingredient, ItemStack result,
      float experience, int cookingTime, int slagOutput) {
    super(RailcraftRecipeTypes.BLASTING.get(), "", CookingBookCategory.MISC,
        ingredient, result, experience, cookingTime);
    this.slagOutput = slagOutput;
  }

  public int getSlagOutput() {
    return this.slagOutput;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RailcraftRecipeSerializers.BLASTING.get();
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public ItemStack getToastSymbol() {
    return new ItemStack(RailcraftBlocks.BLAST_FURNACE_BRICKS.get());
  }

  public static class Serializer implements RecipeSerializer<BlastFurnaceRecipe> {

    private static final MapCodec<BlastFurnaceRecipe> CODEC =
        RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC_NONEMPTY.fieldOf(RecipeJsonKeys.INGREDIENT)
                .forGetter(recipe -> recipe.ingredient),
            ItemStack.CODEC.fieldOf(RecipeJsonKeys.RESULT)
                .forGetter(recipe -> recipe.result),
            Codec.FLOAT.fieldOf(RecipeJsonKeys.EXPERIENCE)
                .orElse(0.0F)
                .forGetter(recipe -> recipe.experience),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf(RecipeJsonKeys.COOKING_TIME,
                    BlastFurnaceRecipeBuilder.DEFAULT_COOKING_TIME)
                .forGetter(recipe -> recipe.cookingTime),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf(RecipeJsonKeys.SLAG_OUTPUT, 0)
                .forGetter(recipe -> recipe.slagOutput)
        ).apply(instance, BlastFurnaceRecipe::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, BlastFurnaceRecipe> STREAM_CODEC =
        StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

    @Override
    public MapCodec<BlastFurnaceRecipe> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, BlastFurnaceRecipe> streamCodec() {
      return STREAM_CODEC;
    }

    private static BlastFurnaceRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
      var slagOutput = buffer.readVarInt();
      var cookingTime = buffer.readVarInt();
      var ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
      var result = ItemStack.STREAM_CODEC.decode(buffer);
      var experience = buffer.readFloat();
      return new BlastFurnaceRecipe(ingredient, result, experience, cookingTime, slagOutput);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, BlastFurnaceRecipe recipe) {
      buffer.writeVarInt(recipe.slagOutput);
      buffer.writeVarInt(recipe.cookingTime);
      Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient);
      ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
      buffer.writeFloat(recipe.experience);
    }
  }
}
