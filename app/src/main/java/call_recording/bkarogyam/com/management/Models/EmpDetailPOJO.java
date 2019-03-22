package call_recording.bkarogyam.com.management.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by satyam on 29/7/18.
 */

public class EmpDetailPOJO {
    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
