package com.wolasoft.bakingapp.data.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable {
    private final int id;
    private final String shortDescription;
    private final String description;
    private final String videoURL;
    private final String thumbnailURL;

    private Step(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Step)) return false;

        Step step = (Step) o;

        if (id != step.id) return false;
        if (shortDescription != null ? !shortDescription.equals(step.shortDescription) : step.shortDescription != null)
            return false;
        if (description != null ? !description.equals(step.description) : step.description != null)
            return false;
        if (videoURL != null ? !videoURL.equals(step.videoURL) : step.videoURL != null)
            return false;
        return thumbnailURL != null ? thumbnailURL.equals(step.thumbnailURL) : step.thumbnailURL == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (shortDescription != null ? shortDescription.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (videoURL != null ? videoURL.hashCode() : 0);
        result = 31 * result + (thumbnailURL != null ? thumbnailURL.hashCode() : 0);
        return result;
    }
}
