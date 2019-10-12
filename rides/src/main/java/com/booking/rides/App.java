package com.booking.rides;

import java.io.*;
import java.net.*;
import java.util.*;
import java.io.Console;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;

import javax.net.ssl.HttpsURLConnection;


public class App {

    private final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) {

        App app = new App();

        Console c = System.console();

        System.out.println("Enter pickup: ");
        String pickup = c.readLine();

        System.out.println("Enter dropoff: ");
        String dropoff = c.readLine();

        System.out.println("Enter number of passengers for supplier Dave. If not, press ENTER.");
        String passengers = c.readLine();

        try {
            app.getDaveTaxis(pickup, dropoff, passengers);

            System.out.println("You'll now see options from all suppliers:");
            app.getAllTaxis(pickup, dropoff);
        }
        catch(Exception e) {
            System.out.println("No results found");
            e.printStackTrace();
        }
    }

    private void getDaveTaxis(String pickup, String dropoff, String passengers) throws Exception {

        try {
            String daveUrl = "https://techtest.rideways.com/dave?pickup=" + pickup + "&dropoff=" + dropoff;

            JSONArray car_options = getSupplierOptions(daveUrl);

            ArrayList<JSONObject> car_options_list = new ArrayList<>();

            for (int i = 0; i < car_options.length(); i++) {
                car_options_list.add((JSONObject) car_options.get(i));
            }
            Collections.sort(car_options_list, new MyJSONComparator());
            Collections.reverse(car_options_list);

            System.out.println();

            if (passengers.isEmpty()) {
                System.out.println();
                for (JSONObject o : car_options_list) {
                    System.out.println(o.getString("car_type") + "-" + o.getInt("price"));
                }
            }
            else {
                boolean found = filterByPassengersNumber(car_options_list, passengers);
                if (found == false) {
                    System.out.println("No results found");
                }
            }
        }
        catch(Exception e) {
            throw e;
        }
    }

    private void getAllTaxis(String pickup, String dropoff) throws Exception {
        String[] car_types = {"STANDARD", "EXECUTIVE", "LUXURY", "PEOPLE_CARRIER", "LUXURY_PEOPLE_CARRIER", "MINIBUS"};
        HashMap<String, HashMap<Integer, String>> map = new HashMap<String, HashMap<Integer, String>>();

        try {
            String[] suppliers = {"dave", "eric", "jeff"};
            for (String supplier: suppliers) {
                String url = "https://techtest.rideways.com/" + supplier + "?pickup=" + pickup + "&dropoff=" + dropoff;
                JSONArray car_options = getSupplierOptions(url);
                map = createTaxisMap(map, supplier, car_types, car_options);
            }
            System.out.println();
            for (String key: map.keySet()) {
                Object[] prices = (map.get(key)).keySet().toArray();
                Arrays.sort(prices);
                if (prices.length != 0)
                    System.out.println(key + "-" + (map.get(key)).get(prices[0]) + "-" + prices[0]);
            }
        }
        catch (Exception e) {
            throw e;
        }
    }

    public HashMap<String, HashMap<Integer, String>> createTaxisMap(
            HashMap<String, HashMap<Integer, String>> map, String supplier,
            String[] car_types, JSONArray car_options) {

        for (String type: car_types) {
            HashMap<Integer, String> price_map = new HashMap<Integer, String>();
            for (int i = 0; i < car_options.length(); i++) {
                if (((JSONObject) car_options.get(i)).getString("car_type").equals(type)) {
                    Integer price = ((JSONObject) car_options.get(i)).getInt("price");
                    price_map.put(price, supplier);
                }
            }
            if (map.containsValue(map.get(type)))
                map.get(type).putAll(price_map);
            else map.put(type, price_map);
        }

        return map;
    }

    public boolean filterByPassengersNumber(ArrayList<JSONObject> car_options_list, String passengers) {

        try {
            String[] car_types = {"STANDARD", "EXECUTIVE", "LUXURY", "PEOPLE_CARRIER", "LUXURY_PEOPLE_CARRIER", "MINIBUS"};
            List<String> car_list = new ArrayList<String>(Arrays.asList(car_types));
            boolean found = false;

            switch (Integer.parseInt(passengers)) {
                case 4:
                    car_list.remove("PEOPLE_CARRIER");
                    car_list.remove("LUXURY_PEOPLE_CARRIER");
                    car_list.remove("MINIBUS");
                    car_types = car_list.toArray(new String[0]);
                    break;
                case 6:
                    car_list.remove("STANDARD");
                    car_list.remove("EXECUTIVE");
                    car_list.remove("LUXURY");
                    car_list.remove("MINIBUS");
                    car_types = car_list.toArray(new String[0]);
                    break;
                case 16:
                    car_list.remove("STANDARD");
                    car_list.remove("EXECUTIVE");
                    car_list.remove("LUXURY");
                    car_list.remove("PEOPLE_CARRIER");
                    car_list.remove("LUXURY_PEOPLE_CARRIER");
                    car_types = car_list.toArray(new String[0]);
                    break;
                default:
                    System.out.println("This is not a valid number. I will print all options");
            }
            for (JSONObject o : car_options_list) {
                if (Arrays.asList(car_types).contains(o.getString("car_type"))) {
                    System.out.println(o.getString("car_type") + "-" + o.getInt("price"));
                    found = true;
                }
            }
            return found;
        }
        catch(Exception e) {
            System.out.println("Something went wrong");
            return false;
        }
    }

    public JSONArray getSupplierOptions(String url) throws Exception {

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            if (responseCode > 199 && responseCode < 300) {

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine = in.readLine();
                JSONObject response = new JSONObject(inputLine);
                in.close();

                System.out.println(response);

                JSONArray car_options = response.getJSONArray("options");
                return car_options;
            }
            else {
                throw new Exception("Something went wrong");
            }
        }
        catch(Exception e) {
            throw e;
        }
    }

    class MyJSONComparator implements Comparator<JSONObject> {

        @Override
        public int compare(JSONObject o1, JSONObject o2) {
            Integer v1 = (Integer) o1.get("price");
            Integer v3 = (Integer) o2.get("price");
            return v1.compareTo(v3);
        }
    }
}
