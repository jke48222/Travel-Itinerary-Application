package cs1302.api;

/**
 * Represents a result in a response from the Google Maps Text Search API. This is
 * used by Gson to create an object from the JSON response body. This class
 * is provided with project's starter code, and the instance variables are
 * intentionally set to package private visibility.
 * @see <a href="https://developers.google.com/maps/documentation/places/web-service/search-text">
 * Understanding Search Results</a>
 */
public class LocationResponse {

    /** An array of Location Results. */
    public LocationResults[] results;
}
