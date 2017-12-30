package henrygarant.com.cryptomanager;

import java.util.HashMap;

/**
 * Created by henry_000 on 12/30/2017.
 */

public class TickerURL {

    public static HashMap<String, String> tickers;

    static {
        tickers = new HashMap<String, String>();
        tickers.put("BTC", "Crypto/btc_pwhtky.svg");
        tickers.put("ETH", "Crypto/eth_v2d0vv.svg");
        tickers.put("BCH", "Crypto/bch_vhszgq.svg");
        tickers.put("MIOTA", "Crypto/MIOTA.svg");
        tickers.put("XRP", "Crypto/XRP.svg");
        tickers.put("LTC", "Crypto/ltc_sqtivx.svg");
        tickers.put("DASH", "Crypto/dash_gpaclz.svg");
        tickers.put("XEM", "Crypto/xem_kwzb2u.svg");
        tickers.put("XMR", "Crypto/xmr_wpfkww.svg");
        tickers.put("BTG", "Crypto/btc_pwhtky.svg");
        tickers.put("ADA", "ada_qt7cfz.svg");
        tickers.put("EOS", "eos_tffk0u.svg");
        tickers.put("XLM", "xlm_billdo.svg");
        tickers.put("ETC", "etc_ctbodd.svg");
        tickers.put("NEO", "neo_ejtinc.svg");
        tickers.put("QTUM", "qtum_xyzddl.svg");
        tickers.put("BCC", "bcc_vzwh4z.svg");
        tickers.put("TRX", "trx_n58xiv.svg");
        tickers.put("PPT", "ppt_qutr25.svg");
        tickers.put("OMG", "omg_gl1jsd.svg");
        tickers.put("WAVES", "waves_wpe3hp.svg");
        tickers.put("ZEC", "zec_n7gtk8.svg");
        tickers.put("LSK", "lsk_wiqmrl.svg");
        tickers.put("USDT", "usdt_ukuamc.svg");
        tickers.put("BTS", "bts_eh8bcj.svg");
    }

    public static String getImgURL(String ticker) {
        if (!tickers.containsKey(ticker)) {
            return "blank_qkuzw9.svg";
        }
        return tickers.get(ticker);
    }

}
