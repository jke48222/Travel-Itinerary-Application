package cs1302.api;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a result in a response from the Google Map's Photos API. This is
 * used by Gson to create an object from the JSON response body. This class
 * is provided with project's starter code, and the instance variables are
 * intentionally set to package private visibility.
 * @see <a href="https://developers.google.com/maps/documentation/places/web-service/photos">
 * Understanding Search Results</a>
 */
public class LocationPhotos {

    /** The photo reference id of the photo. */
    @SerializedName("photo_reference")
    String photoReference;
}
