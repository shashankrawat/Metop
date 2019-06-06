package com.elintminds.mac.metatopos.beans.getnotificationtype;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationTypeData {

    @SerializedName("typeId")
    @Expose
    private String typeId;
    @SerializedName("typeName")
    @Expose
    private String typeName;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
