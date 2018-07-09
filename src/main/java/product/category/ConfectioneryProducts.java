package product.category;

//кондитерские
public class ConfectioneryProducts implements Category{

    private static final ConfectioneryProducts NEW_INSTANCE = new ConfectioneryProducts();
    private static final String NAME_CATEGORY = "Confectionery";

    private ConfectioneryProducts(){}

    public static Category getInstance() {
        return NEW_INSTANCE;
    }

    @Override
    public String getName() {
        return NAME_CATEGORY;
    }


}
