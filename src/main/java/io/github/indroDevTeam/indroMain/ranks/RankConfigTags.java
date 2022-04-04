package io.github.indroDevTeam.indroMain.ranks;

public enum RankConfigTags {
    SECRET(false),
    TIME_TO_WARP(10);

    final Object defaultValue;
    RankConfigTags(Object defaultValue) {
        this.defaultValue = defaultValue;
    }
}
