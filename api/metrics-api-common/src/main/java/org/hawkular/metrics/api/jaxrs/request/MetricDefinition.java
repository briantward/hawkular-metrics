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
package org.hawkular.metrics.api.jaxrs.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModel;
import org.hawkular.metrics.core.api.Metric;

import java.util.Map;
import java.util.Objects;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

/**
 * @author jsanda
 */
@ApiModel(description = "The definition of a metric to create")
@org.codehaus.jackson.map.annotate.JsonSerialize(
        include = org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_EMPTY)
public class MetricDefinition {

    // TODO Do we need this?
    @JsonProperty
    @org.codehaus.jackson.annotate.JsonProperty
    @org.codehaus.jackson.map.annotate.JsonSerialize(
            include = org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_EMPTY)
    private String tenantId;

    @JsonProperty
    @org.codehaus.jackson.annotate.JsonProperty
    @org.codehaus.jackson.map.annotate.JsonSerialize(
            include = org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_EMPTY)
    private String id;

    @JsonProperty
    @org.codehaus.jackson.annotate.JsonProperty
    @org.codehaus.jackson.map.annotate.JsonSerialize(
            include = org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_EMPTY)
    private Map<String, String> tags;

    @JsonProperty
    @org.codehaus.jackson.annotate.JsonProperty
    @org.codehaus.jackson.map.annotate.JsonSerialize(
            include = org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_EMPTY)
    private Integer dataRetention;

    public MetricDefinition() {
    }

    @JsonCreator
    public MetricDefinition(
            @JsonProperty("id") String id,
            @JsonProperty(value = "tags") Map<String, String> tags,
            @JsonProperty("dataRetention") Integer dataRetention) {
        this.id = id;
        this.tags = tags == null ? emptyMap() : unmodifiableMap(tags);
        this.dataRetention = dataRetention;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getId() {
        return id;
    }

    public Map<String, String> getTags() {
        if (tags == null) {
            return emptyMap();
        }
        return tags;
    }

    public Integer getDataRetention() {
        return dataRetention;
    }

    @SuppressWarnings("unchecked")
    public MetricDefinition(Metric metric) {
        this.tenantId = metric.getTenantId();
        this.id = metric.getId().getName();
        this.tags = metric.getTags();
        this.dataRetention = metric.getDataRetention();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetricDefinition gauge = (MetricDefinition) o;
        return Objects.equals(id, gauge.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MetricDefinition{" +
                "tenantId='" + tenantId + '\'' +
                ", id='" + id + '\'' +
                ", tags=" + tags +
                ", dataRetention=" + dataRetention +
                '}';
    }
}
