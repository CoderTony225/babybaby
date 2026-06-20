package com.md.basePlatform.service.ai.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.md.basePlatform.service.ai.UavAttributeAiClient;

/**
 * 占位实现：根据型号等生成确定性 JSON，便于开发与集成测试；生产可替换为真实 HTTP AI 调用。
 */
@Service
public class UavAttributeAiClientStub implements UavAttributeAiClient {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String generateAttributes(String frameSn, String modelName, String notes) {
        Map<String, Object> map = new LinkedHashMap<>();
        
        // 生成制造商（根据型号推断）
        String manufacturer = inferManufacturer(modelName);
        map.put("manufacturer", manufacturer);
        
        // 默认状态为空闲
        map.put("status", "IDLE");
        
        // 硬件参数
        Map<String, Object> hardware = new LinkedHashMap<>();
        hardware.put("maxFlightAltitude", 500);  // 最大飞行高度（米）
        hardware.put("maxFlightSpeed", 80);      // 最大飞行速度（km/h）
        hardware.put("flightDuration", 28);      // 续航时间（分钟）
        hardware.put("batteryCapacity", 5300);   // 电池容量（mAh）
        hardware.put("payloadCapacity", 500);    // 载重能力（克）
        map.put("hardware", hardware);
        
        // 使用信息
        Map<String, Object> usage = new LinkedHashMap<>();
        usage.put("totalFlightHours", 0);        // 总飞行时长（小时）
        usage.put("totalFlightCount", 0);        // 累计飞行次数
        usage.put("lastFlightTime", null);       // 最后飞行时间
        map.put("usage", usage);
        
        // 维保信息
        Map<String, Object> maintenance = new LinkedHashMap<>();
        maintenance.put("firstUseDate", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        maintenance.put("lastMaintenanceDate", null);
        maintenance.put("nextMaintenanceDate", null);
        maintenance.put("maintenanceInterval", 25);  // 维护周期（小时）
        map.put("maintenance", maintenance);
        
        // 额外信息
        map.put("frameSnRef", frameSn);
        map.put("modelNameRef", modelName);
        map.put("generationSource", "Stub AI");
        if (notes != null && !notes.isEmpty()) {
            map.put("notesProcessed", notes);
        }
        
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("serialize ai attributes", e);
        }
    }
    
    private String inferManufacturer(String modelName) {
        if (modelName == null) {
            return "未知厂商";
        }
        String lower = modelName.toLowerCase();
        if (lower.contains("dji") || lower.contains("大疆")) {
            return "大疆创新科技有限公司";
        } else if (lower.contains("autel") || lower.contains("道通")) {
            return "道通智能科技股份有限公司";
        } else if (lower.contains("parrot") || lower.contains("派诺特")) {
            return "Parrot SA";
        } else if (lower.contains("yuneec") || lower.contains("昊翔")) {
            return "昊翔电能运动科技（昆山）有限公司";
        } else if (lower.contains("xiaomi") || lower.contains("小米")) {
            return "小米科技有限责任公司";
        } else if (lower.contains("hubsan") || lower.contains("哈博森")) {
            return "深圳市哈博森科技有限公司";
        } else if (lower.contains("syma") || lower.contains("司马")) {
            return "广东司马航模实业有限公司";
        } else {
            return "未知厂商";
        }
    }
}