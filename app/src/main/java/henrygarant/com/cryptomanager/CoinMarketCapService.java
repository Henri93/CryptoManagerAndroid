package henrygarant.com.cryptomanager;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CoinMarketCapService {

    @GET("?limit=25")
    Call<List<Asset>> getAssets();

   /* @GET("/answers?order=desc&sort=activity&site=stackoverflow")
    Call<Asset> getAnswers(@Query("tagged") String tags);*/
}
