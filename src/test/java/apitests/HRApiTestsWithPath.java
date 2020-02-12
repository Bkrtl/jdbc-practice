package apitests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.*;

public class HRApiTestsWithPath {

    @BeforeClass
    public void setUpClass(){
        baseURI = ConfigurationReader.get("hrapi.uri");
    }

    @Test
    public void getCountriesWithPath(){
        Response response = given().queryParam("q", "{\"region_id\":2}")
                .when().get("/countries");

        assertEquals(response.statusCode(),200);

        //count value
        System.out.println(response.path("count").toString());

            /*
            items.country_id[0] --> AR
            items.country_name[1]   -->Brazil
            items.links[2].href[0] --> "http://54.161.128.36:1000/ords/hr/countries/CA"
            items.country_name --> list with all countries names
            */


        String firstCountryID=response.path("items.country_id[0]");
        String secondCountryName=response.path("items.country_name[1]");

        System.out.println("firstCountryID = " + firstCountryID);
        System.out.println("secondCountryName = " + secondCountryName);

        String link2=response.path("items.links[2].href[0]");
        System.out.println("link2 = " + link2);

        //get all countries
        List<String> allCountries=response.path("items.country_name");
        System.out.println("allCountries = " + allCountries);
        for (String countries:allCountries) {  // alt altta almak için böyle de yazabiliriz
            System.out.println(countries);
        }

        //assert that all regions id's are equal to 2
        List<Integer> regionIds=response.path("items.region_id");
        for (int regionId:regionIds) {
            System.out.println("regionId = " + regionId);
            assertEquals(regionId,2);
        }

    }

    @Test
    public void countriesWithQueryParam2(){
        Response response = given().accept(ContentType.JSON).and()
                .queryParam("q","{\"job_id\":\"IT_PROG\"}")
                .when().get("/employees");

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json");
        assertTrue(response.body().asString().contains("IT_PROG"));

        //assert that all job_id s are IT_PROG
        List<Object> jobIds=response.path("items.job_id");
        for (Object jobId:jobIds) {
            System.out.println("jobId = " + jobId);//sadece yazdırdım görmek için :)
            assertEquals(jobId,"IT_PROG");
        }

        /* String list ile de yapabiliriz
        List<String> jobids=response.path("items.job_id");
        for (String JobId:jobids) {
            System.out.println("JobId = " + JobId);//sadece yazdırdım görmek için :)
            assertEquals(JobId,"IT_PROG");
        }
        */
    }
}
