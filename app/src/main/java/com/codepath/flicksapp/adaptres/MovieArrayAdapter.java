package com.codepath.flicksapp.adaptres;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.res.Configuration;

import com.codepath.flicksapp.R;
import com.codepath.flicksapp.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static android.R.attr.orientation;
import static android.os.Build.VERSION_CODES.M;
import static com.codepath.flicksapp.R.id.tvOverview;
import static com.codepath.flicksapp.R.id.tvTitle;


/*
 * Created by alex_ on 3/7/2017.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private static class ViewHolderDtl {
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivImage;
    }
    private static class ViewHolderPlain {
        ImageView ivImage;
        TextView tvTitle1;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies)
    {
        super(context,android.R.layout.simple_list_item_1,movies);
    }

    @Override
    public int getViewTypeCount() {
        return Movie.MovieType.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getMovieType().ordinal();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Configuration config = getContext().getResources().getConfiguration();
        Movie movie = getItem(position);
        switch(movie.getMovieType())
        {
            case POPULAR:
                ViewHolderPlain viewHolder1;

                if(convertView==null) {
                   // LayoutInflater inflater = LayoutInflater.from(getContext());
                    LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.item_movie_popular, parent, false);
                    viewHolder1 = new ViewHolderPlain();
                    viewHolder1.ivImage= (ImageView) convertView.findViewById(R.id.idMovieImage1);
                    viewHolder1.ivImage.setImageResource(0);
                    viewHolder1.tvTitle1=(TextView) convertView.findViewById(R.id.tvTitle1);
                    convertView.setTag(viewHolder1);
                }
                else
                {
                    viewHolder1=(ViewHolderPlain) convertView.getTag();
                }
                viewHolder1.tvTitle1.setText(movie.getOriginalTitle());

                Picasso.with(getContext()).load(movie.getBackdropPath())
                        .transform(new RoundedCornersTransformation(10, 10))
                        .placeholder(R.drawable.img_loading)
                        .into(viewHolder1.ivImage);
                break;

            case NOPOPULAR:
                ViewHolderDtl viewHolder;

                if(convertView==null) {
                    viewHolder = new ViewHolderDtl();
                    LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.item_movie, parent, false);

                    viewHolder.ivImage= (ImageView) convertView.findViewById(R.id.idMovieImage);
                    viewHolder.ivImage.setImageResource(0);
                    viewHolder.tvTitle= (TextView) convertView.findViewById(tvTitle);
                    viewHolder.tvOverview = (TextView) convertView.findViewById(tvOverview);
                    convertView.setTag(viewHolder);
                }
                else
                {
                    viewHolder=(ViewHolderDtl)convertView.getTag();
                }
                viewHolder.tvOverview.setText(movie.getOverview());
                viewHolder.tvTitle.setText(movie.getOriginalTitle());

               // DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();//
                //TO DO use metrics to retrieve correct images based on device configuration

                String imgPath=movie.getPosterPath(); //let it be default;
                if (config.orientation==Configuration.ORIENTATION_LANDSCAPE)
                {       imgPath=movie.getBackdropPath();
                }
                Picasso.with(getContext()).load(imgPath)
                        .transform(new RoundedCornersTransformation(10, 10))
                        .placeholder(R.drawable.img_loading)
                        .into(viewHolder.ivImage);
                break;
        }
        return convertView;
    }


}
