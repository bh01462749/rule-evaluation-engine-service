package com.hack.rule_evaluation_engine_service.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FiredRule {
    private String rule;
    private boolean result;
    private String reason;
}
