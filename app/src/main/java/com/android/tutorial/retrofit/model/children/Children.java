package com.android.tutorial.retrofit.model.children;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mannan on 8/29/2017.
 */

public class Children {
    @SerializedName("data")
    @Expose
    private Data data;

    @SerializedName("kind")
    @Expose
    private String kind;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    @Override
    public String toString() {
        return "Children{" +
                "data=" + data +
                ", kind='" + kind + '\'' +
                '}';
    }
}
