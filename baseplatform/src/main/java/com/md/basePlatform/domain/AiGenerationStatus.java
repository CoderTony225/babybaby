package com.md.basePlatform.domain;

/**
 * AI 属性生成状态。
 */
public enum AiGenerationStatus {

    /** 生成成功 */
    SUCCESS,

    /** 生成失败（已降级保存其它字段） */
    FAILED,

    /** 跳过生成 */
    SKIPPED
}
