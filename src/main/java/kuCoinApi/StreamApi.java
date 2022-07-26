package kuCoinApi;

import constants.Constants;
import io.restassured.http.ContentType;
import java.util.List;
import static io.restassured.RestAssured.given;

public class StreamApi {

    public List<TickerData> getTickers(){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get(Constants.getUrl())
                .then().log().body()
                .extract().jsonPath().getList("data.ticker", TickerData.class);
    }
}
