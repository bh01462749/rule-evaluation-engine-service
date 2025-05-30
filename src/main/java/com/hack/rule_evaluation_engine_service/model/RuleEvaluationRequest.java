package com.hack.rule_evaluation_engine_service.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
public class RuleEvaluationRequest {
    private Map<String, Object> transaction;
    private List<String> rules;
}