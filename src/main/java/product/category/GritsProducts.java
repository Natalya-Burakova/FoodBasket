package product.category;

//крупы
public class GritsProducts implements Category {

    private static final GritsProducts NEW_INSTANCE = new GritsProducts();
    private static final String NAME_CATEGORY = "Grits";

    private GritsProducts(){}

    public static Category getInstance() {
        return NEW_INSTANCE;
    }

    @Override
    public String getName() {
        return NAME_CATEGORY;
    }

}

