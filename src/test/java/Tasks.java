import Model.ToDo;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Tasks {

    /*
    Task2
    create a request to https://httpstat.us/203
    expect status 203
    expect content type TEXT
     */


    @Test
    public void soru1(){
        ToDo toDo=
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .statusCode(200)
                .extract().body().as(ToDo.class)
        ;
        System.out.println("toDo = " + toDo);
        System.out.println("toDo.getTitle() = " + toDo.getTitle());


    }

    @Test
    public void soru2(){
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title",equalTo("quis ut nam facilis et officia qui"))
                ;

    }

    @Test
    public void soru3(){
        Boolean completed= //2.yöntem testng
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                //.body("completed",equalTo(false)) // 1. yöntem hamcrest
                .extract().path("completed") //2.yöntem testng
                ;
        Assert.assertFalse(completed); //2.yöntem testng
    }
}
