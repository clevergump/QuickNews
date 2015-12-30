package com.tiger.quicknews.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tiger.quicknews.R;
import com.tiger.quicknews.bean.ChannelItem;

import java.util.List;

public class DragAdapter extends BaseAdapter {
    /** TAG */
    private final static String TAG = "DragAdapter";
    /** 是否显示底部的ITEM */
    private boolean isItemShow = false;
    private final Context context;
    /** 控制的postion */
    private int holdPosition;
    /** 是否改变 */
    private boolean isChanged = false;
    /** 列表数据是否改变 */
    private boolean isListChanged = false;
    /** 是否可见 */
    boolean isVisible = true;
    /** 可以拖动的列表（即用户选择的频道列表） */
    public List<ChannelItem> channelList;
    /** TextView 频道内容 */
    private TextView item_text;
    /** 要删除的position */
    public int remove_position = -1;

    public DragAdapter(Context context, List<ChannelItem> channelList) {
        this.context = context;
        this.channelList = channelList;
    }

    @Override
    public int getCount() {
        return channelList == null ? 0 : channelList.size();
    }

    @Override
    public ChannelItem getItem(int position) {
        if (channelList != null && channelList.size() != 0) {
            return channelList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.channel_item, null);
        item_text = (TextView) view.findViewById(R.id.text_item);
        ChannelItem channel = getItem(position);
        item_text.setText(channel.getName());
        // 前两个标签是固定不变的, 不允许删除或移动位置.
        if ((position == 0) || (position == 1)) {
            // item_text.setTextColor(context.getResources().getColor(R.color.black));
            // 前两个标签未使能, 是灰色背景
            item_text.setEnabled(false);
        }

        /******************************************************************************
         *** 在进入该页面后未进行任何操作时, 下面的这几个if判断都不成立, 都不会执行.  ******
         ******************************************************************************/
        if (isChanged && (position == holdPosition) && !isItemShow) {
            item_text.setText("");
            // 以下两个设置会让该位置的item的边框变为灰色虚线边框. 见文件 res/drawable/subscribe_item_bg.xml
            item_text.setSelected(true);
            item_text.setEnabled(true);
            isChanged = false;
        }
        if (!isVisible && (position == -1 + channelList.size())) {
            item_text.setText("");
            item_text.setSelected(true);
            item_text.setEnabled(true);
        }
        if (remove_position == position) {
            item_text.setText("");
        }
        return view;
    }

    /** 添加频道列表 */
    public void addItem(ChannelItem channel) {
        channelList.add(channel);
        isListChanged = true;
        notifyDataSetChanged();
    }

    /** 拖动变更频道排序 */
    public void exchange(int dragPostion, int dropPostion) {
        holdPosition = dropPostion;
        ChannelItem dragItem = getItem(dragPostion);
        Log.d(TAG, "startPostion=" + dragPostion + ";endPosition=" + dropPostion);
//        if (dragPostion < dropPostion) {
//            channelList.add(dropPostion + 1, dragItem);
//            channelList.remove(dragPostion);
//        } else {
//            channelList.add(dropPostion, dragItem);
//            channelList.remove(dragPostion + 1);
//        }

        channelList.remove(dragPostion);
        channelList.add(dropPostion, dragItem);

        isChanged = true;
        isListChanged = true;
        notifyDataSetChanged();
    }

    /** 获取频道列表 */
    public List<ChannelItem> getChannnelLst() {
        return channelList;
    }

    /** 设置删除的position */
    public void setRemove(int position) {
        remove_position = position;
        notifyDataSetChanged();
    }

    /** 删除频道列表 */
    public void remove() {
        channelList.remove(remove_position);
        remove_position = -1;
        isListChanged = true;
        notifyDataSetChanged();
    }

    /** 设置频道列表 */
    public void setListDate(List<ChannelItem> list) {
        channelList = list;
    }

    /** 获取是否可见 */
    public boolean isVisible() {
        return isVisible;
    }

    /** 排序是否发生改变 */
    public boolean isListChanged() {
        return isListChanged;
    }

    /** 设置是否可见 */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    /** 显示放下的ITEM */
    public void setShowDropItem(boolean show) {
        isItemShow = show;
    }

}