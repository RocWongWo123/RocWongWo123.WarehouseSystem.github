package com.warehouse.system;

public class ShoppingCartItem {
    private Material material;
    private int quantity;

    public ShoppingCartItem(Material material, int quantity) {
        this.material = material;
        this.quantity = quantity;
    }

    public Material getMaterial() {
        return material;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
