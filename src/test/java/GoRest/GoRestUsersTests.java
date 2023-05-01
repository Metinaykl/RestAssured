package GoRest;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GoRestUsersTests {

    Faker randomUretici=new Faker();
    String rndFullname=randomUretici.name().fullName();
    String rndEmail=randomUretici.internet().emailAddress();

    RequestSpecification reqSpec;
    int userID;

    @BeforeClass
    public void setup() {

        baseURI = "https://gorest.co.in/public/v2/users";
        //baseURI ="https://test.gorest.co.in/public/v2/users/";

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer e4b22047188da067d3bd95431d94259f63896347f9864894a0a7013ee5f9c703")
                .setContentType(ContentType.JSON)
                .setBaseUri(baseURI)
                .build();

    }
    @Test(enabled = false)
    public void createUser(){





        userID=
        given()
                .header("Authorization","Bearer e5dd25e39181916c023deb1d41059fee99ae85eee2e22ca4ac8f7eb1e667ea96")
                .contentType(ContentType.JSON)
                .body("{\"name\":\""+rndFullname+"\", \"gender\":\"male\", \"email\":\""+rndEmail+"\", \"status\":\"active\"}")
               // .log().uri()
               // .log().body()
                .when()
                .post("https://gorest.co.in/public/v2/users")
                .then()
                .log().body()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .extract().path("id")
                ;

    }
    @Test(dependsOnMethods = "createUserMap")
    public void getUserByID(){
        given()
                .header("Authorization","Bearer e5dd25e39181916c023deb1d41059fee99ae85eee2e22ca4ac8f7eb1e667ea96")
                .when()
                .get("https://gorest.co.in/public/v2/users/"+userID)
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id",equalTo(userID))

                ;


    }

    @Test(dependsOnMethods = "createUserMap")
    public void updateUser(){
        Map<String,String>User=new HashMap<>();
        User.put("name","metin aykul");


        given()
                .header("Authorization","Bearer e5dd25e39181916c023deb1d41059fee99ae85eee2e22ca4ac8f7eb1e667ea96")
                .contentType(ContentType.JSON)
                .body(User)
                .when()
                .put("https://gorest.co.in/public/v2/users/"+userID)
                .then()
                .log().body()
                .statusCode(200)
                .body("id",equalTo(userID))
                .body("name",equalTo("metin aykul"))
                ;

    }

    @Test(dependsOnMethods = "updateUser")
    public void deleteUser(){
        given()
                .spec(reqSpec)
                .when()
                .delete(""+userID)
                .then()
                .log().body()
                .statusCode(204)
                ;

    }

    @Test
    public void deleteUserNegative(){

    }

    @Test
    public void createUserMap(){

        Map<String,String> user=new HashMap<>();
        user.put("name",rndFullname);
        user.put("gender","male");
        user.put("email",rndEmail);
        user.put("status","active");






        userID=
                given()
                        .header("Authorization","Bearer e5dd25e39181916c023deb1d41059fee99ae85eee2e22ca4ac8f7eb1e667ea96")
                        .contentType(ContentType.JSON)
                        .body(user)
                        // .log().uri()
                        // .log().body()
                        .when()
                        .post("https://gorest.co.in/public/v2/users")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
        ;

    }

    @Test (enabled = false)
    public void createUser3(){

        User newUsers= new User();
        newUsers.name=rndFullname;
        newUsers.gender="male";
        newUsers.email=rndEmail;
        newUsers.status="active";

        userID=
                given()
                        .header("Authorization","Bearer e5dd25e39181916c023deb1d41059fee99ae85eee2e22ca4ac8f7eb1e667ea96")
                        .contentType(ContentType.JSON)
                        .body(newUsers)
                        // .log().uri()
                        // .log().body()
                        .when()
                        .post("https://gorest.co.in/public/v2/users")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
        ;

    }
}
