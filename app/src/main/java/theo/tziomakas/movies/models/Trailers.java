package theo.tziomakas.movies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Trailers implements Parcelable{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("iso_639_1")
    @Expose
    private String iso6391;
    @SerializedName("iso_3166_1")
    @Expose
    private String iso31661;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("site")
    @Expose
    private String site;
    @SerializedName("size")
    @Expose
    private Integer size;
    @SerializedName("type")
    @Expose
    private String type;

    protected Trailers(Parcel in) {
        id = in.readString();
        iso6391 = in.readString();
        iso31661 = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        if (in.readByte() == 0) {
            size = null;
        } else {
            size = in.readInt();
        }
        type = in.readString();
    }

    public static final Creator<Trailers> CREATOR = new Creator<Trailers>() {
        @Override
        public Trailers createFromParcel(Parcel in) {
            return new Trailers(in);
        }

        @Override
        public Trailers[] newArray(int size) {
            return new Trailers[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso6391() {
        return iso6391;
    }

    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    public String getIso31661() {
        return iso31661;
    }

    public void setIso31661(String iso31661) {
        this.iso31661 = iso31661;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(iso6391);
        parcel.writeString(iso31661);
        parcel.writeString(key);
        parcel.writeString(name);
        parcel.writeString(site);
        if (size == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(size);
        }
        parcel.writeString(type);
    }
}
