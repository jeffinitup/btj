package com.jeffyjamzhd.btj.registry;

import btw.item.BTWItems;
import net.minecraft.src.*;

import java.util.Iterator;
import java.util.List;

public class BTJRecipes {

    public static void register() {
        removeOldRecipes();
        addNewRecipes();
    }
    // kill old recipes
    private static void removeOldRecipes() {
        CraftingManager manager = CraftingManager.getInstance();
        List recipes = manager.getRecipeList();

        Iterator it = recipes.iterator();
        while (it.hasNext()) {
            IRecipe recipe = (IRecipe) it.next();
            ItemStack output = recipe.getRecipeOutput();

            if (output == null) continue;

            if (output.itemID == BTWItems.firePlough.itemID ||
                    output.itemID == Item.flintAndSteel.itemID ||
                         output.itemID == BTWItems.bowDrill.itemID) {

                it.remove();
            }
        }
    }
    // add new recipes
    private static void addNewRecipes() {

        CraftingManager.getInstance().addRecipe(
                new ItemStack(BTWItems.firePlough, 1),
                new Object[] {
                        "YX",
                        'X', Item.flint,
                        'Y', BTWItems.stone
                }
        );

        CraftingManager.getInstance().addRecipe(
                new ItemStack(BTWItems.bowDrill, 1),
                new Object[] {
                        "XX",
                        'X', Item.flint
                }
        );

        CraftingManager.getInstance().addRecipe(
                new ItemStack(Item.flintAndSteel, 1),
                new Object[] {
                        "YX",
                        'X', Item.flint,
                        'Y', BTWItems.steelNugget
                }
        );





    }
}


