package ru.zennex.zennextesttask.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.zennex.zennextesttask.parse.Quote;
import ru.zennex.zennextesttask.R;

/**
 * Created by Kostez on 23.09.2016.
 */

public class ParsingRecyclerViewAdapter extends RecyclerView.Adapter<ParsingRecyclerViewAdapter.ParsingViewHolder> {

    private List<Quote> quotes;

    public ParsingRecyclerViewAdapter(List<Quote> quotes) {
        if (quotes != null)
            this.quotes = (quotes);
        else
            this.quotes = new ArrayList<>();
    }

    @Override
    public ParsingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.parsing_card_view_item, parent, false);
        ParsingViewHolder vh = new ParsingViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ParsingViewHolder holder, int position) {
        Quote quote = quotes.get(position);
        holder.id.setText(String.valueOf(quote.getId()));

        String descriptionText = quote.getDescription().replaceAll("<br>", "\n").replaceAll("&quot;", "\"");
        holder.description.setText(descriptionText);

        holder.time.setText(quote.getTime());
        holder.rating.setText(String.valueOf(quote.getRating()));
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }

    public void addItem(Quote quote) {
        this.quotes.add(quote);
    }

    public void remove(int position) {
        this.quotes.remove(position);
    }

    public class ParsingViewHolder extends RecyclerView.ViewHolder {
        public TextView id;
        public TextView description;
        public TextView time;
        public TextView rating;

        public ParsingViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.tv_id);
            description = (TextView) view.findViewById(R.id.tv_description);
            time = (TextView) view.findViewById(R.id.tv_time);
            rating = (TextView) view.findViewById(R.id.tv_rating);
        }
    }
}
