package com.hack.rule_evaluation_engine_service.service;

import com.hack.rule_evaluation_engine_service.model.FiredRule;
import com.hack.rule_evaluation_engine_service.model.RuleEvaluationRequest;
import com.hack.rule_evaluation_engine_service.model.RuleEvaluationResponse;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RuleEvaluationService {

    public RuleEvaluationResponse evaluateRules(RuleEvaluationRequest request) {
        List<FiredRule> firedRules = new ArrayList<>();
        boolean finalApproval = true;
        String finalDecision = "Approved - All rules passed";

        for (String rule : request.getRules()) {
            FiredRule firedRule = evaluateSingleRule(request.getTransaction(), rule);
            firedRules.add(firedRule);

            if (!firedRule.isResult()) {
                finalApproval = false;
                finalDecision = "Declined - Rule failed: " + firedRule.getReason();
//                break; // Stop evaluating further rules if one fails
            }
        }

        RuleEvaluationResponse response = new RuleEvaluationResponse();
        response.setApproved(finalApproval);
        response.setFinalDecision(finalDecision);
        response.setFiredRules(firedRules);

        return response;
    }

    private FiredRule evaluateSingleRule(Map<String, Object> transaction, String rule) {
        try (Context context = Context.create("js")) {
            // Bind transaction properties to JS context
            bindTransactionToContext(context, transaction);

            // Create result object and evaluate rule
            String fullScript = "var result = {approved: true, reason: 'Rule passed'};\n" +
                    "try {\n" +
                    rule + "\n" +
                    "} catch (e) {\n" +
                    "  result.approved = false;\n" +
                    "  result.reason = 'Rule evaluation error: ' + e.message;\n" +
                    "}\n" +
                    "result;";

            Value result = context.eval("js", fullScript);

            FiredRule firedRule = new FiredRule();
            firedRule.setRule(rule);
            firedRule.setResult(result.getMember("approved").asBoolean());
            firedRule.setReason(result.getMember("reason").asString());

            return firedRule;
        } catch (Exception e) {
            FiredRule errorRule = new FiredRule();
            errorRule.setRule(rule);
            errorRule.setResult(false);
            errorRule.setReason("Error evaluating rule: " + e.getMessage());
            return errorRule;
        }
    }

    private void bindTransactionToContext(Context context, Map<String, Object> transaction) {
        for (Map.Entry<String, Object> entry : transaction.entrySet()) {
            if (entry.getValue() instanceof Map) {
                // Handle nested objects like additionalData
                Map<String, Object> nestedMap = (Map<String, Object>) entry.getValue();
                Value jsObject = context.eval("js", "({})");
                for (Map.Entry<String, Object> nestedEntry : nestedMap.entrySet()) {
                    jsObject.putMember(nestedEntry.getKey(), nestedEntry.getValue());
                }
                context.getBindings("js").putMember(entry.getKey(), jsObject);
            } else {
                context.getBindings("js").putMember(entry.getKey(), entry.getValue());
            }
        }
    }
}