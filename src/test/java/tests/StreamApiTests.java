package tests;

import constants.Constants;
import io.restassured.http.ContentType;
import kuCoinApi.StreamApi;
import kuCoinApi.TickerComparatorLow;
import kuCoinApi.TickerData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.stream.Collectors;
import static io.restassured.RestAssured.given;

public class StreamApiTests {

    @Test
    public void testCheckCrypto() {
        List<TickerData> tickerData = given()
                .contentType(ContentType.JSON)
                .when()
                .get(Constants.getUrl())
                .then().log().body()
                .extract().jsonPath().getList("data.ticker", TickerData.class);
    }

    @Test
    public void testFindUsdCrypto() {
        StreamApi streamApi = new StreamApi();
        List<TickerData> usdTickers =
                streamApi.getTickers().stream().filter(x->x.getSymbol().endsWith("USDT")).collect(Collectors.toList());

        Assertions.assertTrue(usdTickers.stream().allMatch(x->x.getSymbol().endsWith("USDT")));
    }

    @Test
    public void testSortHighToLowCrypto() {
        StreamApi streamApi = new StreamApi();
        List<TickerData> highToLow = streamApi.getTickers().stream().filter(x->x.getSymbol().endsWith("USDT")).sorted(new Comparator<TickerData>() {
            @Override
            public int compare(TickerData o1, TickerData o2) {
                return o2.getChangeRate().compareTo(o1.getChangeRate());  //sorting from higher to lower
            }
        }).collect(Collectors.toList());
    }

    @Test
    public void testGetTop10Crypto() {
        //fluky teest as prices for crypto is changing every day

        StreamApi streamApi = new StreamApi();
        List<TickerData> highToLow = streamApi.getTickers().stream().filter(x->x.getSymbol().endsWith("USDT")).sorted(new Comparator<TickerData>() {
            @Override
            public int compare(TickerData o1, TickerData o2) {
                return o2.getChangeRate().compareTo(o1.getChangeRate());  //sorting from higher to lower
            }
        }).collect(Collectors.toList());

        List<TickerData> top10 = highToLow.stream().limit(10).collect(Collectors.toList());

        Assertions.assertEquals(top10.get(0).getSymbol(), "FRA-USDT");
    }

    @Test
    public void testSortLowToHighCrypto() {
        StreamApi streamApi = new StreamApi();
        List<TickerData> lowToHigh = streamApi.getTickers().stream()
                .filter(x->x.getSymbol().endsWith("USDT")).
                sorted(new TickerComparatorLow()).limit(10).collect(Collectors.toList());
    }

    @Test
    public void testMap() {
        StreamApi streamApi = new StreamApi();
        Map<String, Float> usd = new HashMap<>();
        List<String> lowerCase = streamApi.getTickers().stream().map(x->x.getSymbol().toLowerCase()).collect(Collectors.toList());

        streamApi.getTickers().stream().forEach(x->usd.put(x.getSymbol(), Float.parseFloat(x.getChangeRate())));

    }
}
