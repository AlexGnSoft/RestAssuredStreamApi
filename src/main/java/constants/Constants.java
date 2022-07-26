package constants;

public class Constants {
    private static final String URL = "https://api.kucoin.com/api/v1/market/allTickers";

    public Constants() {
    }

    public static String getUrl(){
        return URL;
    }
}
