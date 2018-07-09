package product.category;


//специи
public class SpiceProducts implements Category {

    private static final SpiceProducts NEW_INSTANCE = new SpiceProducts();
    private static final String NAME_CATEGORY = "Spice";

    private SpiceProducts(){}

    public static Category getInstance() {
        return NEW_INSTANCE;
    }

    @Override
    public String getName() {
        return NAME_CATEGORY;
    }

}
