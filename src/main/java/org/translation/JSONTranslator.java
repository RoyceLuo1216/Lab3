package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {


    private JSONArray jsonArray;
    private ArrayList<String> countryLanguages = new ArrayList<>();
    private ArrayList<String> countries = new ArrayList<>();




    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {
            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));
            this.jsonArray = new JSONArray(jsonString);

            // Iterate through jsonArray and populate the country and language lists
            for (int i = 0; i < this.jsonArray.length(); i++) {
                JSONObject country = this.jsonArray.getJSONObject(i);
                String countryCode = country.getString("alpha3");  // Get country code
                countries.add(countryCode);  // Add country code to countries list

                // Grab all language codes from the first country object
                if (i == 0) {
                    for (String key : country.keySet()) {
                        if (!key.equals("alpha2") && !key.equals("alpha3") && !key.equals("id")) {
                            countryLanguages.add(key);  // Add language code to language set
                        }
                    }
                }
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {

        return countryLanguages;
    }

    @Override
    public List<String> getCountries() {

        return countries;
    }

    @Override
    public String translate(String country, String language) {
        for (int i = 0; i < this.jsonArray.length(); i++) {
            JSONObject countryObj = this.jsonArray.getJSONObject(i);

            if (country.equals(countryObj.getString("alpha2")) || country.equals(countryObj.getString("alpha3"))) {
                return countryObj.getString(language);
            }
        }
        return null;
    }


}
