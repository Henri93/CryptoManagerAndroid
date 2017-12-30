package henrygarant.com.cryptomanager;

public class ApiUtils {

    public static final String BASE_URL = "https://api.coinmarketcap.com/v1/ticker/";

    public static CoinMarketCapService getCoinMarketCapService() {
        return RetrofitClient.getClient(BASE_URL).create(CoinMarketCapService.class);
    }
}
