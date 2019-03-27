package com.android.babbler.DataClasses;

public class Category {

    private String cID;
    private String cName;
    private String cParent;
    private int cIndex;

    /**
     *  constructor for existing categories
     *  @param cID : category ID
     *  @param cName : category name
     *  @param cParent : Id of the parent category
     *  @param cIndex : Index is number of children +1, we use it for calculating the cID
     */
    public Category(String cID, String cName, String cParent, int cIndex) {
        this.cID = cID;
        this.cName = cName;
        this.cParent = cParent;
        this.cIndex = cIndex;
    }

    /**
     *  constructor for creating a new category
     *  @param cID : category ID
     *  @param cName : category name
     *  @param cParent : Id of the parent category
     */
    public Category(String cID, String cName, String cParent) {
        this.cID = cID;
        this.cName = cName;
        this.cParent = cParent;
        this.cIndex=0;
    }



    public String getcID() {
        return cID;
    }

    public void setcID(String cID) {
        this.cID = cID;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcParent() {
        return cParent;
    }

    public void setcParent(String cParent) {
        this.cParent = cParent;
    }

    public int getcIndex() {
        return cIndex;
    }

    public void setcIndex(int cIndex) {
        this.cIndex = cIndex;
    }


}
