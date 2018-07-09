package product.category;


public class FishProducts implements Category {

    private static final FishProducts NEW_INSTANCE = new FishProducts();
    private static final String NAME_CATEGORY = "Fish";

    private FishProducts(){}

    public static Category getInstance() {
        return NEW_INSTANCE;
    }

    @Override
    public String getName() {
        return NAME_CATEGORY;
    }

}
