package analysis.json;

import com.google.gson.Gson;

public class Validator {

    private Validator() {}

    public static boolean validate(String value){
        Gson gson = new Gson();
        try {
            if (value!=null){
                gson.fromJson(value, Object.class);
                return true;
            }
            else
                return false;
        } catch(Exception ex) {
            return false;
        }
    }

}
