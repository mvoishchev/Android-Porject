package connectors.google;

import java.util.List;

/**
 * Created by Matvey on 11/2/2015.
 */
public class GooglePlacesData {

    public List<Result> results;            //
    public class Result{                    //
        public Geometry geometry;
        public String name;
        public String vicinity;
        //

    }
    public class Geometry{                  //
        public Location location;           //
    }
    public class Location{                  //
        public double lat;                  //
        public double lng;                  //
    }
}