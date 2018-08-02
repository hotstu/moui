package github.hotstu.naiue.widget.recycler;

import android.support.v7.widget.RecyclerView;

/**
 * 判断recyclerview到底部的辅助类，继承fetchdata方法实现加载更多功能
 */
public abstract class ReachBottomListener extends RecyclerView.OnScrollListener {

    private static final String TAG = ReachBottomListener.class.getSimpleName();
    private boolean loading;

    public void resetLoading() {
        this.loading = false;
    }

    @Override
    public void onScrolled(RecyclerView r, int dx, int dy) {
        if (loading)
            return;
        int contentHeight = r.getHeight() - (r.getPaddingTop() + r.getPaddingBottom());
        int scrollRange = r.computeVerticalScrollRange();
        int scrollOffset = r.computeVerticalScrollOffset();
        boolean reachBottom = scrollRange <= scrollOffset + contentHeight;
        if (reachBottom) {
            this.loading = true;
            onReachBottom();
        }
    }

    /**
     * 加载数据，加载完成后需要手动调用resetLoading()
     *
     */
    protected abstract void onReachBottom();

}