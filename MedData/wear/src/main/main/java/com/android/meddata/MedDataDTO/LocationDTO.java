package com.android.meddata.MedDataDTO;

/**
 * Created by CHANDRASAIMOHAN on 10/31/2015.
 */
public class LocationDTO {
private String category;
    private String listDesc;
    private String listId;
    private  String locId;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getListDesc() {
        return listDesc;
    }

    public void setListDesc(String listDesc) {
        this.listDesc = listDesc;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }
}
