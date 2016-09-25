package ru.zennex.zennextesttask.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ru.zennex.zennextesttask.R;
import ru.zennex.zennextesttask.data.DatabaseHelperFactory;
import ru.zennex.zennextesttask.data.ItemRecyclerViewTable;

/**
 * Created by Kostez on 24.09.2016.
 */

public class ListRecyclerViewAdapter extends RecyclerView.Adapter<ListRecyclerViewAdapter.ListViewHolder> {

    private Dao<ItemRecyclerViewTable, Integer> listRecyclerViewTableDao;

    public static final int MENU_EDIT = 1;
    public static final int MENU_DELETE = 2;
    public static final int LAST_POSITION = -1;

    private final Context mContext;
    private List<ItemRecyclerViewTable> listItems;

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ListRecyclerViewAdapter(Context context, List<ItemRecyclerViewTable> listItems) throws SQLException {
        mContext = context;
        if (listItems != null)
            this.listItems = (listItems);
        else this.listItems = new ArrayList<>();
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.list_recycler_view_item, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, final int position) {

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    listRecyclerViewTableDao = DatabaseHelperFactory.getDatabaseHelper().getListRecyclerViewTableDao();
                    updateCheckBox(holder, holder.checkBox.isChecked());
                    listItems.get(position).setItemChecked(holder.checkBox.isChecked());
                    listRecyclerViewTableDao.update(listItems.get(position));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        updateCheckBox(holder, listItems.get(position).isItemChecked());
        holder.textView.setText(listItems.get(position).getItemTitle());
        holder.checkBox.setChecked(listItems.get(position).isItemChecked());


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                setPosition(holder.getLayoutPosition());
                return false;
            }
        });
    }

    @Override
    public void onViewRecycled(ListViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        public final ImageView imageView;
        public final TextView textView;
        public final CheckBox checkBox;

        public ListViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.lrv_item_image_view);
            textView = (TextView) view.findViewById(R.id.lrv_item_text_view);
            checkBox = (CheckBox) view.findViewById(R.id.lrv_item_check_box);

            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle("Select The Action");
            menu.add(Menu.NONE, MENU_EDIT, Menu.NONE, R.string.edit);
            menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, R.string.delete);
        }
    }

    public void add(ItemRecyclerViewTable itemRecyclerViewTable, int position) {
        position = position == LAST_POSITION ? getItemCount() : position;
        listItems.add(position, itemRecyclerViewTable);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        if (position == LAST_POSITION && getItemCount() > 0)
            position = getItemCount() - 1;

        if (position > LAST_POSITION && position < getItemCount()) {
            listItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void notifiyInserted(int position) {
        position = position == LAST_POSITION ? getItemCount() : position;
        notifyItemInserted(position);
    }

    public void notifiyChanged(int position) {
        position = position == LAST_POSITION ? getItemCount() : position;
        notifyItemChanged(position);
    }

    public void notifiyRemoved(int position) {
        position = position == LAST_POSITION ? getItemCount() : position;
        notifyItemRemoved(position);
    }

    private void updateCheckBox(ListViewHolder holder, boolean isChecked) {
        if (isChecked) {
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star));
        } else {
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_border));
        }
    }
}