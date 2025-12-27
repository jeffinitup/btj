package com.jeffyjamzhd.btj.registry;

import btw.item.BTWItems;
import net.minecraft.src.*;

import java.util.*;
import java.util.stream.Collectors;

public class BTJRecipes {
    private static final HashSet<Integer> TO_REMOVE;

    public static void register() {
        removeOldRecipes();
        addNewRecipes();
    }

    /**
     * Remove recipes that have outputs defined in {@code TO_REMOVE}
     */
    private static void removeOldRecipes() {
        CraftingManager manager = CraftingManager.getInstance();
        List<? extends IRecipe> recipes = manager.getRecipeList();

        // Iterate and filter recipes targeted for removal
        Set<? extends IRecipe> set = recipes.stream()
                .filter(Objects::nonNull)
                .filter(iRecipe -> iRecipe.getRecipeOutput() != null)
                .collect(Collectors.toSet());
        // Remove recipes
        set.forEach(recipes::remove);
    }

    /**
     * Add BTJ recipes
     */
    private static void addNewRecipes() {
        CraftingManager.getInstance().addShapelessRecipe(
                new ItemStack(BTWItems.firePlough, 1), Item.flint, BTWItems.stone);
        CraftingManager.getInstance().addShapelessRecipe(
                new ItemStack(BTWItems.bowDrill, 1), Item.flint, Item.flint);
        CraftingManager.getInstance().addShapelessRecipe(
                new ItemStack(Item.flintAndSteel, 1), BTWItems.steelNugget, Item.flint);
    }

    static {
        TO_REMOVE = new HashSet<>();

        // These outputs are removed from standard crafting grid recipes
        TO_REMOVE.add(Item.flintAndSteel.itemID);
        TO_REMOVE.add(BTWItems.firePlough.itemID);
        TO_REMOVE.add(BTWItems.bowDrill.itemID);
    }
}


