package henrygarant.com.cryptomanager;

import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;
import com.cloudinary.android.MediaManager;
import com.google.gson.Gson;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by henry_000 on 12/29/2017.
 */

public class ClickedAssetActivity extends AppCompatActivity {

    private TextView name;
    private TextView ticker;
    private TextView price;
    private TextView pc1h;
    private TextView pc24h;
    private TextView pc7d;
    private TextView marketCap;
    private TextView vol24H;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clicked_asset);

        name = (TextView)findViewById(R.id.clickedName);
        ticker = (TextView)findViewById(R.id.clickedTicker);
        price = (TextView)findViewById(R.id.clickedPrice);
        pc1h = (TextView)findViewById(R.id.clickedPC1H);
        pc24h = (TextView)findViewById(R.id.clickedPC24H);
        pc7d = (TextView)findViewById(R.id.clickedPC7D);
        marketCap = (TextView)findViewById(R.id.clickedMarketCap);
        vol24H = (TextView)findViewById(R.id.clicked24HVol);
        image = (ImageView) findViewById(R.id.clickedImage);

        String jsonMyObject = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("clickedAsset");
        }

        Asset item = new Gson().fromJson(jsonMyObject, Asset.class);

        if(item != null) {
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

            name.setText(item.getName());
            ticker.setText(item.getSymbol());

            String priceString = String.format("%.3f", Double.parseDouble(item.getPriceUsd()));
            priceString = numberFormat.format(Double.parseDouble(priceString));
            price.setText("$" + priceString);

            pc1h.setText("1H: " + item.getPercentChange1h());
            pc24h.setText("24H: " + item.getPercentChange24h());
            pc7d.setText("7D: " + item.getPercentChange7d());

            Asset.percentColor(this, pc1h);
            Asset.percentColor(this, pc24h);
            Asset.percentColor(this, pc7d);

            String marketCapString = numberFormat.format(Double.parseDouble( item.getMarketCapUsd()));
            String vol24hString = numberFormat.format(Double.parseDouble( item.get24hVolumeUsd()));

            marketCap.setText("Market Cap: $" + marketCapString);
            vol24H.setText("24H Vol: $" + vol24hString);

            GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;
            requestBuilder = Glide.with(this)
                    .using(Glide.buildStreamModelLoader(Uri.class, this), InputStream.class)
                    .from(Uri.class)
                    .as(SVG.class)
                    .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                    .sourceEncoder(new StreamEncoder())
                    .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
                    .decoder(new SvgDecoder())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_background)
                    .animate(android.R.anim.fade_in)
                    .listener(new SvgSoftwareLayerSetter<Uri>());
            Uri uri = Uri.parse(MediaManager.get().url().generate(TickerURL.getImgURL(item.getSymbol())));
            requestBuilder
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    // SVG cannot be serialized so it's not worth to cache it
                    .load(uri)
                    .into(image);
        }
    }
}
