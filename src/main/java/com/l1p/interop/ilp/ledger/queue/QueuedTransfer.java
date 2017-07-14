package com.l1p.interop.ilp.ledger.queue;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l1p.interop.ilp.ledger.domain.Transfer;

import java.util.Date;

public class QueuedTransfer {
    final private TransferState transferState;
    final private Transfer transferDetails;
    private final ObjectMapper mapper;

    public QueuedTransfer(TransferState transferState, Transfer transferDetails) {
        this.transferState = transferState;
        this.transferDetails = transferDetails;
        mapper = new ObjectMapper();

    }

    public TransferState getTransferState() {
        return transferState;
    }


    public Transfer getTransferDetails() {
        return transferDetails;
    }

    public String getTransferDetailsAsJson() throws JsonProcessingException {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(transferDetails);
    }

    public Date getTimestamp() {
        return transferDetails.getExpiresAt();
    }

    @Override
    public String toString() {
        return "QueuedTransfer{" +
                "transferState=" + transferState +
                ", timestamp=" + getTimestamp() +
                '}';
    }
}
