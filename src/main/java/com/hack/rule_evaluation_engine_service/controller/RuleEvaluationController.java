package com.hack.rule_evaluation_engine_service.controller;


import com.hack.rule_evaluation_engine_service.model.RuleEvaluationRequest;
import com.hack.rule_evaluation_engine_service.model.RuleEvaluationResponse;
import com.hack.rule_evaluation_engine_service.service.RuleEvaluationService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class RuleEvaluationController {

    private final RuleEvaluationService ruleEvaluationService;

    public RuleEvaluationController(RuleEvaluationService ruleEvaluationService) {
        this.ruleEvaluationService = ruleEvaluationService;
    }

    @PostMapping("/evaluate")
    public RuleEvaluationResponse evaluate(@RequestBody RuleEvaluationRequest request) {
        return ruleEvaluationService.evaluateRules(request);
    }
}
