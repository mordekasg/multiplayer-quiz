package pl.edu.uwr.pum.pumproject2.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_category")
public class CategoryEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "mod_at")
    private long mod_at;

    public CategoryEntity(@NonNull int id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return this.id; }

    public String getName() { return this.name; }

    public long getMod_at() { return this.mod_at; }

    public void setMod_at(long mod_at) { this.mod_at = mod_at; }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
