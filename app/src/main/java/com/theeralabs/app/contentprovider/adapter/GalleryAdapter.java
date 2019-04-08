package com.theeralabs.app.contentprovider.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.theeralabs.app.contentprovider.R;
import com.theeralabs.app.contentprovider.model.GalleryImage;

import java.io.File;
import java.util.List;



public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private List<GalleryImage> mMessagesList;
    private Context mContext;

    public GalleryAdapter(List<GalleryImage> messages, Context context) {
        mMessagesList = messages;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.gallery_item_view, parent, false);
        return new GalleryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtDesc.setText(mMessagesList.get(position).getName());

        File f = new File(mMessagesList.get(position).getImage());
        //Bitmap resized = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(f.getPath()),
        //        150, 150);
        //holder.image.setImageBitmap(decodeSampledBitmapFromFile(f, 150, 150));
        Picasso.with(mContext).load(f)
                .resize(150, 150)
                .into(holder.image);
    }
/*
    private static Bitmap decodeSampledBitmapFromFile(File file,
                                                      int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getPath(), options);
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 2;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
*/
    @Override
    public int getItemCount() {
        return mMessagesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDesc;
        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img);
            txtDesc = itemView.findViewById(R.id.txt_desc);
        }
    }
}

