package Storage;

public class RecipeBook {
	
	static int recipesSize = 5; 
	private static Recipe[] recipes = new Recipe[recipesSize];
	
	//all the recipes
	static {
		recipes[0] = new Recipe(new int[][]{{1,1},{1,1}}, 6,1);
		recipes[1] = new Recipe(new int[][]{{8,9},{8,0}}, 2,1);
		recipes[2] = new Recipe(new int[][]{{8},{8}}, 3,1);
		recipes[3] = new Recipe(new int[][]{{1}}, 8,1);
		recipes[4] = new Recipe(new int[][]{{9,8},{8,9}}, 10,1);

	}
	
	public static Item checkRecipe(int[][] crafting) {
		Item temp;
		for(int i = 0; i < recipesSize;i++) {
			if(recipeCompare(crafting, recipes[i].recipe)) {
				temp = new Item(recipes[i].idOfResult);
				temp.setQuantity(recipes[i].quantity);
				return temp;
			}
		}
		return temp = new Item();
	}
	
	private static boolean recipeCompare(int[][] crafting,int[][] recipe) {
		//The algorithm checks whether a given recipe appears in the crafting table
		int n = recipe.length;
	    int m = recipe[0].length;
	    int N = crafting.length;
	    int M = crafting[0].length;

	    for (int i = 0; i <= N - n; i++) {
	        for (int j = 0; j <= M - m; j++) {
	            boolean match = true;

	            for (int x = 0; x < n; x++) {
	                for (int y = 0; y < m; y++) {
	                    int expected = recipe[x][y];
	                    int actual = crafting[i + x][j + y];

	                    if (expected != 0 && actual != expected) {
	                        match = false;
	                        break;
	                    }
	                    if (expected == 0 && actual != 0) {
	                        match = false;
	                        break;
	                    }
	                }
	                if (!match) break;
	            }

	            if (match) {
	                for (int x = 0; x < N; x++) {
	                    for (int y = 0; y < M; y++) {
	                        boolean insideRecipeArea = x >= i && x < i + n && y >= j && y < j + m;
	                        if (!insideRecipeArea && crafting[x][y] != 0) {
	                            match = false;
	                            break;
	                        }
	                    }
	                    if (!match) break;
	                }
	            }

	            if (match) return true;
	        }
	    }

	    return false;
		}
}
