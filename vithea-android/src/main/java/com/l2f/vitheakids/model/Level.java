package com.l2f.vitheakids.model;

/**
 * Created by silvi on 20/04/2017.
 * Updated by Soraia Meneses Alarcão on 21/07/2017
 */

public class Level {

    private Long levelId;
    private String levelDescription;

    public Long getLevelId() {
        return levelId;
    }
    public String getLevelDescription() {
        return levelDescription;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }
    public void setLevelDescription(String levelDescription) {
        this.levelDescription = levelDescription;
    }

}
