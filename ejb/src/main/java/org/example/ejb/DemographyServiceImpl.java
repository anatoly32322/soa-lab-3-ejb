package org.example.ejb;

//import org.example.controllers.DemographyController;
import org.example.interfaces.DemographyService;
import org.example.models.Country;
import org.example.models.Person;
import org.example.models.PersonList;
import org.example.wrapper.CountWrapper;
import org.example.wrapper.PercentWrapper;
import org.jboss.ejb3.annotation.Pool;

import java.util.*;

import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Pool(value = "labWorkServicePool")
public class DemographyServiceImpl implements DemographyService {
    private Client client;
    private final String mainServiceUrl = "http://localhost:4500";

    @Override
    public PercentWrapper getPersonCountByNationalityAndHairColor(String nationality, String hairColor) throws Exception {
        String url = mainServiceUrl + "/demography-1.0/api/persons/all";
        
        client = ClientBuilder.newClient();
        Response response = client.target(url).request(MediaType.APPLICATION_JSON_TYPE).get();
        if (response.getStatus() != 200) {
            throw new Exception("Unexpected error");
        }
        PersonList personList = response.readEntity(PersonList.class);
        Country countryEnum;
        try {
            countryEnum = Country.valueOf(nationality.toUpperCase());
        } catch (IllegalArgumentException err) {
            throw new Exception("Nationality must be one of the following: Germany, USA, Italy, Thailand");
        }
        Country finalCountryEnum = countryEnum;
        long cnt = personList.getPersonList().stream().
                filter(e -> e.getNationality().equals(finalCountryEnum)).
                filter(e -> e.getHairColor().equalsIgnoreCase(hairColor)).
                count();
        if (cnt == 0) {
            return new PercentWrapper(0);
        }
        return new PercentWrapper((double)cnt/(double)personList.getPersonList().size() * 100);
    }

    @Override
    public CountWrapper getPersonCountByHairColor(String hairColor) throws Exception {
        String url = mainServiceUrl + "/demography-1.0/api/persons/all";

        client = ClientBuilder.newClient();
        Response response = client.target(url).request(MediaType.APPLICATION_JSON_TYPE).get();
        if (response.getStatus() != 200) {
            throw new Exception("Unexpected error");
        }
        PersonList personList = response.readEntity(PersonList.class);
        if (personList == null) {
            throw new Exception("Unexpected error");
        }
        long cnt = personList.getPersonList().stream().
                filter(e -> e.getHairColor().equalsIgnoreCase(hairColor)).
                count();
        return new CountWrapper(cnt);
    }

}
