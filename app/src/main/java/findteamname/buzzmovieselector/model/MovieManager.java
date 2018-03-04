package findteamname.buzzmovieselector.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is mostly going to be for parsing JSON
 * Created by jherndon on 2/19/16.
 */
public class MovieManager implements MovieInformationFacade, MovieSearcherFacade {

    private static String movieID;
    private static List<Movie> movies = new ArrayList<>();

    @Override
    public Movie getMovieObject(JSONObject json) {
        Map<Movie, String> information = new HashMap<>();
        Movie movie = new Movie(json.optString("imdbID"));
        assert json != null;
        movie.Title =  json.optString("Title");
        movie.Description =  json.optString("Plot");
        movie.Year = json.optString("Year");
        movie.Genre = json.optString("Genre");

        return movie;
    }
    @Override
    public String getDisplayURL(String id) {
        return "http://www.omdbapi.com/?i=" + id + "&plot=short";
    }

    /**
     * This method analyzes the parameters and returns the search url
     * @param movie movie title
     * @param category movie category
     * @param year movie release year
     * @param page movie page
     * @return the search url
     */
    public String getSearchURL(String movie, Movie.Type category, int year, int page) {
        if (movie == null || movie.equals("")) {
            throw new RuntimeException("Can't search empty title");
        }
        movie = movie.trim().replaceAll("\\s+", "+");

        String searchWith = "http://www.omdbapi.com/?s=" + movie;
        if (category != Movie.Type.ANY) {
            searchWith = searchWith + "&type=" + category;
        }

        if (year > 0) {
            searchWith = searchWith + "&y=" + year;
        }

        if (page > 1) {
            searchWith = searchWith + "&page=" + page;
        }
        Log.d("URL STRING", searchWith);
        return searchWith;

        /*String[] answer = new String[10];

        APIComm search = APIComm.getInstance();
        search.pullFromURL(searchWith, APIComm.titleListener,answer);

        //searchWith is now something like
        //"http://www.omdbapi.com/?s=Batman+Begins&type=MOVIE&year=1997&page=3"
        return answer;*/
    }

    @Override
    public String[] getSearchElements(JSONObject json, String tag) {

        //Now we want only the info under result
        JSONArray MovieArray = json.optJSONArray("Search");
        String[] output = new String[MovieArray.length()];

        for(int i=0; i < MovieArray.length(); i++) {

            try {
                 //Make a new JSONObject which will represent a movie
                JSONObject movieObject = MovieArray.getJSONObject(i);
                assert movieObject != null;
                output[i] = movieObject.optString(tag);

            } catch (JSONException e) {
                Log.d("VolleyApp", "Failed to get JSON object");
                e.printStackTrace();
            }
        }
        return output;
    }

    /**
     * Gets the movie title (Good for display movie activity coming from search movie activity)
     * @return movie title
     */
    public static String getMovieTitle() {
        return movieID;
    }

    /**
     * Sets the movie title (Good for leaving search movie activity to go display movie)
     * @param movieID
     */
    public static void setMovieTitle(String movieID) {
        MovieManager.movieID = movieID;
    }

    /**
     * Adds the movie to the movie list for the app to locally see movies if the list
     * doesn't already contain the movie
     * @param movie
     */
    public static void addMovie(Movie movie) {
        boolean duplicateMovie = false;
        for (int i = 0; i < movies.size(); i++) {
            Movie curMovie = movies.get(i);
            Log.d("BUGG", curMovie.getID() + ", " + movie.getID() + ", " + curMovie.getID().equals(movie.getID()));
            if (curMovie.getID().equals(movie.getID())) {
                duplicateMovie = true;
            }
        }
        if (!duplicateMovie) {
            movies.add(movie);
        }
    }

    public List<String> getMovieInfoByID(String id) {
        List<String> output = new ArrayList<>();
        for (int i = 0; i < movies.size(); i++) {
            Movie curMovie = movies.get(i);
            if (curMovie.getID().equals(id)) {
                output.add(curMovie.getID());
                output.add(curMovie.getTitle());
                output.add(curMovie.getGenre());
            }
        }
        return output;
    }


//    //how are we gonna update the average rating?
//    public static void setRating(Movie movie, Float rating) {
//        if (ratings == null) {
//            ratings = new HashMap<>();
//        } else if (ratings.get(movie) == null) {
//            ratings.put(movie, rating);
//        } else {
//            //float currentAverage = ratings.get(movie);
//            //float totalPoints = currentAverage * users;
//            //totalPoints += rating;
//            //users++;
//            //rating = totalPoints / users;
//            ratings.put(movie, rating);
//
//            // for now, this will work. but we need another parameter to see how many users have
//            // added to calculate the average/
//            //should we store users? which users rated this movie?
//        }
//    }
//
//    public static Float getRating(Movie movie) {
//        if (ratings == null) {
//            ratings = new HashMap<>();
//            return 0f;
//        } else {
//            Float rating = ratings.get(movie);
//            return rating == null ? 0f : rating;
//        }
//    }
}
