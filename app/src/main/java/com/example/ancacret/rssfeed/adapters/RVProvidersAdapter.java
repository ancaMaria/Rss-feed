package com.example.ancacret.rssfeed.adapters;


import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ancacret.rssfeed.R;
import com.example.ancacret.rssfeed.pojo.DrawerSectionHeader;
import com.example.ancacret.rssfeed.pojo.NewsProvider;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class RVProvidersAdapter extends RecyclerView.Adapter<RVProvidersAdapter.ProvidersViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    private List<DrawerSectionHeader> mDrawersList = new ArrayList<DrawerSectionHeader>();
    private List<NewsProvider> mProviders;
    private Context mContext;
    Typeface typeface;


    public RVProvidersAdapter(Context context, List<DrawerSectionHeader> drawersList, List<NewsProvider> providers) {
        mDrawersList = drawersList;
        mProviders = providers;
        mContext = context;
        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Regular.ttf");
    }

    @Override
    public ProvidersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.provider_row_item, parent, false);
        return new ProvidersViewHolder(view, new ProvidersViewHolder.IHolderListener() {
            @Override
            public void onProviderClick(TextView provider, int position) {
                DrawerSectionHeader header = mDrawersList.get(position);
                /* send message to parent activity */
                EventBus.getDefault().post(header);
            }
        });

       /* if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);
            return new ProvidersViewHolder(view, viewType);
        } else if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_separator, parent, false);
            return new ProvidersViewHolder(view, viewType);
        }
        return null;*/
    }

    @Override
    public void onBindViewHolder(ProvidersViewHolder holder, int i) {
        for(NewsProvider provider : mProviders){
            if(mDrawersList.get(i).getSectionName().equals(provider.getName())){
                holder.providerName.setText(mDrawersList.get(i).getSectionName());
                holder.slogan.setText(provider.getSlogan());
                holder.slogan.setTypeface(typeface);
            }
        }
        holder.providerName.setTextColor((mDrawersList.get(i).getColor()));
        holder.slogan.setTextColor((mDrawersList.get(i).getColor()));

        /*if (holder.holderId == 1) {
            holder.providerName.setText(mDrawersList.get(i-1).getSectionName());
            holder.providerName.setTextColor((mDrawersList.get(i-1).getColor()));
            holder.providerName.setBackgroundColor(mContext.getResources().getColor(R.color.grey_300));
        } else if (holder.holderId == 0) {
            holder.headerTag.setText("PROVIDERS");
            holder.mArrow.setImageDrawable(mContext.getResources().getDrawable(R.drawable.menu_up_white));
            holder.mArrow.setTag(TvrProviderConstants.UP_TAG);
        }*/
    }

    @Override
    public int getItemCount() {
        return mDrawersList.size();
    }

    @Override
    public int getItemViewType(int position) {
        /*if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }*/
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int pos) {
        return pos == 0;
    }

    public static class ProvidersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //private int holderId;
        private TextView providerName;
        private TextView slogan;
        private TextView headerTag;
        private ImageView mArrow;
        public IHolderListener mListener;

        public ProvidersViewHolder(View itemView, IHolderListener listener) {
            super(itemView);
            mListener = listener;
            providerName = (TextView) itemView.findViewById(R.id.separator_title);
            slogan = (TextView) itemView.findViewById(R.id.slogan);
            //slogan = (TextView) itemView.findViewById(R.id.slogan);
            itemView.setOnClickListener(this);
            providerName.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            mListener.onProviderClick(providerName, getPosition());
        }

        public static interface IHolderListener {
            public void onProviderClick(TextView provider, int position);
        }


    }

}
