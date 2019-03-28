package com.kk666.enums;

public enum GameIdEnum {
    FFSSC("分分时时彩", 54),
    SFSSC("三分时时彩", 51),
    CQSSC("重庆时时彩", 5),
    XJSSC("新疆时时彩", 7)
    ;

    private String gameName;
    private int gameId;

    GameIdEnum(String gameName, int gameId) {
        this.gameName = gameName;
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public int getGameId() {
        return gameId;
    }
}
