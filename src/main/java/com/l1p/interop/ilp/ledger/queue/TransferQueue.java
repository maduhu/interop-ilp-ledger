package com.l1p.interop.ilp.ledger.queue;

import com.l1p.interop.ilp.ledger.domain.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.l1p.interop.ilp.ledger.queue.TransferState.*;

public class TransferQueue {
  private final Logger log = LoggerFactory.getLogger(TransferQueue.class);

  final PriorityQueue<QueuedTransfer> priorityQueue;
  final AtomicInteger prepareCount;
  final AtomicInteger fulfillCount;
  final AtomicInteger cancelCount;
  final Map<String, Date> tranferExpiryMap = new ConcurrentHashMap<>();

  public TransferQueue() {
    Comparator comparator = Comparator.comparing(QueuedTransfer::getTransferState);
    priorityQueue = new PriorityQueue(comparator);
    prepareCount = new AtomicInteger(0);
    fulfillCount = new AtomicInteger(0);
    cancelCount = new AtomicInteger(0);
  }

  public void addPrepare(Transfer transfer) {
    tranferExpiryMap.put(transfer.getId(), transfer.getExpiresAt());
    priorityQueue.add(new QueuedTransfer(PREPARE, transfer));
    if (transfer.getAdditionalInfo() != null) prepareCount.incrementAndGet();
  }

  public void addFufill(String transferId, String executionCondition) {
    Transfer transfer = new Transfer();
    transfer.setId(transferId);
    transfer.setExecutionCondition(executionCondition);
    transfer.setExpiresAt(tranferExpiryMap.getOrDefault(transferId, new Date()));
    QueuedTransfer queuedTransfer = new QueuedTransfer(FULFILL, transfer);
    priorityQueue.add(queuedTransfer);
    if (transfer.getAdditionalInfo() != null) fulfillCount.incrementAndGet();
  }

  public void addCancel(String transferId, String rejectionReason) {
    Transfer transfer = new Transfer();
    transfer.setId(transferId);
    transfer.setRejectionReason(rejectionReason);
    transfer.setExpiresAt(tranferExpiryMap.getOrDefault(transferId, new Date()));
    QueuedTransfer queuedTransfer = new QueuedTransfer(CANCEL, transfer);
    priorityQueue.add(queuedTransfer);
    if (transfer.getAdditionalInfo() != null) cancelCount.incrementAndGet();
  }


  public QueuedTransfer takeNext() {
    QueuedTransfer queuedTransfer = priorityQueue.poll();
    if (queuedTransfer.getTransferState() == PREPARE) prepareCount.decrementAndGet();
    else if (queuedTransfer.getTransferState() == FULFILL) fulfillCount.decrementAndGet();
    else cancelCount.decrementAndGet();

    return queuedTransfer;
  }

  public TransferQueueStats getStats() {
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
