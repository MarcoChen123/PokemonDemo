package com.example.pokemonkotlindemo.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pokemonkotlindemo.R;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerView<T> extends RecyclerView {

    protected static final int LAYOUT_GRID = 1;
    protected static final int LAYOUT_LIST = 2;

    protected static final int TYPE_HEAD = 1;
    protected static final int TYPE_FOOT = 2;

    private int maxHeight = -1;

    protected List<T> mList;
    protected MyAdapter mAdapter;

    protected final ArrayList<View> mHeadView = new ArrayList<>();
    protected final ArrayList<Integer> mFootView = new ArrayList<>();

    private OnItemClickListener<T> onItemClickListener;

    public BaseRecyclerView(Context context) {
        this(context, null);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.BaseRecyclerView, 0, 0);
            maxHeight = array.getDimensionPixelSize(R.styleable.BaseRecyclerView_android_maxHeight, -1);
            array.recycle();
        }
        if (getLayoutType() == LAYOUT_GRID) {
            setLayoutManager(new GridLayoutManager(getContext(), getLayoutColumn()));
        } else {
            setLayoutManager(new LinearLayoutManager(getContext(), getLayoutOrientation(), false));
        }
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        if (maxHeight > 0) {
            heightSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthSpec, heightSpec);
    }

    /**
     * 设置数据
     * @param list
     */
    public void setAdapter(List<T> list) {
        mList = list;
        if (mList == null) {
            mList = new ArrayList<>();
        }
        if (mAdapter == null) {
            mAdapter = new MyAdapter();
            setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置item点击事件
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        onItemClickListener = listener;
    }

    /**
     * 刷新View
     */
    public void notifyDataSetChanged() {
        try {
            if (mAdapter != null) mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新Item
     * @param position
     */
    public void notifyItemChanged(int position) {
        try {
            if (mAdapter != null) {
                mAdapter.notifyItemChanged(position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新Item
     * @param item
     */
    public void notifyItemChanged(T item) {
        try {
            if (mAdapter != null) {
                int position = mList.indexOf(item);
                notifyItemChanged(position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入Item刷新
     * @param start
     * @param count
     */
    public void notifyItemRangeInserted(int start, int count) {
        try {
            if (mAdapter != null) {
                mAdapter.notifyItemRangeInserted(start, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void notifyItemRemoved(int position) {
        try {
            if (mAdapter != null) {
                mAdapter.notifyItemRemoved(position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void notifyItemRemoved(T item) {
        try {
            if (mAdapter != null) {
                int position = mList.indexOf(item);
                mAdapter.notifyItemRemoved(position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 子View复写对Head进行设置
     * @param holder
     * @param position
     */
    protected void setHeadViewData(BaseViewHolder holder, int position) {

    }

    /**
     * 子View复写对每个item进行设置
     * @param holder
     * @param data 数据
     * @param position
     */
    protected abstract void setItemViewData(BaseViewHolder holder, T data, int position);

    /**
     * 子View复写对foot进行设置
     * @param holder
     * @param position
     */
    protected void setFootViewData(BaseViewHolder holder, int position) {

    }

    /**
     * 添加header
     * @param headerView
     */
    public void addHeaderView(View headerView) {
        mHeadView.add(headerView);
    }

    /**
     * 添加footer
     * @param footerView
     */
    public void addFooterView(@LayoutRes int footerView) {
        mFootView.add(footerView);
    }

    /**
     * 移除footer
     */
    public void removeFooterView() {
        mFootView.clear();
        notifyDataSetChanged();
    }

    /**
     * itemView布局类型
     * @param position
     * @return
     */
    protected int getItemViewType(int position) {
        return 0;
    }

    /**
     * itemView布局文件
     * @return
     */
    @LayoutRes
    protected abstract int getItemLayout(int viewType);

    /**
     * 列表or网格
     * @return
     */
    protected int getLayoutType() {
        return LAYOUT_LIST;
    }

    /**
     * 列表的方向
     * @return
     */
    @Orientation
    protected int getLayoutOrientation() {
        return LinearLayoutManager.VERTICAL;
    }

    /**
     * 网格的列数
     * @return
     */
    protected int getLayoutColumn() {
        return 3;
    }

    protected final int getItemCount() {
        if (mAdapter != null)
            return mAdapter.getItemCount();
        return 0;
    }

    protected final T getItem(int position) {
        if (position < getHeadCount()) {
            return null;
        }
        return mList.get(position - getHeadCount());
    }

    protected final int getHeadCount() {
        return mHeadView.size();
    }

    protected final int getFootCount() {
        return mFootView.size();
    }

    protected final List<T> getData() {
        return mList;
    }

    class MyAdapter extends Adapter<BaseViewHolder> {

        @Override
        public int getItemCount() {
            return mList == null ? getHeadCount() + getFootCount()
                    : getHeadCount() + getFootCount() + mList.size();
        }

        @Override
        public int getItemViewType(int position) {
            int headcount = getHeadCount();
            if (position < headcount) {
                // 返回Header类型
                return TYPE_HEAD;
            }

            final int itemPosition = position - headcount;
            int itemCount = mList.size();
            if (itemPosition < itemCount) {
                return BaseRecyclerView.this.getItemViewType(position);
            }

            // 返回Footer类型
            return TYPE_FOOT;
        }

        @Override
        @NonNull
        public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            switch (viewType) {
                case TYPE_HEAD:
                    return BaseViewHolder.createViewHolder(mHeadView.get(0));
                case TYPE_FOOT:
                    return BaseViewHolder.createViewHolder(inflater.inflate(mFootView.get(0), parent, false));
                default:
                    return BaseViewHolder.createViewHolder(inflater.inflate(getItemLayout(viewType), parent, false));
            }
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
            int viewType = getItemViewType(position);
            if (viewType == TYPE_HEAD) {
                setHeadViewData(holder, position);
            } else if (viewType == TYPE_FOOT) {
                setFootViewData(holder, position);
            } else {
                int itemPosition = position - getHeadCount();
                int itemCount = getItemCount();
                if (itemPosition < itemCount) {
                    holder.itemView.setOnClickListener(v -> {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(v, getItem(itemPosition), itemPosition);
                        }
                    });
                    // 模仿BaseAdapter的getView方法
                    setItemViewData(holder, getItem(itemPosition), itemPosition);
                }
            }
        }

        @Override
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            LayoutManager manager = recyclerView.getLayoutManager();
            if(manager instanceof GridLayoutManager){
                GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        int type = getItemViewType(position);
                        if(type == TYPE_FOOT){
                            return gridLayoutManager.getSpanCount();
                        }
                        return 1;
                    }
                });
            }
        }
    }

    public interface OnItemClickListener<S> {
        void onItemClick(View itemView, S data, int position);
    }
}
