package ru.zennex.zennextesttask.data;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by Kostez on 24.09.2016.
 */

public class ItemRecyclerViewTable implements Serializable {

    @DatabaseField(generatedId = true, columnName = "item_id")
    private int itemId;

    @DatabaseField(columnName = "item_title")
    private String itemTitle;

    @DatabaseField(columnName = "is_item_checked")
    private boolean isItemChecked;

    public ItemRecyclerViewTable() {
    }

    public ItemRecyclerViewTable(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public boolean isItemChecked() {
        return isItemChecked;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public void setItemChecked(boolean itemChecked) {
        isItemChecked = itemChecked;
    }
}