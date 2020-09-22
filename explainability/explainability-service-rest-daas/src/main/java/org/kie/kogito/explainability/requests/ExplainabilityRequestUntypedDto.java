/*
 *  Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.kie.kogito.explainability.requests;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.kie.kogito.explainability.api.ModelIdentifierDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExplainabilityRequestUntypedDto {

    @JsonProperty("serviceUrl")
    @NotBlank(message = "serviceUrl is mandatory.")
    private String serviceUrl;

    @JsonProperty("modelIdentifier")
    @NotNull(message = "modelIdentifier object must be provided.")
    @Valid
    private ModelIdentifierDto modelIdentifier;

    @JsonProperty("inputs")
    private JsonNode inputs;

    @JsonProperty("outputs")
    private JsonNode outputs;

    private ExplainabilityRequestUntypedDto() {
    }

    public ExplainabilityRequestUntypedDto(String serviceUrl, ModelIdentifierDto modelIdentifier, JsonNode inputs, JsonNode outputs) {
        this.serviceUrl = serviceUrl;
        this.modelIdentifier = modelIdentifier;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public ModelIdentifierDto getModelIdentifier() {
        return modelIdentifier;
    }

    public JsonNode getInputs() {
        return inputs;
    }

    public JsonNode getOutputs() {
        return outputs;
    }
}
