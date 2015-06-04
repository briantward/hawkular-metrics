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

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * @author jsanda
 */
public class GaugeDataPoint implements DataPoint<Double> {

    private final long timestamp;

    private final Double value;

    private Map<String, String> tags = Collections.emptyMap();

    public GaugeDataPoint(long timestamp, Double value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public GaugeDataPoint(long timestamp, Double value, Map<String, String> tags) {
        this.timestamp = timestamp;
        this.value = value;
        this.tags = tags;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public Map<String, String> getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GaugeDataPoint that = (GaugeDataPoint) o;
        // TODO should tags be included in equals?
//        return Objects.equals(timestamp, that.timestamp) &&
//                Objects.equals(value, that.value) &&
//                Objects.equals(tags, that.tags);
        return Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        // TODO should tags be included?
        return Objects.hash(timestamp, value);
    }

    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper("GaugeDataPoint")
                .add("timestamp", timestamp)
                .add("value", value)
                .add("tags", tags)
                .toString();
    }
}
