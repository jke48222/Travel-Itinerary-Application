package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.Priority;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import java.io.IOException;
import java.util.Random;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.control.ProgressIndicator;

/**
 * The {@code ApiApp} class extends JavaFX's {@code Application} and serves as the main class
 * for the location information application. It provides functionality for retrieving data
 * about countries, cities, hotels, activities, attractions, and restaurants from external APIs.
 * The application includes a graphical user interface (GUI) built using JavaFX components.
 *
 * <p>The class includes methods for creating UI elements, loading data from APIs, updating
 * the UI components asynchronously, and initiating background threads for data loading.
 * The application flow is controlled by the {@code go} method, which executes loading
 * operations and transitions between different scenes in the UI.</p>
 *
 * <p>Note: The class assumes the existence of classes such as {@code CountryResponse},
 * {@code CityResponse}, and {@code LocationResponse}, as well as initialized instances
 * of objects like {@code HTTP_CLIENT} and {@code GSON}.</p>
 *
 * @author Jalen Edusei
 * @version 1.0
 * @since 2023-12-10
 */
public class ApiApp extends Application {

    /** HTTP client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)           // uses HTTP protocol version 2 where possible
        .followRedirects(HttpClient.Redirect.NORMAL)  // always redirects, except from HTTPS to HTTP
        .build();                                     // builds and returns a HttpClient object

    /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()                          // enable nice output when printing
        .create();                                    // builds and returns a Gson object

    private static final String COUNTRIES_API = "https://restcountries.com/v3.1/name/";

    private static final String CITIES_API = "https://api.api-ninjas.com/v1/city?limit=20&country=";

    private static final String LOCATIONS_API =
        "https://maps.googleapis.com/maps/api/place/textsearch/json?";

    Stage stage;

    Scene scene1;
    VBox root1;
    Button goButton;

    Scene scene2;
    VBox root2;
    Button backButton;

    Scene loadingScene;
    VBox loadingRoot;
    Label loadingLabel;
    Label spacerLabel;
    ProgressIndicator progressIndicator;

    HBox topHBox;
    HBox inputBox;
    Label titleLabel;
    Label startingLabel;
    Label descriptionLabel;
    ComboBox<Label> countryComboBox;

    GridPane gridPane;
    ColumnConstraints col1;
    ColumnConstraints col2;
    RowConstraints row1;
    RowConstraints row2;

    VBox vBox1;
    VBox vBox2;
    VBox vBox3;
    VBox vBox4;

    String citiesString;
    String countriesString;
    String locationsString1;
    String locationsString2;
    String locationsString3;
    String locationsString4;

    CountryResponse theCountry;
    CityResponse theCity;
    LocationResponse theRestaurant;
    LocationResponse theHotel;
    LocationResponse theAttraction;
    LocationResponse theActivity;

    Button remakeButton;

    String city;

    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root1 = new VBox();
        root2 = new VBox();
        goButton = new Button("Go!");
        backButton = new Button("Back");
        countryComboBox = new ComboBox<>();
        descriptionLabel = new Label("This app will create a unique travel itinerary to a popular"
                                     + " city in the country that you want to travel to.");
        startingLabel = new Label("Travel Itinerary App");
        inputBox = new HBox();
        topHBox = new HBox();
        titleLabel = new Label();
        gridPane = new GridPane();
        col1 = new ColumnConstraints();
        col2 = new ColumnConstraints();
        row1 = new RowConstraints();
        row2 = new RowConstraints();
        vBox1 = new VBox();
        vBox2 = new VBox();
        vBox3 = new VBox();
        vBox4 = new VBox();
        loadingRoot = new VBox();
        loadingLabel = new Label("Loading...");
        spacerLabel = new Label();
        progressIndicator = new ProgressIndicator();
        remakeButton = new Button("Remake");
    } // ApiApp

    /**
 * Initializes the application. Sets up the UI elements and initial values.
 */
    public void init() {

        firstInitialization();
        secondInitialization();

        root2.getChildren().addAll(topHBox, gridPane);
        root2.setPadding(new Insets(7.0));

        addValues();

        goButton.setOnAction(e -> {
            go();
        });
        backButton.setOnAction(e -> switchScenes(scene1));

        remakeButton.setOnAction(e -> {
            go();
        });

        loadingLabel.setAlignment(javafx.geometry.Pos.BOTTOM_CENTER);
        loadingLabel.setPrefHeight(320.0);
        loadingLabel.setPrefWidth(1280.0);
        loadingLabel.setFont(new Font(96.0));

        spacerLabel.setPrefHeight(41.0);
        spacerLabel.setPrefWidth(1374.0);

        progressIndicator.setMaxHeight(Double.MAX_VALUE);
        progressIndicator.setMaxWidth(Double.MAX_VALUE);
        progressIndicator.setPrefHeight(153.0);
        progressIndicator.setPrefWidth(1280.0);

        loadingRoot.getChildren().addAll(loadingLabel, spacerLabel, progressIndicator);
        loadingRoot.setMaxHeight(Double.NEGATIVE_INFINITY);
        loadingRoot.setMaxWidth(Double.NEGATIVE_INFINITY);
        loadingRoot.setMinWidth(Double.NEGATIVE_INFINITY);
        loadingRoot.setMinHeight(Double.NEGATIVE_INFINITY);
        loadingRoot.setPrefHeight(720.0);
        loadingRoot.setPrefWidth(1280.0);
        loadingRoot.setStyle("-fx-background-color: aliceblue;");

    } // init

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        this.stage = stage;

        // setup stage
        stage.setTitle("ApiApp!");

        scene1 = createSceneOne();
        scene2 = createSceneTwo();
        loadingScene = createLoadingScene();

        stage.setScene(scene1);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();

    } // start

    /**
 * Creates the first scene of the application.
 *
 * @return The first scene
 */
    private Scene createSceneOne() {

        scene1 = new Scene(root1, 1280, 720);

        return scene1;
    }

    /**
 * Creates the second scene of the application.
 *
 * @return The second scene
 */
    private Scene createSceneTwo() {

        scene2 = new Scene(root2, 1280, 720);

        return scene2;
    }

    /**
 * Creates the loading scene of the application.
 *
 * @return The loading scene
 */
    private Scene createLoadingScene() {

        loadingScene = new Scene(loadingRoot, 1280, 720);

        return loadingScene;
    }

    /**
 * Switches between scenes.
 *
 * @param scene The scene to switch to
 */
    public void switchScenes(Scene scene) {
        stage.setScene(scene);
    }

    /**
 * Shows an error alert with the specified message.
 *
 * @param cause The cause of the error
 */
    public void alertError(Throwable cause) {
        Platform.runLater(() -> {
            TextArea text = new TextArea(cause.toString());
            text.setEditable(false);
            Alert alert = new Alert(AlertType.ERROR);
            alert.getDialogPane().setContent(text);
            alert.setResizable(true);
            alert.showAndWait();
        });
    } // alertError

    /**
 * Adds values to the countryComboBox.
 */
    private void addValues() {
        Label[] countryLabels = createLabels("Afghanistan", "Albania", "Algeria", "American Samoa",
                 "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina",
                    "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain",
            "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan",
                        "Bolivia", "Bosnia and Herzegovina", "Botswana", "Bouvet Island", "Brazil",
                  "British Indian Ocean Territory", "British Virgin Islands", "Brunei", "Bulgaria",
                         "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde",
                     "Caribbean Netherlands", "Cayman Islands", "Central African Republic", "Chad",
            "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros",
                   "Cook Islands", "Costa Rica", "Croatia", "Cuba", "Curaçao", "Cyprus", "Czechia",
           "DR Congo", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt",
                  "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Eswatini", "Ethiopia",
                 "Falkland Islands", "Faroe Islands", "Fiji", "Finland", "France", "French Guiana",
           "French Polynesia", "French Southern and Antarctic Lands", "Gabon", "Gambia", "Georgia",
           "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam",
                             "Guatemala", "Guernsey", "Guinea", "Guinea-Bissau", "Guyana", "Haiti",
                "Heard Island and McDonald Islands", "Honduras", "Hong Kong", "Hungary", "Iceland",
                 "India", "Indonesia", "Iran", "Iraq", "Ireland", "Isle of Man", "Israel", "Italy",
                      "Ivory Coast", "Jamaica", "Japan", "Jersey", "Jordan", "Kazakhstan", "Kenya",
              "Kiribati", "Kosovo", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho",
             "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Madagascar",
               "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique",
                 "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia", "Moldova", "Monaco",
             "Mongolia", "Montenegro", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia",
             "Nauru", "Nepal", "Netherlands", "New Caledonia", "New Zealand", "Nicaragua", "Niger",
                             "Nigeria", "Niue", "Norfolk Island", "North Korea", "North Macedonia",
                    "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Palestine",
               "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn Islands",
                  "Poland", "Portugal", "Puerto Rico", "Qatar", "Republic of the Congo", "Romania",
                                                 "Russia", "Rwanda", "Réunion", "Saint Barthélemy",
            "Saint Helena, Ascension and Tristan da Cunha", "Saint Kitts and Nevis", "Saint Lucia",
                   "Saint Martin", "Saint Pierre and Miquelon", "Saint Vincent and the Grenadines",
                          "Samoa", "San Marino", "Saudi Arabia", "Senegal", "Serbia", "Seychelles",
            "Sierra Leone", "Singapore", "Sint Maarten", "Slovakia", "Slovenia", "Solomon Islands",
                 "Somalia", "South Africa", "South Georgia", "South Korea", "South Sudan", "Spain",
               "Sri Lanka", "Sudan", "Suriname", "Svalbard and Jan Mayen", "Sweden", "Switzerland",
                  "Syria", "São Tomé and Príncipe", "Taiwan", "Tajikistan", "Tanzania", "Thailand",
             "Timor-Leste", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey",
                         "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine",
                  "United Arab Emirates", "United Kingdom", "United States Minor Outlying Islands",
               "United States Virgin Islands", "United States", "Uruguay", "Uzbekistan", "Vanuatu",
            "Vatican City", "Venezuela", "Vietnam", "Wallis and Futuna", "Western Sahara", "Yemen",
                                                                             "Zambia", "Zimbabwe");
        countryComboBox.getItems().addAll(countryLabels);
        Label initialValue = new Label("Select a country");
        initialValue.setFont(new Font("Standard", 20.0));
        initialValue.setAlignment(javafx.geometry.Pos.CENTER);
        initialValue.setStyle("-fx-alignment: center;" + "-fx-text-fill: black;");
        countryComboBox.setValue(initialValue);
    } // addValues

    /**
 * Creates an array of labels from the given strings.
 *
 * @param strings The strings to use
 * @return The array of labels
 */
    public static Label[] createLabels(String... strings) {
        Label[] labels = new Label[strings.length];

        for (int i = 0; i < strings.length; i++) {
            labels[i] = new Label(strings[i]);
            labels[i].setFont(new Font("Standard", 20.0));
            labels[i].setAlignment(javafx.geometry.Pos.CENTER);
            labels[i].setStyle("-fx-alignment: center;"
                               + "-fx-text-fill: black;");
        }

        return labels;
    }

    /**
 * Creates a VBox with a label and two HBoxes.
 *
 * @param labelText The text for the label
 * @return The VBox
 */
    private VBox createVBox(String labelText) {

        VBox vBox = new VBox();
        vBox.setPrefHeight(200.0);
        vBox.setPrefWidth(100.0);

        Label label = new Label(labelText);
        label.setAlignment(javafx.geometry.Pos.CENTER);
        label.setPrefHeight(63.0);
        label.setPrefWidth(640.0);
        label.setStyle("-fx-border-color: black; -fx-border-width: 3; -fx-border-insets: 3;"
                       + "-fx-background-color: white;");
        label.setFont(new Font("Georgia", 38.0));

        HBox hBox1 = createHBox();
        HBox hBox2 = createHBox();

        vBox.getChildren().addAll(label, hBox1, hBox2);

        return vBox;
    }

    /**
 * Creates an HBox with an ImageView and a TextArea.
 *
 * @return The HBox
 */
    private HBox createHBox() {

        HBox hBox = new HBox();
        hBox.setPrefHeight(120.0);
        hBox.setPrefWidth(200.0);
        hBox.setSpacing(4.0);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(115.0);
        imageView.setFitWidth(115.0);
        imageView.setPickOnBounds(true);

        TextArea textArea = new TextArea("Name of Item\nAddress of Item\nWebsite\nRating");
        textArea.setEditable(false);
        textArea.setPrefHeight(115.0);
        textArea.setPrefWidth(537.0);
        textArea.setFont(new Font(20.0));

        hBox.getChildren().addAll(imageView, textArea);

        return hBox;
    }

    /**
 * Loads information about a country from the API.
 *
 * @return The CountryResponse object
 */
    public CountryResponse loadCountry() {
        try {
            String name = URLEncoder.encode(this.countryComboBox.getValue().getText(),
                                            StandardCharsets.UTF_8);
            String countriesQuery = String.format("%s%s?fullText=true", COUNTRIES_API, name)
                .replaceAll("\\+", "%20");
            System.out.println(countriesQuery);
            HttpRequest countryRequest = HttpRequest.newBuilder()
                .uri(URI.create(countriesQuery))
                .build();
            HttpResponse<String> countryResponse = HTTP_CLIENT
                .send(countryRequest, BodyHandlers.ofString());
            if (countryResponse.statusCode() != 200) {
                throw new IOException(countryResponse.toString());
            }
            this.countriesString = countryResponse.body();
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
            e.printStackTrace();
        }
        String countryString = countriesString.substring(1, countriesString.length() - 1);

        CountryResponse countryResult = GSON.fromJson(countryString, CountryResponse.class);
        return countryResult;
    }

/**
 * Loads information about a city from the API.
 *
 * @param cor The CountryResponse object
 * @return The CityResponse object
 */
    public CityResponse loadCity(CountryResponse cor) {
        try {
            String country = URLEncoder.encode(cor.cca2, StandardCharsets.UTF_8);
            String cityQuery = String.format("%s", country);
            String cityUri = CITIES_API + cityQuery;
            System.out.println(cityUri);
            HttpRequest cityRequest = HttpRequest.newBuilder()
                .uri(URI.create(cityUri))
                .header("X-Api-Key", "mOoOMdEWOibzoS0VmMOPjQ==blzjkk6h6SB8Idz8")
                .build();

            HttpResponse<String> cityResponse = HTTP_CLIENT
                .send(cityRequest, BodyHandlers.ofString());

            if (cityResponse.statusCode() != 200) {
                throw new IOException(cityResponse.toString());
            }

            this.citiesString = cityResponse.body();
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
            e.printStackTrace();
        }
        CityResponse[] cityArray = GSON.fromJson(citiesString, CityResponse[].class);
        int numCities = Math.min(20, cityArray.length);
        Random random = new Random();
        int c = random.nextInt(numCities);
        CityResponse cityResult = cityArray[c];
        this.city = cityResult.name;
        return cityResult;
    }

    /**
 * Loads information about a restaurant near the specified city.
 *
 * @param cr The CityResponse object
 * @return The LocationResponse object
 */
    public LocationResponse loadRestaurant(CityResponse cr) {

        try {
            String locationsKey = URLEncoder.encode("AIzaSyBZsgEEfTE8SRSajifmZ7OathKzsQ2ojM8",
                                                    StandardCharsets.UTF_8);
            String locationsQueryLat = URLEncoder.encode(Double.toString(cr.latitude),
                                                         StandardCharsets.UTF_8);
            String locationsQueryLng = URLEncoder.encode(Double.toString(cr.longitude),
                                                         StandardCharsets.UTF_8);
            String locationsQuery = String.format(
                "query=restaurant&location=%s,%s&radius=10000&key=%s",
                locationsQueryLat, locationsQueryLng, locationsKey);
            String locationsUri = LOCATIONS_API + locationsQuery;
            System.out.println(locationsUri);
            HttpRequest locationsRequest = HttpRequest.newBuilder()
                .uri(URI.create(locationsUri))
                .build();

            HttpResponse<String> locationsResponse = HTTP_CLIENT
                .send(locationsRequest, BodyHandlers.ofString());

            if (locationsResponse.statusCode() != 200) {
                throw new IOException(locationsResponse.toString());
            }

            this.locationsString1 = locationsResponse.body();
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
            e.printStackTrace();
        }

        LocationResponse locationResult = GSON.fromJson(locationsString1, LocationResponse.class);
        return locationResult;
    }

    /**
 * Loads information about an attraction near the specified city.
 *
 * @param cr The CityResponse object
 * @return The LocationResponse object
 */
    public LocationResponse loadAttraction(CityResponse cr) {

        try {
            String locationsKey = URLEncoder.encode("AIzaSyBZsgEEfTE8SRSajifmZ7OathKzsQ2ojM8",
                                                    StandardCharsets.UTF_8);
            String locationsQueryLat = URLEncoder.encode(Double.toString(cr.latitude),
                                                         StandardCharsets.UTF_8);
            String locationsQueryLng = URLEncoder.encode(Double.toString(cr.longitude),
                                                         StandardCharsets.UTF_8);
            String locationsQuery = String.format(
                "query=attraction&location=%s,%s&radius=10000&key=%s",
                locationsQueryLat, locationsQueryLng, locationsKey);
            String locationsUri = LOCATIONS_API + locationsQuery;

            HttpRequest locationsRequest = HttpRequest.newBuilder()
                .uri(URI.create(locationsUri))
                .build();

            HttpResponse<String> locationsResponse = HTTP_CLIENT
                .send(locationsRequest, BodyHandlers.ofString());

            if (locationsResponse.statusCode() != 200) {
                throw new IOException(locationsResponse.toString());
            }

            this.locationsString2 = locationsResponse.body();
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
            e.printStackTrace();
        }

        LocationResponse locationResult = GSON.fromJson(locationsString2, LocationResponse.class);
        return locationResult;
    }

    /**
 * Loads information about an activity near the specified city.
 *
 * @param cr The CityResponse object
 * @return The LocationResponse object
 */
    public LocationResponse loadActivity(CityResponse cr) {

        try {
            String locationsKey = URLEncoder.encode("AIzaSyBZsgEEfTE8SRSajifmZ7OathKzsQ2ojM8",
                                                    StandardCharsets.UTF_8);
            String locationsQueryLat = URLEncoder.encode(Double.toString(cr.latitude),
                                                         StandardCharsets.UTF_8);
            String locationsQueryLng = URLEncoder.encode(Double.toString(cr.longitude),
                                                         StandardCharsets.UTF_8);
            String locationsQuery = String.format(
                "query=activities&location=%s,%s&radius=10000&key=%s",
                locationsQueryLat, locationsQueryLng, locationsKey);
            String locationsUri = LOCATIONS_API + locationsQuery;
            System.out.println(locationsUri);
            HttpRequest locationsRequest = HttpRequest.newBuilder()
                .uri(URI.create(locationsUri))
                .build();

            HttpResponse<String> locationsResponse = HTTP_CLIENT
                .send(locationsRequest, BodyHandlers.ofString());

            if (locationsResponse.statusCode() != 200) {
                throw new IOException(locationsResponse.toString());
            }

            this.locationsString3 = locationsResponse.body();
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
            e.printStackTrace();
        }

        LocationResponse locationResult = GSON.fromJson(locationsString3, LocationResponse.class);
        return locationResult;
    }

     /**
 * Loads information about a hotel near the specified city.
 *
 * @param cr The CityResponse object
 * @return The LocationResponse object
 */
    public LocationResponse loadHotel(CityResponse cr) {

        try {
            String locationsKey = URLEncoder.encode("AIzaSyBZsgEEfTE8SRSajifmZ7OathKzsQ2ojM8",
                                                    StandardCharsets.UTF_8);
            String locationsQueryLat = URLEncoder.encode(Double.toString(cr.latitude),
                                                         StandardCharsets.UTF_8);
            String locationsQueryLng = URLEncoder.encode(Double.toString(cr.longitude),
                                                         StandardCharsets.UTF_8);
            String locationsQuery = String.format(
                "query=hotel&location=%s,%s&radius=10000&key=%s",
                locationsQueryLat, locationsQueryLng, locationsKey);
            String locationsUri = LOCATIONS_API + locationsQuery;

            HttpRequest locationsRequest = HttpRequest.newBuilder()
                .uri(URI.create(locationsUri))
                .build();

            HttpResponse<String> locationsResponse = HTTP_CLIENT
                .send(locationsRequest, BodyHandlers.ofString());

            if (locationsResponse.statusCode() != 200) {
                throw new IOException(locationsResponse.toString());
            }

            this.locationsString4 = locationsResponse.body();
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
            e.printStackTrace();
        }

        LocationResponse locationResult = GSON.fromJson(locationsString4, LocationResponse.class);
        return locationResult;
    }

    /**
 * Adds information from a given {@code LocationResponse} to a specified {@code VBox}.
 * The information is randomly selected from the location results, and two distinct results
 * are chosen to populate different areas of the specified {@code VBox}. Additionally,
 * photos associated with the selected locations are added to the display.
 *
 * @param lr     The {@code LocationResponse} containing location results.
 * @param vBox   The {@code VBox} container to which the location information will be added.
 */
    public void addInfo(LocationResponse lr, VBox vBox) {
        int numOf = Math.min(10, lr.results.length);
        Random randomA = new Random();
        int a = randomA.nextInt(numOf);
        System.out.println("start1");
        while (lr.results[a].rating == 0.0 || lr.results[a].name == this.countryComboBox
               .getValue().getText() || (lr.results[a].formattedAddress != null &&
               (!lr.results[a].formattedAddress.replaceAll("\\s+","").contains(this.countryComboBox
               .getValue().getText().replaceAll("\\s+","")) || !lr.results[a].formattedAddress
                .contains(city)))
               || lr.results[a].photos == null) {
            Random newRandom = new Random();
            a = newRandom.nextInt(numOf);
        }
        System.out.println("finish1");
        int b = a;

        while (b == a) {
            Random randomB = new Random();
            b = randomB.nextInt(numOf);
        }
        System.out.println("start2");
        while (b == a || lr.results[b].rating == 0.0 || lr.results[b].name == this.countryComboBox
               .getValue().getText() || (lr.results[b].formattedAddress != null &&
               (!lr.results[b].formattedAddress.replaceAll("\\s+","").contains(this.countryComboBox
               .getValue().getText().replaceAll("\\s+","")) || !lr.results[b].formattedAddress
                .contains(city)))
               || lr.results[b].photos == null) {
            Random newRandom2 = new Random();
            b = newRandom2.nextInt(numOf);
        }
        System.out.println("finish2");
        final int finalA = a;
        final int finalB = b;
        Platform.runLater(() -> {
            ((TextArea)((HBox)(vBox.getChildren().get(1))).getChildren().get(1))
                .setText(lr.results[finalA].name + "\n" + lr.results[finalA]
                         .formattedAddress + "\n" + lr.results[finalA].rating + " Rating");
            ((TextArea)((HBox)(vBox.getChildren().get(2))).getChildren().get(1))
                .setText(lr.results[finalB].name + "\n" + lr.results[finalB]
                         .formattedAddress + "\n" + lr.results[finalB].rating + " Rating");
            addPhoto(vBox, lr, 1, finalA);
            addPhoto(vBox, lr, 2, finalB);
        });
    }

/**
 * Populates the user interface with information from different types of locations.
 * This method takes four instances of {@code LocationResponse} representing hotel,
 * attraction, activity, and restaurant locations, and adds information for each type
 * to its corresponding {@code VBox} container. Additionally, it outputs the selected
 * country to the console for debugging purposes.
 *
 * @param hotel       The {@code LocationResponse} containing hotel location results.
 * @param attraction  The {@code LocationResponse} containing attraction location results.
 * @param activity    The {@code LocationResponse} containing activity location results.
 * @param restaurant  The {@code LocationResponse} containing restaurant location results.
 */
    public void make(LocationResponse hotel, LocationResponse attraction,
                     LocationResponse activity, LocationResponse restaurant) {
        addInfo(hotel, vBox1);
        addInfo(attraction, vBox2);
        addInfo(activity, vBox4);
        addInfo(restaurant, vBox3);
        System.out.println(this.countryComboBox.getValue().getText());
    }

    /**
 * Adds a photo to the provided VBox at the specified index.
 *
 * @param vBox The VBox to which the photo will be added.
 * @param lr The LocationResponse object containing information about the photo.
 * @param index The index at which the photo will be added.
 * @param i The index of the photo within the LocationResponse object.
 */
    private void addPhoto(VBox vBox, LocationResponse lr, int index, int i) {
        if (lr.results[i].photos != null) {
            ((ImageView)((HBox)(vBox.getChildren().get(index))).getChildren().get(0)).setImage(
                new Image("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_ref"
                          + "erence=" + lr.results[i].photos[0].photoReference +
                          "&key=AIzaSyBZsgEEfTE8SRSajifmZ7OathKzsQ2ojM8"));
        }
    }

/**
 * Initiates a background thread to load location data, update UI components, and switch scenes
 * accordingly.
 * Executes loading operations for country, city, hotel, activity, attraction, and restaurant.
 * Displays the loaded information using the 'make' method and updates UI components with the
 * appropriate scene transitions.
 */
    public void go() {
        Thread loadDataThread = new Thread(() -> {
            Platform.runLater(() -> {
                    //               switchScenes(loadingScene);
            });

            theCountry = loadCountry();
            theCity = loadCity(theCountry);
            theHotel = loadHotel(theCity);
            theActivity = loadActivity(theCity);
            theAttraction = loadAttraction(theCity);
            theRestaurant = loadRestaurant(theCity);
            make(theHotel, theAttraction, theActivity, theRestaurant);

            Platform.runLater(() -> {
                titleLabel.setText("Enjoy your trip to " + theCity.name + ", " +
                                   countryComboBox.getValue().getText() + "!");
                switchScenes(scene2);
            });
        });

        loadDataThread.start();
    }

    /**
 * Initializes the graphical user interface (GUI) components during the first setup.
 * This method configures various layout and style properties for the primary
 * containers, labels, input elements, and buttons used in the application's UI.
 * It sets the alignment, size, spacing, and styles for the root panes, labels,
 * input boxes, and buttons to create an organized and visually appealing interface.
 *
 * Note: This method assumes the existence of specific UI elements like root1, root2,
 * startingLabel, descriptionLabel, inputBox, countryComboBox, goButton, topHBox, and titleLabel.
 * Ensure these elements are declared before calling this method.
 */
    public void firstInitialization() {
        root1.setAlignment(Pos.CENTER);
        root1.setPrefSize(1280, 720);
        root1.setSpacing(20);
        root1.setStyle("-fx-background-color: aliceblue;" +
                       "-fx-background-size: 1280 720;");

        root2.setMaxHeight(Double.MAX_VALUE);
        root2.setMaxWidth(Double.MAX_VALUE);
        root2.setMinHeight(Double.MIN_VALUE);
        root2.setMinWidth(Double.MIN_VALUE);
        root2.setPrefHeight(720.0);
        root2.setPrefWidth(1280.0);
        root2.setStyle("-fx-background-color: aliceblue;");

        startingLabel.setPrefSize(1280, 134);
        startingLabel.setAlignment(Pos.BOTTOM_CENTER);
        startingLabel.setFont(new Font("Georgia", 96));

        descriptionLabel.setPrefSize(935, 69);
        descriptionLabel.setAlignment(Pos.CENTER);
        descriptionLabel.setStyle("-fx-background-color: lavender;");
        descriptionLabel.setFont(new Font("Georgia", 17.5));

        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPrefSize(1280, 130);
        inputBox.setSpacing(75);

        countryComboBox.setPrefSize(430, 170);
        countryComboBox.setStyle("-fx-background-color: lavender;" +
                                 "-fx-text-fill: black;");

        goButton.setPrefSize(430, 170);
        goButton.setAlignment(Pos.CENTER);
        goButton.setFont(new Font(48));

        inputBox.getChildren().addAll(countryComboBox, goButton);

        root1.getChildren().addAll(startingLabel, descriptionLabel, inputBox);

        topHBox.setPrefHeight(94.0);
        topHBox.setPrefWidth(1280.0);
        topHBox.setSpacing(5.0);
        VBox.setMargin(topHBox, new Insets(3.0));

        titleLabel.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        titleLabel.setPrefHeight(108.0);
        titleLabel.setPrefWidth(900.0);
        titleLabel.setFont(new Font("Georgia", 30.0));
    }

    /**
 * Performs the second stage of initialization for the graphical user interface (GUI)
 * components. This method configures various layout and style properties for UI elements
 * related to the secondary stage of the application. It includes the setup of navigation
 * buttons, a title label, and a grid layout containing multiple VBoxes for organizing
 * information about hotels, attractions, restaurants, and activities.
 *
 * Note: This method assumes the existence of specific UI elements like backButton,
 * remakeButton, topHBox, titleLabel, gridPane, col1, col2, row1, row2, vBox1, vBox2, vBox3,
 * and vBox4. Ensure these elements are declared before calling this method.
 */
    public void secondInitialization() {
        backButton.setAlignment(javafx.geometry.Pos.CENTER);
        backButton.setMnemonicParsing(false);
        backButton.setPrefHeight(77.0);
        backButton.setPrefWidth(164.0);
        backButton.setFont(new Font("Georgia", 36.0));
        HBox.setMargin(backButton, new Insets(7.0));

        remakeButton.setAlignment(javafx.geometry.Pos.CENTER);
        remakeButton.setMnemonicParsing(false);
        remakeButton.setPrefHeight(108.0);
        remakeButton.setPrefWidth(185.0);
        remakeButton.setFont(new Font("Georgia", 30.0));
        HBox.setMargin(remakeButton, new Insets(7.0));

        topHBox.getChildren().addAll(titleLabel, remakeButton, backButton);

        gridPane.setHgap(3.0);
        gridPane.setPrefHeight(613.0);
        gridPane.setPrefWidth(1280.0);
        gridPane.setVgap(10.0);

        col1.setHgrow(Priority.SOMETIMES);
        col1.setMinWidth(10.0);
        col1.setPrefWidth(100.0);
        col2.setHgrow(Priority.SOMETIMES);
        col2.setMinWidth(10.0);
        col2.setPrefWidth(100.0);

        row1.setMinHeight(10.0);
        row1.setPrefHeight(30.0);
        row1.setVgrow(Priority.SOMETIMES);
        row2.setMinHeight(10.0);
        row2.setPrefHeight(30.0);
        row2.setVgrow(Priority.SOMETIMES);

        gridPane.getColumnConstraints().addAll(col1, col2);
        gridPane.getRowConstraints().addAll(row1, row2);

        vBox1 = createVBox("Hotels:");
        GridPane.setMargin(vBox1, new Insets(0.0, 0.0, 0.0, 0.0));
        gridPane.add(vBox1, 0, 0);

        vBox2 = createVBox("Attractions:");
        GridPane.setColumnIndex(vBox2, 1);
        GridPane.setMargin(vBox2, new Insets(0.0, 0.0, 0.0, 0.0));
        gridPane.add(vBox2, 1, 0);

        vBox3 = createVBox("Restaurants:");
        GridPane.setRowIndex(vBox3, 1);
        GridPane.setColumnIndex(vBox3, 1);
        GridPane.setMargin(vBox3, new Insets(0.0, 0.0, 0.0, 0.0));
        gridPane.add(vBox3, 1, 1);

        vBox4 = createVBox("Activities:");
        GridPane.setRowIndex(vBox4, 1);
        GridPane.setMargin(vBox4, new Insets(0.0, 0.0, 0.0, 0.0));
        gridPane.add(vBox4, 0, 1);
    }

} // ApiApp
