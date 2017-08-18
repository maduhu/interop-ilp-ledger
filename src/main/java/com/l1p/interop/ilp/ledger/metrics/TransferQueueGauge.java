package com.l1p.interop.ilp.ledger.metrics;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.l1p.interop.ilp.ledger.queue.TransferQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransferQueueGauge {

    private static final Logger log = LoggerFactory.getLogger(TransferQueue.class);

    public TransferQueueGauge(final MetricRegistry metricRegistry, final TransferQueue transferQueue) {
        metricRegistry.register(MetricRegistry.name("l1p.interop-ilp-ledger.transfer.queue.prepares.size"), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return transferQueue.getStats().preparesSize;
            }
        });

        metricRegistry.register(MetricRegistry.name("l1p.interop-ilp-ledger.transfer.queue.fulfills.size"), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return transferQueue.getStats().fulfillsSize;
            }
        });

        metricRegistry.register(MetricRegistry.name("l1p.interop-ilp-ledger.transfer.queue.cancels.size"), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return transferQueue.getStats().cancelsSize;
            }
        });

        log.info("TransferQueueGauges installed");
    }

}
