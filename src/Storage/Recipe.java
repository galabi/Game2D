package Storage;

public class Recipe {

	int[][] recipe;
	int idOfResult,quantity;
		
	public Recipe(int[][] recipe,int idOfResult,int quantity) {
		this.idOfResult = idOfResult;
		this.recipe = recipe;
		this.quantity = quantity;
		
	}
}

