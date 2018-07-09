package product.category;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class MeatProducts implements Category{

    private static final MeatProducts NEW_INSTANCE = new MeatProducts();
    private static final String NAME_CATEGORY = "Meat";

    private MeatProducts(){}

    public static Category getInstance() {
        return NEW_INSTANCE;
    }

    @Override
    public String getName() {
        return NAME_CATEGORY;
    }

}
