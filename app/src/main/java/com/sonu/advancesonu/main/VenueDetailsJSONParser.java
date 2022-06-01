package com.sonu.advancesonu.main;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Balwant on 06-Feb-18.
 */

public class VenueDetailsJSONParser {
    public HashMap<String, String> parse(JSONObject jObject){

        JSONObject response=null,jPlaceDetails = null;
        try {
            /** Retrieves all the elements in the 'places' array */
            response=jObject.getJSONObject("response");
            jPlaceDetails = response.getJSONObject("venue");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /** Invoking getPlaces with the array of json object
         * where each json object represent a place
         */
        return getPlaceDetails(jPlaceDetails);
    }


    /** Parsing the Place Details Object object */
    private HashMap<String, String> getPlaceDetails(JSONObject jPlaceDetails){


        HashMap<String, String> hPlaceDetails = new HashMap<String, String>();

        String name = "-NA-";
        String icon = "-NA-";
        String address="-NA-";
        String city="-NA-";
        String latitude="";
        String longitude="";
        //String formatted_address="-NA-";
        //String formatted_phone="-NA-";
        String website="-NA-";
        String rating="-NA-";
        //String international_phone_number="-NA-";
        String url="-NA-";

        try {
            // Extracting Place name, if available
            if(jPlaceDetails.has("name")){
                name = jPlaceDetails.getString("name");
            }

            // Extracting Icon, if available
            if(jPlaceDetails.getJSONArray("categories").getJSONObject(0).has("icon")){
                icon = jPlaceDetails.getJSONArray("categories").getJSONObject(0).getJSONObject("icon").getString("prefix")+"bg_44"+
                        jPlaceDetails.getJSONArray("categories").getJSONObject(0).getJSONObject("icon").getString("suffix");
            }

            // Extracting Place Vicinity, if available
            if(jPlaceDetails.getJSONObject("location").has("address")){
                address = jPlaceDetails.getJSONObject("location").getString("address");
            }
            if(jPlaceDetails.getJSONObject("location").has("city")){
                city = jPlaceDetails.getJSONObject("location").getString("city");
            }

            // Extracting Place formatted_address, if available
            //if(!jPlaceDetails.isNull("formatted_address")){
            //    formatted_address = jPlaceDetails.getString("formatted_address");
            //}

            // Extracting Place formatted_phone, if available
            //if(!jPlaceDetails.isNull("formatted_phone_number")){
            //    formatted_phone = jPlaceDetails.getString("formatted_phone_number");
            //}

            // Extracting website, if available
            //if(!jPlaceDetails.isNull("website")){
            //    website = jPlaceDetails.getString("website");
            //}

            // Extracting rating, if available
            if(jPlaceDetails.has("rating")){
                rating = jPlaceDetails.getString("rating");
            }

            // Extracting rating, if available
            //if(!jPlaceDetails.isNull("international_phone_number")){
            //    international_phone_number = jPlaceDetails.getString("international_phone_number");
            //}

            // Extracting url, if available
            if(!jPlaceDetails.isNull("url")){
                url = jPlaceDetails.getString("canonicalUrl");
            }

            latitude = jPlaceDetails.getJSONObject("location").getString("lat");
            longitude = jPlaceDetails.getJSONObject("location").getString("lng");


            hPlaceDetails.put("name", name);
            hPlaceDetails.put("icon", icon);
            hPlaceDetails.put("address", address);
            hPlaceDetails.put("lat", latitude);
            hPlaceDetails.put("lng", longitude);
            //hPlaceDetails.put("formatted_address", formatted_address);
            //hPlaceDetails.put("formatted_phone", formatted_phone);
            hPlaceDetails.put("city", city);
            hPlaceDetails.put("rating", rating);
            //hPlaceDetails.put("international_phone_number", international_phone_number);
            hPlaceDetails.put("url", url);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return hPlaceDetails;
    }
}
