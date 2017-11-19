package com.asherelgar.myfinalproject.models;

/**
 * Created by asherelgar on 26.6.2017.
 */

public class ShoppingListItem {
    private String name;
    private String owner;
    private String shoppingItemId;
    private boolean isDeleted = true;

    public ShoppingListItem() {
    }

    public ShoppingListItem(String name, String owner, String shoppingItemId, boolean isDeleted) {
        this.name = name;
        this.owner = owner;
        this.shoppingItemId = shoppingItemId;
        this.isDeleted = isDeleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getShoppingItemId() {
        return shoppingItemId;
    }

    public void setShoppingItemId(String shoppingItemId) {
        this.shoppingItemId = shoppingItemId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "ShoppingListItem{" +
                "name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", shoppingItemId='" + shoppingItemId + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
