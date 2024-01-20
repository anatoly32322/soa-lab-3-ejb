package org.example.interfaces;

import org.example.wrapper.CountWrapper;
import org.example.wrapper.PercentWrapper;

import javax.ejb.Remote;

@Remote
public interface DemographyService {
    PercentWrapper getPersonCountByNationalityAndHairColor(String nationality, String hairColor) throws Exception;
    CountWrapper getPersonCountByHairColor(String hairColor) throws Exception;
}
