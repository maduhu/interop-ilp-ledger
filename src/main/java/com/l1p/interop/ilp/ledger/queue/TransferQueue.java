package com.l1p.interop.ilp.ledger.queue;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l1p.interop.ilp.ledger.domain.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static com.codahale.metrics.MetricRegistry.name;
import static com.l1p.interop.ilp.ledger.queue.TransferState.*;

public class TransferQueue {
    private final Logger log = LoggerFactory.getLogger(TransferQueue.class);

    final PriorityBlockingQueue<QueuedTransfer> priorityQueue;
    final AtomicInteger prepareCount;
    final AtomicInteger fulfillCount;
    final AtomicInteger cancelCount;
    final Map<String, Date> tranferExpiryMap = new ConcurrentHashMap<>();
    final int preparesInQueueLimit;
    final int cancelsInQueueLimit;
    final MetricRegistry metricRegistry;

    private final ObjectMapper mapper;

    public TransferQueue(int preparesInQueueLimit, int cancelsInQueueLimit, final MetricRegistry metricRegistry) {
        this.preparesInQueueLimit = preparesInQueueLimit;
        this.cancelsInQueueLimit = cancelsInQueueLimit;
        Comparator comparator = Comparator.comparing(QueuedTransfer::getTransferState).thenComparing(QueuedTransfer::getTimestamp);
        priorityQueue = new PriorityBlockingQueue<QueuedTransfer>(1000, comparator);

        prepareCount = new AtomicInteger(0);
        fulfillCount = new AtomicInteger(0);
        cancelCount = new AtomicInteger(0);

        mapper = new ObjectMapper();
        this.metricRegistry = metricRegistry;
    }

    public void addPrepare(String transferJson) throws IOException {
        final Transfer transfer = mapper.readValue(transferJson, Transfer.class);
        log.warn("Transfer Id: {}",transfer.getId());
        tranferExpiryMap.put(transfer.getId(), transfer.getExpiresAt());
        if (prepareCount.get() > preparesInQueueLimit) {
            log.warn("Prepare msg discarded b/c limit reached");
            metricRegistry.counter(name("l1p.interop-ilp-ledger.transfer.queue.prepares.drop")).inc();
        } else {
            boolean added = priorityQueue.add(new QueuedTransfer(PREPARE, transfer));
            if (added) {
                prepareCount.incrementAndGet();
                metricRegistry.counter(name("l1p.interop-ilp-ledger.transfer.queue.prepares.size")).inc();
            }
        }
    }

    public void addFulfill(String transferId, String executionCondition) {
        Transfer transfer = new Transfer();
        transfer.setId(transferId);
        transfer.setExecutionCondition(executionCondition);
        transfer.setExpiresAt(tranferExpiryMap.getOrDefault(transferId, new Date()));

        QueuedTransfer queuedTransfer = new QueuedTransfer(FULFILL, transfer);
        boolean added = priorityQueue.add(queuedTransfer);
        if (added) {
            fulfillCount.incrementAndGet();
            metricRegistry.counter(name("l1p.interop-ilp-ledger.transfer.queue.fulfills.size")).inc();
        }
    }

    public void addCancel(String transferId, String rejectionReason) {
        Transfer transfer = new Transfer();
        transfer.setId(transferId);
        transfer.setRejectionReason(rejectionReason);
        transfer.setExpiresAt(tranferExpiryMap.getOrDefault(transferId, new Date()));

        QueuedTransfer queuedTransfer = new QueuedTransfer(CANCEL, transfer);
        if (cancelCount.get() > cancelsInQueueLimit) {
            log.warn("Cancel msg discarded b/c limit reached");
            metricRegistry.counter(name("l1p.interop-ilp-ledger.transfer.queue.cancels.drop")).inc();
        } else {
            boolean added = priorityQueue.add(queuedTransfer);
            if (added) cancelCount.incrementAndGet();
            metricRegistry.counter(name("l1p.interop-ilp-ledger.transfer.queue.cancels.size")).inc();
        }
    }


    public QueuedTransfer takeNext() {
        QueuedTransfer queuedTransfer = priorityQueue.poll();
        if (queuedTransfer == null) return null;

        if (queuedTransfer.getTransferState() == PREPARE) {
            prepareCount.decrementAndGet();
            metricRegistry.counter(name("l1p.interop-ilp-ledger.transfer.queue.prepares.size")).dec();
        }
        else if (queuedTransfer.getTransferState() == FULFILL) {
            fulfillCount.decrementAndGet();
            metricRegistry.counter(name("l1p.interop-ilp-ledger.transfer.queue.fulfills.size")).dec();
        }
        else {
            cancelCount.decrementAndGet();
            metricRegistry.counter(name("l1p.interop-ilp-ledger.transfer.queue.cancels.size")).dec();
        }

        return queuedTransfer;
    }

    public TransferQueueStats getStats() {
        log.info("Providing stats");
        return new TransferQueueStats(prepareCount.get(), fulfillCount.get(), cancelCount.get());
    }

    public static class TransferQueueStats {
        public final int preparesSize, fulfillsSize, cancelsSize;

        public TransferQueueStats(int preparesSize, int fulfillsSize, int cancelsSize) {
            this.preparesSize = preparesSize;
            this.fulfillsSize = fulfillsSize;
            this.cancelsSize = cancelsSize;
        }

        public int getPreparesSize() {
            return preparesSize;
        }

        public int getFulfillsSize() {
            return fulfillsSize;
        }

        public int getCancelsSize() {
            return cancelsSize;
        }
    }

}
