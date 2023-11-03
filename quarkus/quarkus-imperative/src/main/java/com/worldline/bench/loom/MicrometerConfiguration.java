package com.worldline.bench.loom;

import io.github.mweirauch.micrometer.jvm.extras.ProcessMemoryMetrics;
import io.github.mweirauch.micrometer.jvm.extras.ProcessThreadMetrics;
import io.micrometer.core.instrument.binder.MeterBinder;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;

@Dependent
public class MicrometerConfiguration {
    
    @Produces
    public MeterBinder processMemoryMetrics() {
        return new ProcessMemoryMetrics();
    }

    @Produces
    public MeterBinder processThreadMetrics() {
        return new ProcessThreadMetrics();
    }
}
