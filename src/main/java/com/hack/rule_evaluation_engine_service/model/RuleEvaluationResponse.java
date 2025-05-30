package com.hack.rule_evaluation_engine_service.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class RuleEvaluationResponse {
    private boolean approved;
    private String finalDecision;
    private List<FiredRule> firedRules;
}