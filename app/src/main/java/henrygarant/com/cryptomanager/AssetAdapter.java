package henrygarant.com.cryptomanager;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;
import com.cloudinary.android.MediaManager;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.ViewHolder> {

    private List<Asset> mItems;
    private Context mContext;
    private PostItemListener mItemListener;
    private DatabaseHelper mDbHelper;
    private Comparator<Asset> currentComparator = Asset.getComparator(0);

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView nameTv;
        public TextView tickerTv;
        public TextView priceTv;
        public TextView pc1HTv;
        public TextView pc24HTv;
        public TextView pc7DTv;
        public ImageView image;
        public LottieAnimationView likeButton;

        PostItemListener mItemListener;

        public ViewHolder(View itemView, PostItemListener postItemListener) {
            super(itemView);

            nameTv = (TextView) itemView.findViewById(R.id.name);
            tickerTv = (TextView) itemView.findViewById(R.id.ticker);
            priceTv = (TextView) itemView.findViewById(R.id.price);
            pc1HTv = (TextView) itemView.findViewById(R.id.pc_1h);
            pc24HTv = (TextView) itemView.findViewById(R.id.pc_24h);
            pc7DTv = (TextView) itemView.findViewById(R.id.pc_7d);
            image = (ImageView) itemView.findViewById(R.id.image);
            likeButton = (LottieAnimationView) itemView.findViewById(R.id.likeButton);


            this.mItemListener = postItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Asset item = getItem(getAdapterPosition());
            this.mItemListener.onPostClick(item);
            notifyDataSetChanged();
        }
    }

    public AssetAdapter(Context context, List<Asset> posts, PostItemListener itemListener) {
        mItems = posts;
        mContext = context;
        mItemListener = itemListener;
        mDbHelper = new DatabaseHelper(mContext);

    }

    @Override
    public AssetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.asset_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(postView, this.mItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AssetAdapter.ViewHolder holder, int position) {

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

        final Asset item = mItems.get(position);
        holder.nameTv.setText(item.getName());

        String priceString = "0.00";

        try {
            priceString = String.format("%.3f", Double.parseDouble(item.getPriceUsd()));
            priceString = numberFormat.format(Double.parseDouble(priceString));
        } catch (NumberFormatException e){

        }

        holder.priceTv.setText("$" + priceString);

        holder.tickerTv.setText(item.getSymbol());

        holder.pc1HTv.setText("1h: " + item.getPercentChange1h());
        holder.pc24HTv.setText("24h: " + item.getPercentChange24h());
        holder.pc7DTv.setText("7d: " + item.getPercentChange7d());

        if(!mDbHelper.checkIfLiked(item.getSymbol())){
            holder.likeButton.setAnimation(R.raw.notliked);
            item.setLiked(false);
        }else{
            holder.likeButton.setAnimation(R.raw.liked);
            item.setLiked(true);
        }

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mDbHelper.checkIfLiked(item.getSymbol())){
                    holder.likeButton.setAnimation(R.raw.liked);
                    holder.likeButton.playAnimation();
                    //add to db
                    boolean result = mDbHelper.addData(item.getSymbol());
                    if(result){
                        Log.d("AssetAdpater", "Succesfully liked asset!");
                        item.setLiked(true);
                    }else{
                        Log.d("AssetAdpater", "Failed to like asset!");
                    }
                }else{
                    holder.likeButton.setAnimation(R.raw.notliked);
                    holder.likeButton.playAnimation();
                    //remove from db
                    boolean result = mDbHelper.removeLike(item.getSymbol());
                    if(result){
                        Log.d("AssetAdpater", "Succesfully unliked asset!");
                        item.setLiked(false);
                    }else{
                        Log.d("AssetAdpater", "Failed to unlike asset!");
                    }
                }
            }
        });

        //set color based on went up or down
        Asset.percentColor(mContext, holder.pc1HTv);
        Asset.percentColor(mContext, holder.pc24HTv);
        Asset.percentColor(mContext, holder.pc7DTv);

        GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;
        requestBuilder = Glide.with(mContext)
                .using(Glide.buildStreamModelLoader(Uri.class, mContext), InputStream.class)
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
                .into(holder.image);

        //Glide.with(mContext).load(MediaManager.get().url().generate("zec_n7gtk8.svg")).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateAnswers(List<Asset> items) {
        mItems = items;
        refreshLikes();
        Collections.sort(mItems, currentComparator);
        notifyDataSetChanged();
    }

    public void sortAnswers(long id) {
        Log.d("AssetAdpater", "Sorting based on "+ id);
        if(id == 0){
            Log.d("AssetAdpater", "Refreshing Likes");
            refreshLikes();
        }
        Collections.sort(mItems, Asset.getComparator(0));
        Collections.sort(mItems, Asset.getComparator(id));
        currentComparator = Asset.getComparator(id);
        notifyDataSetChanged();
    }

    private void refreshLikes() {
        for(Asset item : mItems){
            if(!mDbHelper.checkIfLiked(item.getSymbol())){
                item.setLiked(false);
            }else{
                item.setLiked(true);
            }
        }
    }


    private Asset getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }

    public interface PostItemListener {
        void onPostClick(Asset clickedAsset);
    }

    public List<Asset> getmItems() {
        return mItems;
    }


}
