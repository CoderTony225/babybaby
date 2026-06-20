package com.md.basePlatform.service.ai;

/**
 * 无人机扩展属性 AI 生成客户端。
 */
public interface UavAttributeAiClient {

    /**
     * 根据简要输入生成扩展属性 JSON 字符串。
     *
     * @param frameSn 机身编号
     * @param modelName 型号
     * @param notes 备注，可为 null
     * @return JSON 字符串
     * @throws RuntimeException 调用失败时抛出，由上层标记 FAILED
     */
    String generateAttributes(String frameSn, String modelName, String notes);
}
