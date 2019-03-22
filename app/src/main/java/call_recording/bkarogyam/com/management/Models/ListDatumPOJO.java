package call_recording.bkarogyam.com.management.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by satyam on 15/6/18.
 */

public class ListDatumPOJO {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("emp")
    @Expose
    private String emp;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("subtitle")
    @Expose
    private String subtitle;
    @SerializedName("mobile")
    @Expose
    private String mobile;
//    @SerializedName("hide")
//    @Expose
//    private String hide;
    @SerializedName("manager")
    @Expose
    private String manager;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("disposition")
    @Expose
    private List<String> disposition = null;
    @SerializedName("complete")
    @Expose
    private String complete;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmp() {
        return emp;
    }

    public void setEmp(String emp) {
        this.emp = emp;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

//    public String getHide() {
//        return hide;
//    }
//
//    public void setHide(String hide) {
//        this.hide = hide;
//    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public List<String> getDisposition() {
        return disposition;
    }

    public void setDisposition(List<String> disposition) {
        this.disposition = disposition;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }
}
