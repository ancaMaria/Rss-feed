package com.example.ancacret.rssfeed.adapters;


import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ancacret.rssfeed.R;
import com.example.ancacret.rssfeed.pojo.DrawerCategoryListItem;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class RVCategoriesAdapter extends RecyclerView.Adapter<RVCategoriesAdapter.CategoriesViewHolder> {

    //public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    //public static final int TYPE_SECTION = 2;
    private List<DrawerCategoryListItem> mDrawersList = new ArrayList<DrawerCategoryListItem>();
    private Context mContext;


    public RVCategoriesAdapter(Context context, List<DrawerCategoryListItem> drawersList) {
        mDrawersList = drawersList;
        mContext = context;
    }

    @Override
    public CategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_list_item, parent, false);
        return new CategoriesViewHolder(view, new RVProvidersAdapter.ProvidersViewHolder.IHolderListener() {
            @Override
            public void onProviderClick(TextView provider, int position) {
                DrawerCategoryListItem listItem = mDrawersList.get(position);
                EventBus.getDefault().post(listItem);
            }
        });


        /*if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);
            return new CategoriesViewHolder(view, viewType);
        } else if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_list_item, parent, false);
            return new CategoriesViewHolder(view, viewType);
        }
        return null;*/
    }

    @Override
    public void onBindViewHolder(CategoriesViewHolder holder, int i) {
        holder.category.setText(mDrawersList.get(i).getItem().getCategory());
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(100);
        drawable.setColor(mDrawersList.get(i).getItem().getColor());
        holder.categoryIcon.setBackground(drawable);


       /* if(holder.holderId == 0){
            holder.headerTag.setText(mDrawersList.get(i).getItem().getCategory());
            holder.mArrow.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    return false;
                }
            });
        } else if(holder.holderId == 1){
            holder.category.setText(mDrawersList.get(i-1).getItem().getCategory());
            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(100);
            drawable.setColor(mDrawersList.get(i-1).getItem().getColor());
            holder.categoryIcon.setBackground(drawable);
        }*/

    }

    @Override
    public int getItemCount() {
        return mDrawersList.size();
    }

    @Override
    public int getItemViewType(int position) {
       /* if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }*/
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int pos) {
        return pos == 0;
    }


    public static class CategoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private int holderId;
        private TextView headerTag, category;
        private ImageView mArrow;
        private View categoryIcon;
        private RVProvidersAdapter.ProvidersViewHolder.IHolderListener mListener;

        public CategoriesViewHolder(View itemView, RVProvidersAdapter.ProvidersViewHolder.IHolderListener listener) {
            super(itemView);
            mListener = listener;
            headerTag = (TextView) itemView.findViewById(R.id.headerText);
            mArrow = (ImageView) itemView.findViewById(R.id.downUpIcon);
            category = (TextView) itemView.findViewById(R.id.itemText);
            categoryIcon =  itemView.findViewById(R.id.item_icon);
            category.setOnClickListener(this);
            itemView.setOnClickListener(this);

           /* if (viewType == TYPE_HEADER) {
                headerTag = (TextView) itemView.findViewById(R.id.headerText);
                mArrow = (ImageView) itemView.findViewById(R.id.downUpIcon);
                holderId = 0;
            } else if (viewType == TYPE_ITEM) {
                category = (TextView) itemView.findViewById(R.id.itemText);
                categoryIcon =  itemView.findViewById(R.id.item_icon);
                holderId = 1;
            }*/

        }

        @Override
        public void onClick(View v) {
            mListener.onProviderClick(category, getPosition());
        }

        public static interface ICategoriesHolderClickListener{
            public void onCategoryClick(TextView category, int position);
        }


    }


}
