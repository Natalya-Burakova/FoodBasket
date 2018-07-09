package product.category;

public class MilkProducts implements Category {

    private static final MilkProducts NEW_INSTANCE = new MilkProducts();
    private static final String NAME_CATEGORY = "Milk";

    private MilkProducts(){}

    public static Category getInstance() {
        return NEW_INSTANCE;
    }

    @Override
    public String getName() {
        return NAME_CATEGORY;
    }

}
