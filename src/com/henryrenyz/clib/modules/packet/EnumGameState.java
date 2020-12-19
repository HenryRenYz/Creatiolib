package com.henryrenyz.clib.modules.packet;

public enum EnumGameState {
    NoRespawnBlockAvailable(0,"a"),
    EndRaining(1,"b"),
    BeginRaining(2, "c"),
    ChangeGamemode(3, "d"),
    WinGame(4, "e"),
    DemoEvent(5, "f"),
    ArrowHitPlayer(6, "g"),
    RainLevelChange(7, "h"),
    ThunderLevelChange(8, "i"),
    PlayPufferfishStingSound(9, "j"),
    PlayElderGuardianMobAppearance(10, "k"),
    EnableRespawnScreen(11, "l");

    private Integer value;
    private String code;

    private EnumGameState(Integer value, String code) {
        this.value = value;
        this.code = code;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getCode() {
        return this.code;
    }
}
