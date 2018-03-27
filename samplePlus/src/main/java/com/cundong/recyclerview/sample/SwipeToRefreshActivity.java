package com.cundong.recyclerview.sample;

import android.content.Context;
import android.os.Handler;
import android.preference.SwitchPreference;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.cundong.recyclerview.RecyclerViewUtils;
import com.cundong.recyclerview.sample.R;
import com.cundong.recyclerview.sample.weight.LoadingFooter;
import com.cundong.recyclerview.sample.weight.SFSwipeRefreshLayout;

import java.util.ArrayList;

public class SwipeToRefreshActivity extends AppCompatActivity {

//    private SwipeRefreshLayout refreshLayout;
    private SFSwipeRefreshLayout sfrefreshLayout;
    private DataAdapter mDataAdapter;
    private ArrayList<ItemModel> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_to_refresh);
        dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            ItemModel item = new ItemModel();
            item.id = i;
            item.title = "item" + i;
            dataList.add(item);
        }

        mDataAdapter = new DataAdapter(this);
        mDataAdapter.addItems(dataList);
        sfrefreshLayout = (SFSwipeRefreshLayout) findViewById(R.id.refreshLayout);
        sfrefreshLayout.setAdapter(mDataAdapter);
        sfrefreshLayout.setOnRefreshListener(new SFSwipeRefreshLayout.RefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sfrefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int currentSize = mDataAdapter.getItemCount();

                        //模拟组装10个数据
                        ArrayList<ItemModel> newList = new ArrayList<>();
                        for (int i = 0; i < 10; i++) {
                            ItemModel item = new ItemModel();
                            item.id = currentSize + i;
                            item.title = "item" + (item.id);

                            newList.add(item);
                        }

                        addItems(newList);

//                        dataList.addAll(list);

                        if (mDataAdapter.getItemCount() > 40){
                            sfrefreshLayout.setFooterViewState(LoadingFooter.State.TheEnd);
                        }else {
                            sfrefreshLayout.setFooterViewState(LoadingFooter.State.Normal);
                        }
                        sfrefreshLayout.getAdapter().notifyDataSetChanged();
                    }
                }, 2000);

            }
        });

//        sfrefreshLayout.setLoadMoreState(LoadingFooter.State.Normal);
//        sfrefreshLayout.getAdapter().notifyDataSetChanged();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sfrefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    private void addItems(ArrayList<ItemModel> list) {

        mDataAdapter.addItems(list);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        refreshLayout.setRefreshing(true);
        sfrefreshLayout.setRefreshing(true);
    }

    private class DataAdapter extends RecyclerView.Adapter {

        private LayoutInflater mLayoutInflater;
        private SortedList<ItemModel> mSortedList;

        public DataAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            mSortedList = new SortedList<>(ItemModel.class, new SortedList.Callback<ItemModel>() {

                /**
                 * 返回一个负整数（第一个参数小于第二个）、零（相等）或正整数（第一个参数大于第二个）
                 */
                @Override
                public int compare(ItemModel o1, ItemModel o2) {

                    if (o1.id < o2.id) {
                        return -1;
                    } else if (o1.id > o2.id) {
                        return 1;
                    }

                    return 0;
                }

                @Override
                public boolean areContentsTheSame(ItemModel oldItem, ItemModel newItem) {
                    return oldItem.title.equals(newItem.title);
                }

                @Override
                public boolean areItemsTheSame(ItemModel item1, ItemModel item2) {
                    return item1.id == item2.id;
                }

                @Override
                public void onInserted(int position, int count) {
                    notifyItemRangeInserted(position, count);
                }

                @Override
                public void onRemoved(int position, int count) {
                    notifyItemRangeRemoved(position, count);
                }

                @Override
                public void onMoved(int fromPosition, int toPosition) {
                    notifyItemMoved(fromPosition, toPosition);
                }

                @Override
                public void onChanged(int position, int count) {
                    notifyItemRangeChanged(position, count);
                }
            });
        }

        public void addItems(ArrayList<ItemModel> list) {
            mSortedList.beginBatchedUpdates();

            for(ItemModel itemModel : list) {
                mSortedList.add(itemModel);
            }

            mSortedList.endBatchedUpdates();
        }

        public void deleteItems(ArrayList<ItemModel> items) {
            mSortedList.beginBatchedUpdates();
            for (ItemModel item : items) {
                mSortedList.remove(item);
            }
            mSortedList.endBatchedUpdates();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DataAdapter.ViewHolder(mLayoutInflater.inflate(R.layout.sample_item_text, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            ItemModel item = mSortedList.get(position);

            DataAdapter.ViewHolder viewHolder = (DataAdapter.ViewHolder) holder;
            viewHolder.textView.setText(item.title);
        }

        @Override
        public int getItemCount() {
            return mSortedList.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {

            private TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.info_text);

                textView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ItemModel item = mSortedList.get(RecyclerViewUtils.getAdapterPosition(rec, EndlessLinearLayoutActivity.DataAdapter.ViewHolder.this));
                    }
                });
            }
        }
    }


}
