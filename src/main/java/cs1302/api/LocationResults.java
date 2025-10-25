package cs1302.api;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a result in a response from the Google Maps Text Search API. This is
 * used by Gson to create an object from the JSON response body. This class
 * is provided with project's starter code, and the instance variables are
 * intentionally set to package private visibility.
 * @see <a href="https://developers.google.com/maps/documentation/places/web-service/search-text">
 * Understanding Search Results</a>
 */
public class LocationResults {

    /** The address of the location. */
    @SerializedName("formatted_address")
    String formattedAddress;

    /** The name of the location. */
    String name;

    /** The price level of the location. */
    @SerializedName("price_level")
    int priceLevel;

    /** The place id of the location. */
    @SerializedName("place_id")
    String placeId;

    /** The rating of the location. */
    double rating;

    /** An array of Location Photos of the location. */
    LocationPhotos[] photos;

}
