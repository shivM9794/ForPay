package in.forpay.util;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yuyakaido.android.cardstackview.CardStackLayoutManager;

public abstract class PaginationCardStackListener extends RecyclerView.OnScrollListener {
    public static final int PAGE_START = 1;
    @NonNull
    private CardStackLayoutManager cardStackLayoutManager;
    /**
     * Set scrolling threshold here (for now i'm assuming 10 item in one page)
     */
    private int PAGE_SIZE;
    /**
     * Supporting only LinearLayoutManager for now.
     */
    public PaginationCardStackListener(int PAGE_SIZE, @NonNull CardStackLayoutManager cardStackLayoutManager) {
        this.cardStackLayoutManager = cardStackLayoutManager;
        this.PAGE_SIZE=PAGE_SIZE;
    }
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount = cardStackLayoutManager.getChildCount();
        int totalItemCount = cardStackLayoutManager.getItemCount();
        int firstVisibleItemPosition = cardStackLayoutManager.getTopPosition();
        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE) {
                loadMoreItems();
            }
        }
    }
    protected abstract void loadMoreItems();
    public abstract boolean isLastPage();
    public abstract boolean isLoading();
}
