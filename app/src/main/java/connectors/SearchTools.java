package connectors;

/**
 *
 * Class to hold all enumerations for searches.  Ensures that filters are used properly
 * Created by AGCOSTFU on 10/6/2015.
 */
public class SearchTools {


    public enum SEARCH_TYPE{
        ANY_INGREDIENT_IS_PRESENT,
        ALL_INGREDIENTS_PRESENT,
        ONLY_INGREDIENTS_PRESENT;
    }
 /*   //list of possible searchable cuisines are from Spoonacular documentation.
    //TODO: verify that same parameters are used for cuisines in Yummly
    public enum CUISINES{
        AFRICAN("african"),
        CHINESE("chinese"),
        JAPANESE("japanese"),
        KOREAN("korean"),
        VIETNAMESE("vietnamese"),
        THAI("thai"),
        INDIAN("indian"),
        BRITISH("british"),
        IRISH("irish"),
        FRENCH("french"),
        ITALIAN("italian"),
        MEXICAN("mexican"),
        SPANISH("spanish"),
        MIDDLE_EASTERN("middle eastern"),
        JEWISH("jewish"),
        AMERICAN("american"),
        CAJUN("cajun"),
        SOUTHERN("southern"),
        GREEK("greek"),
        GERMAN("german"),
        NORDIC("nordic"),
        EASTERN_EUROPEAN("eastern european"),
        CARIBBEAN("caribbean"),
        LATIN("latin american");

        String search;
        private CUISINES(String search_parameter)
        {
            search = search_parameter;
        }
    }
*/
}
