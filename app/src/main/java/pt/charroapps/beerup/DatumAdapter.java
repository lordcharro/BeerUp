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

import java.util.List;

import pt.charroapps.beerup.model.Datum;

/**
 * Created by ivolopes on 07/02/16.
 */
public class DatumAdapter extends RecyclerView.Adapter<DatumAdapter.DatumViewHolder>{


        public static class DatumViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView name, brewery, style, date;
            ImageView img;

            DatumViewHolder(View itemView) {
                super(itemView);
                cv = (CardView)itemView.findViewById(R.id.card_view);
                name = (TextView)itemView.findViewById(R.id.name);
                brewery = (TextView)itemView.findViewById(R.id.brewery);
                style = (TextView)itemView.findViewById(R.id.style);
                date = (TextView)itemView.findViewById(R.id.date);
                img = (ImageView) itemView.findViewById(R.id.img);
            }
        }

        List <Datum> beers;

        public DatumAdapter(List<Datum> beers){
            this.beers = beers;
        }

        @Override
        public long getItemId(int position) {
            int id = beers.get(position).getStyleId();
            return id;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public DatumViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_file, viewGroup, false);
            DatumViewHolder pvh = new DatumViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(DatumViewHolder beerViewHolder, int i) {
            Context context = beerViewHolder.img.getContext();
            beerViewHolder.name.setText(beers.get(i).getName());
            //beerViewHolder.brewery.setText(beers.getData().get(i).getStyle().getName());
            if((beers.get(i).getStyle())!=null) {
                beerViewHolder.style.setText(beers.get(i).getStyle().getShortName());
            }
            beerViewHolder.date.setText(beers.get(i).getCreateDate());

            if((beers.get(i).getLabels())!=null) {
                String url = beers.get(i).getLabels().getMedium();
                Picasso.with(context).load(url).resize(100,100).into(beerViewHolder.img);
            }else
                beerViewHolder.img.setImageResource(R.mipmap.ic_launcher);

        }

        @Override
        public int getItemCount() {
            return beers.size();
        }
    }