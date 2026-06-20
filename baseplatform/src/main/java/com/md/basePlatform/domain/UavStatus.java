package com.md.basePlatform.domain;

/**
 * 无人机状态枚举。
 */
public enum UavStatus {
    
    /** 空闲 */
    IDLE("空闲"),
    
    /** 飞行中 */
    FLYING("飞行中"),
    
    /** 充电中 */
    CHARGING("充电中"),
    
    /** 故障 */
    FAULT("故障"),
    
    /** 维修中 */
    MAINTENANCE("维修中"),
    
    /** 报废 */
    SCRAPPED("报废");
    
    private final String description;
    
    UavStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static UavStatus fromString(String value) {
        if (value == null) {
            return IDLE;
        }
        try {
            return UavStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return IDLE;
        }
    }
}