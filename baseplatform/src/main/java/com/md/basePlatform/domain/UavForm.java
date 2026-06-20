package com.md.basePlatform.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 无人机表单（录入 / 编辑）。
 */
public class UavForm {

    private Long id;

    @NotBlank
    @Size(max = 64)
    private String frameSn;

    @NotBlank
    @Size(max = 128)
    private String modelName;

    // 新增字段：制造厂商
    @Size(max = 128)
    private String manufacturer;

    // 新增字段：无人机状态
    private String status;

    // 硬件参数
    private Integer maxFlightAltitude;      // 最大飞行高度（米）
    private Integer maxFlightSpeed;         // 最大飞行速度（km/h）
    private Integer flightDuration;         // 续航时间（分钟）
    private Integer batteryCapacity;        // 电池容量（mAh）
    private Integer payloadCapacity;        // 载重能力（克）

    @Size(max = 512)
    private String notes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrameSn() {
        return frameSn;
    }

    public void setFrameSn(String frameSn) {
        this.frameSn = frameSn;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getMaxFlightAltitude() {
        return maxFlightAltitude;
    }

    public void setMaxFlightAltitude(Integer maxFlightAltitude) {
        this.maxFlightAltitude = maxFlightAltitude;
    }

    public Integer getMaxFlightSpeed() {
        return maxFlightSpeed;
    }

    public void setMaxFlightSpeed(Integer maxFlightSpeed) {
        this.maxFlightSpeed = maxFlightSpeed;
    }

    public Integer getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(Integer flightDuration) {
        this.flightDuration = flightDuration;
    }

    public Integer getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(Integer batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public Integer getPayloadCapacity() {
        return payloadCapacity;
    }

    public void setPayloadCapacity(Integer payloadCapacity) {
        this.payloadCapacity = payloadCapacity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}