package com.codepath.flicksapp.adaptres;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.flicksapp.R;
import com.codepath.flicksapp.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.codepath.flicksapp.R.id.tvOverview;
import static com.codepath.flicksapp.R.id.tvTitle;
import static com.codepath.flicksapp.models.Movie.MovieType.NOPOPULAR;
import static com.codepath.flicksapp.models.Movie.MovieType.POPULAR;


/*
 * Created by alex_ on 3/10/2017.
 */

public class ReviewArrayAdapter extends ArrayAdapter<String> {

    private static class ViewHolder{
        TextView tvReview;
    }

    public ReviewArrayAdapter(Context context, List<String> reviews)
    {
        super(context,android.R.layout.simple_list_item_1,reviews);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String review = getItem(position);

        ViewHolder viewHolder1;

        if(convertView==null) {

            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_review, parent, false);
            viewHolder1 = new ViewHolder();
            viewHolder1.tvReview=(TextView) convertView.findViewById(R.id.tvReview);
            convertView.setTag(viewHolder1);
        }
        else
        {
            viewHolder1=(ViewHolder) convertView.getTag();
        }
        viewHolder1.tvReview.setText(review);


        return convertView;
    }


}
