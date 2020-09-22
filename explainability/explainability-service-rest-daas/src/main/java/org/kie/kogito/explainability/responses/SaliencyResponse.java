package org.kie.kogito.explainability.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaliencyResponse {

    @JsonProperty("outcomeId")
    @JsonInclude(NON_NULL)
    private String outcomeId;

    @JsonProperty("outcomeName")
    @JsonInclude(NON_NULL)
    private String outcomeName;

    @JsonProperty("featureImportance")
    private List<FeatureImportanceResponse> featureImportance;

    private SaliencyResponse() {
    }

    public SaliencyResponse(String outcomeId, String outcomeName, List<FeatureImportanceResponse> featureImportance) {
        this.outcomeId = outcomeId;
        this.outcomeName = outcomeName;
        this.featureImportance = featureImportance;
    }

    public String getOutcomeId() {
        return outcomeId;
    }

    public String getOutcomeName() {
        return outcomeName;
    }

    public List<FeatureImportanceResponse> getFeatureImportance() {
        return featureImportance;
    }
}
