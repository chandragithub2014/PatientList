package com.android.meddata.MedDataDTO;

/**
 * Created by CHANDRASAIMOHAN on 10/31/2015.
 */
public class PhysicianDTO {
  private String category;
    private String phyDesc;
    private String listId;
    private String locId;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getPhyDesc() {
        return phyDesc;
    }

    public void setPhyDesc(String phyDesc) {
        this.phyDesc = phyDesc;
    }
}
