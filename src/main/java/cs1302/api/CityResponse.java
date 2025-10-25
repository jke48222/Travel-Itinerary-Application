package cs1302.api;

/**
 * Represents a result in a response from the Cities API. This is
 * used by Gson to create an object from the JSON response body. This class
 * is provided with project's starter code, and the instance variables are
 * intentionally set to package private visibility.
 * @see <a href="https://api-ninjas.com/api/city">Understanding Search Results</a>
 */
public class CityResponse {

    /** The name of the city. */
    public String name;

    /** The latitude of the city. */
    public double latitude;

    /** The longitude of the city. */
    public double longitude;

    /** The country of the city. */
    public String country;

    /** The population of the city. */
    public int population;

}
