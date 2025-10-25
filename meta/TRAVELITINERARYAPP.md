# Travel Itinerary Application

## Part 1.1: App Description

```
https://github.com/jke48222/cs1302-api-app
```

The Travel Itinerary takes a user-inputted country from a dropdown
list and picks a popular city from that country. The app then creates
a personalized itinerary for the user through finding hotels, attractions,
activities, and restaurants within the vicinity of the city and neighboring
cities (around 6 mile radius). It gives the user two locations in each
category, along with the name, address, and rating of each location.
If the user wants to explore other cities within the country they selected,
then they can press the remake button to make another itinerary with a
different city. The user can also press the back button to go back to the
title page and select a different country. The APIs that I am using are a
country API, city API, and location API through Google Maps. The country API
is used to find the 2-letter code for the country selected (e.g. US for
United States). The city API is used to generate a popular city within the
selected country by using the country code from the country API. The location
API is used to find the hotels, attractions, activities, and restaurants near
the city by using its latitude and longitude coordinates provided by the city
API. It is also used to get a photo ID of the location, which is input into a
special URL to grab the photo.

## Part 1.2: APIs

> For each RESTful JSON API that your app uses (at least two are required),
> include an example URL for a typical request made by your app. If you
> need to include additional notes (e.g., regarding API keys or rate
> limits), then you can do that below the URL/URI. Placeholders for this
> information are provided below. If your app uses more than two RESTful
> JSON APIs, then include them with similar formatting.

### API 1

```
https://restcountries.com/v3.1/name/United%20States?fullText=true
```

### API 2

```
https://api.api-ninjas.com/v1/city?limit=20&country=US
```

> API Key is included in header when making an HTTP Request.

### API 3

```
https://maps.googleapis.com/maps/api/place/textsearch/json?query=activities&location=41.8373,-87.6862&radius=10000&key=AIzaSyBZsgEEfTE8SRSajifmZ7OathKzsQ2ojM8
```

## Part 2: New

> What is something new and/or exciting that you learned from working
> on this project?

Something new and exciting that I learned from working on this project
is just how customizable javafx is. I experimented with changing the
background colors to increasing the font sizes, and also switching
between three different scenes.

## Part 3: Retrospect

> If you could start the project over from scratch, what do
> you think might do differently and why?

If I were to start over from scratch, I would have tried to not
overcomplicate things. At first, I was using 5 different APIs,
including Yelp and TripAdvisor, but using those just complicated
a lot as using the information from them would be use different
methods and it took a lot of time figuring out how to connect
each API.