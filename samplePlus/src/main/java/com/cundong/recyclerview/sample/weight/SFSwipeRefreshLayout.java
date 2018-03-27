package com.cundong.recyclerview.sample.weight;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;

import com.cundong.recyclerview.EndlessRecyclerOnScrollListener;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.cundong.recyclerview.RecyclerViewUtils;
import com.cundong.recyclerview.sample.R;
import com.cundong.recyclerview.sample.utils.RecyclerViewStateUtils;

/**
 * @author 01373506
 * @date 2018/3/22
 */
public class SFSwipeRefreshLayout extends LinearLayout {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerview;
    private RecyclerView.Adapter adapter;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private RefreshListener listener;
    private LoadingFooter.State footerState;

    public SFSwipeRefreshLayout(Context context) {
        super(context);
    }

    public SFSwipeRefreshLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SFSwipeRefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SFSwipeRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        View rootView = inflate(getContext(), R.layout.swipe_refresh_layout,this);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefreshlayout);
        recyclerview = (RecyclerView) rootView.findViewById(R.id.recyclerview);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter == null) throw new NullPointerException("adapter不能为空");
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        recyclerview.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

//        RecyclerViewUtils.setHeaderView(recyclerview, new SampleHeader(getContext()));

        recyclerview.addOnScrollListener(mOnScrollListener);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listener.onRefresh();
            }
        });
    }

    public void addHeaderView(View view) {
        RecyclerViewUtils.setHeaderView(recyclerview, view);
    }

    public RecyclerView getRecyclerview() {
        return recyclerview;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public void setRefreshing(boolean isRefresh) {
        swipeRefreshLayout.setRefreshing(isRefresh);
    }

    public RecyclerView.Adapter<RecyclerView.ViewHolder> getAdapter() {
        return mHeaderAndFooterRecyclerViewAdapter;
    }

//    /**
//     * 设置底部footer状态
//     * @param footerState
//     */
//    public void setLoadMoreState(LoadingFooter.State footerState) {
//        RecyclerViewStateUtils.setFooterViewState(getContext(), recyclerview, footerState, null);
////        RecyclerViewStateUtils.setFooterViewState(recyclerview, footerState);
//    }


    /**
     * 设置底部footer状态
     * @param footerState
     */
    public void setFooterViewState(LoadingFooter.State footerState) {
        RecyclerViewStateUtils.setFooterViewState(recyclerview, footerState);
    }



    /**
     * @param footerState
     * @param errorListener 当网路错误，点击架加载更多footer重新加载回调
     */
    public void setLoadMoreState(LoadingFooter.State footerState, OnClickListener errorListener) {
        RecyclerViewStateUtils.setFooterViewState(getContext(), recyclerview, footerState, errorListener);
    }

    public void setOnRefreshListener(RefreshListener listener) {
        this.listener = listener;
    }

    public interface RefreshListener {
        void onRefresh();
        void onLoadMore();
    }

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {

        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);

            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(recyclerview);
            if(state == LoadingFooter.State.Loading) {
                Log.d("SFSwipeRefreshLayout", "正在加载..");
                return;
            }

            if(state == LoadingFooter.State.TheEnd) {
                Log.d("SFSwipeRefreshLayout", "已加载全部");
                return;
            }

            if(state == LoadingFooter.State.NetWorkError) {
                Log.d("SFSwipeRefreshLayout", "网络错误");
                return;
            }

            listener.onLoadMore();
            RecyclerViewStateUtils.setFooterViewState(getContext(), recyclerview, LoadingFooter.State.Loading, null);
//            if (mCurrentCounter < TOTAL_COUNTER) {
//                // loading more
//                RecyclerViewStateUtils.setFooterViewState(getContext(), recyclerview, LoadingFooter.State.Loading, null);
//                if (listener != null) {
//                    listener.onLoadMore();
//                }
//            } else {
//                //the end
//                RecyclerViewStateUtils.setFooterViewState(getContext(), recyclerview, LoadingFooter.State.TheEnd, null);
//            }
        }
    };
}
