package henrygarant.com.cryptomanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

/**
 * Created by henry_000 on 12/29/2017.
 */


public class Asset implements Comparable<Asset> {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("rank")
    @Expose
    private String rank;
    @SerializedName("price_usd")
    @Expose
    private String priceUsd;
    @SerializedName("price_btc")
    @Expose
    private String priceBtc;
    @SerializedName("24h_volume_usd")
    @Expose
    private String _24hVolumeUsd;
    @SerializedName("market_cap_usd")
    @Expose
    private String marketCapUsd;
    @SerializedName("available_supply")
    @Expose
    private String availableSupply;
    @SerializedName("total_supply")
    @Expose
    private String totalSupply;
    @SerializedName("max_supply")
    @Expose
    private Object maxSupply;
    @SerializedName("percent_change_1h")
    @Expose
    private String percentChange1h;
    @SerializedName("percent_change_24h")
    @Expose
    private String percentChange24h;
    @SerializedName("percent_change_7d")
    @Expose
    private String percentChange7d;
    @SerializedName("last_updated")
    @Expose

    private String lastUpdated;
    private boolean liked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(String priceUsd) {
        this.priceUsd = priceUsd;
    }

    public String getPriceBtc() {
        return priceBtc;
    }

    public void setPriceBtc(String priceBtc) {
        this.priceBtc = priceBtc;
    }

    public String get24hVolumeUsd() {
        return _24hVolumeUsd;
    }

    public void set24hVolumeUsd(String _24hVolumeUsd) {
        this._24hVolumeUsd = _24hVolumeUsd;
    }

    public String getMarketCapUsd() {
        return marketCapUsd;
    }

    public void setMarketCapUsd(String marketCapUsd) {
        this.marketCapUsd = marketCapUsd;
    }

    public String getAvailableSupply() {
        return availableSupply;
    }

    public void setAvailableSupply(String availableSupply) {
        this.availableSupply = availableSupply;
    }

    public String getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(String totalSupply) {
        this.totalSupply = totalSupply;
    }

    public Object getMaxSupply() {
        return maxSupply;
    }

    public void setMaxSupply(Object maxSupply) {
        this.maxSupply = maxSupply;
    }

    public String getPercentChange1h() {
        return percentChange1h;
    }

    public void setPercentChange1h(String percentChange1h) {
        this.percentChange1h = percentChange1h;
    }

    public String getPercentChange24h() {
        return percentChange24h;
    }

    public void setPercentChange24h(String percentChange24h) {
        this.percentChange24h = percentChange24h;
    }

    public String getPercentChange7d() {
        return percentChange7d;
    }

    public void setPercentChange7d(String percentChange7d) {
        this.percentChange7d = percentChange7d;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean getLiked(){
        return liked;
    }

    public void setLiked(boolean like){
        liked = like;
    }

    public static void percentColor(Context mContext, TextView tv){
        if(tv.getText().toString().contains("-")){
            //down = red
            tv.setTextColor(mContext.getResources().getColor(R.color.down));
        }else{
            //up = green
            tv.setTextColor(mContext.getResources().getColor(R.color.up));
        }
    }

    @Override
    public int compareTo(@NonNull Asset o) {
        Double thisPrice = Double.parseDouble(this.getPriceUsd());
        Double thatPrice = Double.parseDouble(o.getPriceUsd());
        return Double.compare(thatPrice, thisPrice);
    }

    public static Comparator<Asset> rankComparator = new Comparator<Asset>() {
        @Override
        public int compare(Asset a1, Asset a2) {
            Double thisChange = Double.parseDouble(a1.getRank());
            Double thatChange = Double.parseDouble(a2.getRank());
            return Double.compare(thisChange, thatChange);
        }
    };

    public static Comparator<Asset> hourPriceChangeComparator = new Comparator<Asset>() {
        @Override
        public int compare(Asset a1, Asset a2) {
            Double thisChange = Double.parseDouble(a1.getPercentChange1h());
            Double thatChange = Double.parseDouble(a2.getPercentChange1h());
            return Double.compare(thatChange, thisChange);
        }
    };

    public static Comparator<Asset> dayPriceChangeComparator = new Comparator<Asset>() {
        @Override
        public int compare(Asset a1, Asset a2) {
            Double thisChange = Double.parseDouble(a1.getPercentChange24h());
            Double thatChange = Double.parseDouble(a2.getPercentChange24h());
            return Double.compare(thatChange, thisChange);
        }
    };

    public static Comparator<Asset> weekPriceChangeComparator = new Comparator<Asset>() {
        @Override
        public int compare(Asset a1, Asset a2) {
            Double thisChange = Double.parseDouble(a1.getPercentChange7d());
            Double thatChange = Double.parseDouble(a2.getPercentChange7d());
            return Double.compare(thatChange, thisChange);
        }
    };

    public static Comparator<Asset> likeComparator = new Comparator<Asset>() {
        @Override
        public int compare(Asset a1, Asset a2) {

            Boolean thisChange = a1.getLiked();
            Boolean thatChange = a2.getLiked();
            return Boolean.compare(thatChange, thisChange);


        }
    };

    public static Comparator<Asset> getComparator(long id) {
        if (id == 1) {
            return Asset.rankComparator;
        } else if (id == 2) {
            return Asset.hourPriceChangeComparator;
        } else if (id == 3) {
            return Asset.dayPriceChangeComparator;
        } else if(id == 4){
            return Asset.weekPriceChangeComparator;
        }

        return Asset.likeComparator;
    }


}
