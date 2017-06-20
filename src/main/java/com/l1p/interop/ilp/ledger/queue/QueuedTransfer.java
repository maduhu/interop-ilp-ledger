package com.l1p.interop.ilp.ledger.queue;

import com.l1p.interop.ilp.ledger.domain.Transfer;

public class QueuedTransfer {
    final private TransferState transferState;
    final private Transfer transferDetails;

    public QueuedTransfer(TransferState transferState, Transfer transferDetails) {
        this.transferState = transferState;
        this.transferDetails = transferDetails;
    }

    public TransferState getTransferState() {
        return transferState;
    }

    public Transfer getTransferDetails() {
        return transferDetails;
    }
}
