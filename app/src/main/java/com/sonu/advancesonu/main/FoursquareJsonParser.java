package com.sonu.advancesonu.main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Balwant on 06-Feb-18.
 */

public class FoursquareJsonParser {
    public List<HashMap<String, String>> parse(JSONObject jObject){
        JSONObject response=null,group0=null,meta=null;
        JSONArray jPlaces = null,groups=null;
        try {
            /** Retrieves all the elements in the 'places' array */
            meta=jObject.getJSONObject("meta");
            if(!meta.has("errorType")) {
                response = jObject.getJSONObject("response");
                groups = response.getJSONArray("groups");
                group0 = groups.getJSONObject(0);
                jPlaces = group0.getJSONArray("items");
            }
            else{
                String errDetails=meta.getString("errorDetails");
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /** Invoking getPlaces with the array of json object
         * where each json object represent a place
         */
        return getPlaces(jPlaces);
    }


    private List<HashMap<String, String>> getPlaces(JSONArray jPlaces){
        int placesCount = jPlaces.length();
        List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> place = null;

        /** Taking each place, parses and adds to list object */
        for(int i=0; i<placesCount;i++){
            try {
                /** Call getPlace with place JSON object to parse the place */
                place = getPlace((JSONObject)jPlaces.get(i));
                placesList.add(place);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return placesList;
    }

    /** Parsing the Place JSON object */
    private HashMap<String, String> getPlace(JSONObject jPlace){

        HashMap<String, String> place = new HashMap<String, String>();
        String placeName = "-NA-";
        String address="-NA-";
        String latitude="";
        String longitude="";
        String venueId="--NA-";
        String flag="v";

        try {
            // Extracting Place name, if available
            if(jPlace.getJSONObject("venue").has("name")){
                placeName = jPlace.getJSONObject("venue").getString("name");
            }


            venueId=jPlace.getJSONObject("venue").getString("id");
            latitude = jPlace.getJSONObject("venue").getJSONObject("location").getString("lat");
            longitude = jPlace.getJSONObject("venue").getJSONObject("location").getString("lng");
            if(jPlace.getJSONObject("venue").getJSONObject("location").has("address")) {
                address = jPlace.getJSONObject("venue").getJSONObject("location").getString("address");
            }
            else
            {
                address=placeName;
            }
            place.put("venue_name", placeName);
            place.put("Address", address);
            place.put("lat", latitude);
            place.put("lng", longitude);
            place.put("venueid",venueId);
            place.put("flag",flag);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }
}
