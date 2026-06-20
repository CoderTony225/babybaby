package com.md.basePlatform.service.ai.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UavAttributeAiClientStubTest {

    @Test
    void should_return_json() {
        UavAttributeAiClientStub stub = new UavAttributeAiClientStub();
        String json = stub.generateAttributes("FSN", "Phantom", "note");
        assertTrue(json.contains("Phantom"));
        assertTrue(json.contains("FSN"));
    }
}
