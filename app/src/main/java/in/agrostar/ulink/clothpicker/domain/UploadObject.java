package in.agrostar.ulink.clothpicker.domain;

import com.amazonaws.mobileconnectors.s3.transfermanager.Transfer;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ayush on 28/4/17.
 */

public class UploadObject {
    @SerializedName("id")
    int id;

    @SerializedName("transferId")
    int transferId;

    @SerializedName("fileUrl")
    String fileUrl;

    @SerializedName("filePath")
    String filePath;
    @SerializedName("type")
    UploadType type;

    @SerializedName("state")
    TransferState state;

    public UploadObject() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public UploadType getType() {
        return type;
    }

    public void setType(UploadType type) {
        this.type = type;
    }

    public TransferState getState() {
        return state;
    }

    public void setState(TransferState state) {
        this.state = state;
    }


}
