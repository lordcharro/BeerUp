package pt.charroapps.beerup;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import pt.charroapps.beerup.model.Beer;

/**
 * Created by ivolopes on 05/02/16.
 */
public class BeerAdapter extends RecyclerView.Adapter<BeerAdapter.BeerViewHolder>{



    public static class BeerViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name, brewery, style, date;
        ImageView img;

        BeerViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            name = (TextView)itemView.findViewById(R.id.name);
            brewery = (TextView)itemView.findViewById(R.id.brewery);
            style = (TextView)itemView.findViewById(R.id.style);
            date = (TextView)itemView.findViewById(R.id.date);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }

    Beer beers;

    public BeerAdapter(Beer beers){
        this.beers = beers;
    }

    @Override
    public long getItemId(int position) {
        int id = beers.getData().get(position).getStyleId();
        return id;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public BeerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_file, viewGroup, false);
        BeerViewHolder pvh = new BeerViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(BeerViewHolder beerViewHolder, int i) {
        Context context = beerViewHolder.img.getContext();
        beerViewHolder.name.setText(beers.getData().get(i).getName());
        //beerViewHolder.brewery.setText(beers.getData().get(i).getStyle().getName());
        if((beers.getData().get(i).getStyle())!=null) {
            beerViewHolder.style.setText(beers.getData().get(i).getStyle().getShortName());
        }
        String [] dateSplit = (beers.getData().get(i).getCreateDate()).split(" ");
        beerViewHolder.date.setText(dateSplit[0]);

        if((beers.getData().get(i).getLabels())!=null) {
            String url = beers.getData().get(i).getLabels().getMedium();
            Picasso.with(context).load(url).resize(100,100).into(beerViewHolder.img);
        }else
            beerViewHolder.img.setImageResource(R.mipmap.ic_launcher);

    }

    @Override
    public int getItemCount() {
        return beers.getData().size();
    }
}