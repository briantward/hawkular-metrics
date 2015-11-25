/*
 * Copyright 2014-2015 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hawkular.metrics.core.api;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;
import java.util.Map;

import org.hawkular.metrics.core.api.fasterxml.jackson.MetricTypeDeserializer;
import org.hawkular.metrics.core.api.fasterxml.jackson.MetricTypeSerializer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import rx.Observable;

/**
 * @author John Sanda
 */
@ApiModel(value = "Metric", description = "The definition of a metric")
public class MetricDefinition<S> {
    private final String tenantId;
    private final String id;
    private final Map<String, String> tags;
    private final Integer dataRetention;
    private final MetricType<?> type;
    private final List<DataPoint<S>> data;

    @JsonCreator(mode = Mode.PROPERTIES)
    @org.codehaus.jackson.annotate.JsonCreator
    public MetricDefinition(
            @JsonProperty("id")
            @org.codehaus.jackson.annotate.JsonProperty("id")
            String id,
            @JsonProperty(value = "tags")
            @org.codehaus.jackson.annotate.JsonProperty("tags")
            Map<String, String> tags,
            @JsonProperty("dataRetention")
            @org.codehaus.jackson.annotate.JsonProperty("dataRetention")
            Integer dataRetention,
            @JsonProperty("type")
            @org.codehaus.jackson.annotate.JsonProperty("type")
            @JsonDeserialize(using = MetricTypeDeserializer.class)
            @org.codehaus.jackson.map.annotate.JsonDeserialize(
                    using = org.hawkular.metrics.core.api.codehaus.jackson.MetricTypeDeserializer.class
            )
            MetricType<?> type,
            @JsonProperty("data") @org.codehaus.jackson.annotate.JsonProperty("data")
            List<DataPoint<S>> data
    ) {
        checkArgument(id != null, "Metric id is null");
        this.tenantId = null;
        this.id = id;
        this.tags = tags == null ? emptyMap() : unmodifiableMap(tags);
        this.dataRetention = dataRetention;
        this.type = type;
        this.data = data == null || data.isEmpty() ? emptyList() : unmodifiableList(data);
    }

    public MetricDefinition(Metric<?> metric) {
        this.tenantId = metric.getId().getTenantId();
        this.id = metric.getId().getName();
        this.type = metric.getId().getType();
        this.tags = metric.getTags();
        this.dataRetention = metric.getDataRetention();
        this.data = emptyList();
    }

    @ApiModelProperty("Identifier of the tenant")
    public String getTenantId() {
        return tenantId;
    }

    @ApiModelProperty(value = "Identifier of the metric", required = true)
    public String getId() {
        return id;
    }

    @ApiModelProperty("Metric tags")
    @JsonSerialize(include = Inclusion.NON_EMPTY)
    @org.codehaus.jackson.map.annotate.JsonSerialize(
            include = org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_EMPTY
    )
    public Map<String, String> getTags() {
        return tags;
    }

    @ApiModelProperty("How long, in days, a data point of this metric stays in the system after it is stored")
    public Integer getDataRetention() {
        return dataRetention;
    }

    @ApiModelProperty(value = "Metric type", dataType = "string", allowableValues = "gauge, availability, counter")
    @JsonSerialize(using = MetricTypeSerializer.class)
    @org.codehaus.jackson.map.annotate.JsonSerialize(
            using = org.hawkular.metrics.core.api.codehaus.jackson.MetricTypeSerializer.class
    )
    public MetricType<?> getType() {
        return type;
    }

    @ApiModelProperty("Metric data points")
    @JsonSerialize(include = Inclusion.NON_EMPTY)
    @org.codehaus.jackson.map.annotate.JsonSerialize(
            include = org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_EMPTY
    )
    public List<DataPoint<S>> getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        @SuppressWarnings("rawtypes")
        MetricDefinition that = (MetricDefinition) o;
        return id.equals(that.id) && type == that.type;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("tenantId", tenantId)
                .add("id", id)
                .add("tags", tags)
                .add("dataRetention", dataRetention)
                .add("type", type)
                .add("data", data)
                .omitNullValues()
                .toString();
    }

    public static <S, T extends MetricDefinition<S>> Observable<Metric<S>> toObservable(
            String tenantId, List<T> metrics, MetricType<S> type) {
        return Observable.from(metrics).map(g -> {
            return new Metric<S>(new MetricId<S>(tenantId, type, g.getId()), g.getData());
        });
    }
}
