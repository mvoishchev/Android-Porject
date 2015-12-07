package connectors.google;

import java.util.List;

/**
 * Created by Matvey Voishchev on 11/2/2015.
 * This class deals with the Response from Google Places API
 * Breakdown is based on Json file setup
 */
public class GooglePlacesData {

    /**Results is the array that was returned*/
    public List<Result> results;            //

    /**Result is separated in geometry parameters
     * Name and Vicinity
     * Price Level and Rating
     */
    public class Result{                    //
        public Geometry geometry;
        public String name;
        public String vicinity;
        public int price_level;
        public double rating;
        //

    }

    /**
     * Geometry class has Location class
     */
    public class Geometry{                  //
        public Location location;           //
    }

    /**
     * Geometry class has two values
     * Latitude and Longitude
     */
    public class Location{                  //
        public double lat;                  //
        public double lng;                  //
    }
}