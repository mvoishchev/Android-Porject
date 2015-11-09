package database;

/**
 * Created by MG on 10/28/2015.
 */
public class RecipeDB {

    private int _id;
    private String _recipename;

    public RecipeDB() {}

    public RecipeDB(String recipename) {
        this._recipename = recipename;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_recipename(String _recipename) {
        this._recipename = _recipename;
    }

    public int get_id() {
        return _id;
    }

    public String get_recipename() {
        return _recipename;
    }

}
