package product.category;

public class MakeCategory {
    public static Category makeCategory(String category) {
        if (category.equals("Bakery"))
            return BakeryProducts.getInstance();
        else if (category.equals("Confectionery"))
            return ConfectioneryProducts.getInstance();
        else if (category.equals("Fish"))
            return FishProducts.getInstance();
        else if (category.equals("Fruits and Vegetables"))
            return FruitsAndVegetablesProducts.getInstance();
        else if (category.equals("Grits"))
            return GritsProducts.getInstance();
        else if (category.equals("Meat"))
            return MeatProducts.getInstance();
        else if (category.equals("Milk"))
            return MilkProducts.getInstance();
        else
            return SpiceProducts.getInstance();
    }
}
