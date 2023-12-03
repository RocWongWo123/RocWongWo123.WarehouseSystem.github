package com.warehouse.system;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class Material {

    private BooleanProperty selected = new SimpleBooleanProperty(false);

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean isSelected) {
        selected.set(isSelected);
    }


    public BooleanProperty selectedProperty() {
        return selected;
    }
    private static List<Material> selectedMaterialsList = new ArrayList<>();

    public static List<Material> getSelectedMaterialsList() {
        return selectedMaterialsList;
    }
    public int mid;
    public String materialId;
    public String materialName;
    public String specification;
    public String unit;
    public int stockQuantity;
    public String remark;




    public Material() {
    }

    public Material(int mid,String materialId, String materialName, String specification, String unit, int stockQuantity, String remark) {
        this.mid = mid;
        this.materialId = materialId;
        this.materialName = materialName;
        this.specification = specification;
        this.unit = unit;
        this.stockQuantity = stockQuantity;
        this.remark = remark;
    }
    public void resetSelection() {
        setSelected(false);
    }
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }
    public int getMid() {
        return mid;
    }
    public int setMid(){
        return this.mid=mid;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }



}
