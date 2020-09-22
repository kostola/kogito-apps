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

package org.kie.kogito.explainability.rest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.JsonNode;
import org.kie.kogito.explainability.api.ExplainabilityResultDto;
import org.kie.kogito.explainability.api.FeatureImportanceDto;
import org.kie.kogito.explainability.api.SaliencyDto;
import org.kie.kogito.explainability.models.ExplainabilityRequest;
import org.kie.kogito.explainability.models.ModelIdentifier;
import org.kie.kogito.explainability.requests.ExplainabilityRequestUntypedDto;
import org.kie.kogito.explainability.responses.FeatureImportanceResponse;
import org.kie.kogito.explainability.responses.SalienciesResponse;
import org.kie.kogito.explainability.responses.SaliencyResponse;
import org.kie.kogito.tracing.typedvalue.CollectionValue;
import org.kie.kogito.tracing.typedvalue.StructureValue;
import org.kie.kogito.tracing.typedvalue.TypedValue;
import org.kie.kogito.tracing.typedvalue.UnitValue;

public class DtoUtils {

    public static Optional<ExplainabilityRequest> explainabilityRequestFrom(ExplainabilityRequestUntypedDto dto) {
        try {
            return Optional.of(new ExplainabilityRequest(
                    null,
                    dto.getServiceUrl(),
                    ModelIdentifier.from(dto.getModelIdentifier()),
                    typedValueMapFrom(dto.getInputs()),
                    typedValueMapFrom(dto.getOutputs())
            ));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static SalienciesResponse salienciesResponseFrom(ExplainabilityResultDto dto) {
        return new SalienciesResponse(
                dto.getStatus().toString(),
                dto.getStatusDetails(),
                dto.getSaliencies() == null ? null : dto.getSaliencies().entrySet().stream()
                        .map(DtoUtils::saliencyResponseFrom)
                        .collect(Collectors.toList())
        );
    }

    private static FeatureImportanceResponse featureImportanceResponseFrom(FeatureImportanceDto dto) {
        return new FeatureImportanceResponse(dto.getFeatureName(), dto.getScore());
    }

    private static SaliencyResponse saliencyResponseFrom(Map.Entry<String, SaliencyDto> entry) {
        return new SaliencyResponse(
                null,
                entry.getKey(),
                entry.getValue().getFeatureImportance().stream()
                        .map(DtoUtils::featureImportanceResponseFrom)
                        .collect(Collectors.toList())
        );
    }

    private static <T> Stream<T> streamFrom(Iterator<T> iterator) {
        Iterable<T> iterable = () -> iterator;
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    private static TypedValue typedValueFrom(JsonNode value) {
        if (value != null && value.isObject()) {
            return new StructureValue(
                    typeOf(value),
                    streamFrom(value.fields())
                            .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), typedValueFrom(entry.getValue())), HashMap::putAll)
            );
        }
        if (value != null && value.isArray()) {
            return new CollectionValue(
                    typeOf(value),
                    streamFrom(value.elements()).map(DtoUtils::typedValueFrom).collect(Collectors.toList())
            );
        }
        return new UnitValue(typeOf(value), value);
    }

    private static Map<String, TypedValue> typedValueMapFrom(JsonNode value) {
        if (value == null || !value.isObject()) {
            throw new IllegalArgumentException();
        }
        return streamFrom(value.fields())
                .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), typedValueFrom(entry.getValue())), HashMap::putAll);
    }

    private static String typeOf(JsonNode value) {
        if (value == null) {
            return "UNKNOWN";
        }
        if (value.isArray()) {
            return "LIST";
        }
        if (value.isBoolean()) {
            return "BOOLEAN";
        }
        if (value.isNumber()) {
            return "NUMBER";
        }
        if (value.isTextual()) {
            return "STRING";
        }
        return "UNKNOWN";
    }

    private DtoUtils() {
    }

}
