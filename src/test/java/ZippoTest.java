import Model.Location;
import Model.Place;
import io.restassured.builder.*;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {

    @Test
    public void test1()
    {
        given()

                .when()
                .then();
    }

    @Test
    public void contentTypeTest(){
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("country",equalTo("United States"))
        ;
    }

    @Test
    public void stateTest(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("places[0].state",equalTo("California"))
                ;
    }

    @Test
    public void checkHasItem(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                //.log().body()
                .statusCode(200)
                .body("places.'place name'",hasItem("Dörtağaç Köyü"))
                ;
    }

    @Test
    public void bodyArrayHasSizeTest(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .statusCode(200)
                .body("places",hasSize(1));
    }

    @Test
    public void combiningTest(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .statusCode(200)
                .body("places",hasSize(1))
                .body("places.state",hasItem("California"))
                .body("places[0].'place name'",equalTo("Beverly Hills"))
                ;
    }









    @Test
    public void pathParamTest(){
        given()
                .pathParam("ulke","us")
                .pathParam("postakod",90210)
                .log().uri()
                .when()
                .get("http://api.zippopotam.us/{ulke}/{postakod}")
                .then()
                .statusCode(200)
                ;
    }

    @Test
    public void queryTest(){

        given()
                .param("page",1)
                .log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                .statusCode(200)
                ;
    }
RequestSpecification requestSpec;
    ResponseSpecification responseSpec;
    @BeforeClass
    public void Setup(){
        baseURI = "https://gorest.co.in/public/v1";
        requestSpec=new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setContentType(ContentType.JSON)
                .build();
        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .log(LogDetail.BODY)
                .build();

    }

    @Test
    public void queryParamTest2(){
        for (int i = 1; i <10 ; i++) {


            given()
                    .param("page", i)
                    .spec(requestSpec)
                    .when()
                    .get("/users")
                       .then()
                    .spec(responseSpec)
            ;
        }
    }

    @Test
    public void extractingJsonPath(){
        String countryName=
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")
                        .then()
                        .extract().path("country")
                ;
        System.out.println("countryName = " + countryName);
        Assert.assertEquals(countryName,"United States");
    }

    @Test
    public void extractingJsonPath2(){
        String placename=
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")
                        .then()
                        .log().body()
                        .extract().path("places[0].'place name'")
                ;
        System.out.println("placename = " + placename);
        Assert.assertEquals(placename,"Beverly Hills");
    }

    @Test
    public void extractingJsonPath3(){
        //dönen değerdeki limit bilgisini yazdırma
        int limit=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .statusCode(200)
                        .extract().path("meta.pagination.limit")
                ;
        System.out.println("limit = " + limit);
    }

    @Test public void extractingJsonPath4(){
        ArrayList<Integer> id=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .statusCode(200)
                        .extract().path("data.id");
        System.out.println("id = "+ id);
    }

    @Test public void extractingJsonPath5(){
        ArrayList<String> name=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .statusCode(200)
                        .extract().path("data.name");
        System.out.println("name = "+ name);
    }

    @Test public void extractingJsonPathResponseAll(){
        Response response =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .statusCode(200)
                        .extract().response();
        ;
        List<Integer> ids=response.path("data.id");
        List<String> names=response.path("data.name");
        int limit=response.path("meta.pagination.limit");

        System.out.println("ids = " + ids);
        System.out.println("names = " + names);
        System.out.println("limit = " + limit);

        Assert.assertTrue(names.contains("Dakshayani Pandey"));
        Assert.assertTrue(ids.contains(1203767));
        Assert.assertEquals(limit,10,"test sonucu hatali");

    }

    @Test
    public void extractJsonAll_POJO(){
        Location locationNesnesi=
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .extract().body().as(Location.class)
        ;
        System.out.println("locationNesnesi.getCountry =" +
                " " + locationNesnesi.getCountry());

        for (Place p : locationNesnesi.getPlaces())
            System.out.println("p = " + p);
        System.out.println(locationNesnesi.getPlaces().get(0).getPlacename());

    }

    @Test
    public void extractPOJO_Soru(){
        Location adana=
        given()
                .when()
                .get("https://api.zippopotam.us/tr/01000")
                .then()
                .log().body()
                .statusCode(200)
                .extract().body().as(Location.class)
                ;
                for (Place p : adana.getPlaces())
                    if (p.getPlacename().equalsIgnoreCase("Dörtağaç Köyü"))
                    {
                        System.out.println("p = " + p);
                    }
    }


}
