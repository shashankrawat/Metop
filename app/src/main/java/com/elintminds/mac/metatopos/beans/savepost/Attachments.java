package com.elintminds.mac.metatopos.beans.savepost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attachments {
    @SerializedName("attachmentpath")
    @Expose
    private String attachment;

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
