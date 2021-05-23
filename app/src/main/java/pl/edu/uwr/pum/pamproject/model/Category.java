package pl.edu.uwr.pum.pamproject.model;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("mod_at")
    public long mod_at;
}
