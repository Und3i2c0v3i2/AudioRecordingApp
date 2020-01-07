package practice.und3i2c0v3i2.audiorecordingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "records_table")
public class RecordItem implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo
    private String genName;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private long length;
    @ColumnInfo
    private String path;
    @ColumnInfo
    private String timestamp;
    @ColumnInfo
    private boolean isUploaded;


    public RecordItem() {
    }

    @Ignore
    public RecordItem(String genName, String name, long length, String path, String timestamp, boolean isUploaded) {
        this.genName = genName;
        this.name = name;
        this.length = length;
        this.path = path;
        this.timestamp = timestamp;
        this.isUploaded = isUploaded;
    }

    protected RecordItem(Parcel in) {
        genName = in.readString();
        name = in.readString();
        length = in.readLong();
        path = in.readString();
        timestamp = in.readString();
        isUploaded = in.readByte() != 0;
    }

    public static final Creator<RecordItem> CREATOR = new Creator<RecordItem>() {
        @Override
        public RecordItem createFromParcel(Parcel in) {
            return new RecordItem(in);
        }

        @Override
        public RecordItem[] newArray(int size) {
            return new RecordItem[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGenName() {
        return genName;
    }

    public void setGenName(String genName) {
        this.genName = genName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(genName);
        dest.writeString(name);
        dest.writeLong(length);
        dest.writeString(path);
        dest.writeString(timestamp);
        dest.writeByte((byte) (isUploaded ? 1 : 0));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordItem that = (RecordItem) o;
        return getId() == that.getId() &&
                getLength() == that.getLength() &&
                isUploaded() == that.isUploaded() &&
                getGenName().equals(that.getGenName()) &&
                getName().equals(that.getName()) &&
                getPath().equals(that.getPath()) &&
                getTimestamp().equals(that.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getGenName(), getName(), getLength(), getPath(), getTimestamp(), isUploaded());
    }
}
