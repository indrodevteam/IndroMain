package com.github.indrodevteam.indroMain.ranks;

import com.google.gson.annotations.SerializedName;

public enum RankConfigTags {
    @SerializedName("${SECRET}")
    SECRET(false),
    @SerializedName("${TIME_TO_WARP}")
    TIME_TO_WARP(10);

    final Object defaultValue;
    RankConfigTags(Object defaultValue) {
        this.defaultValue = defaultValue;
    }
}
