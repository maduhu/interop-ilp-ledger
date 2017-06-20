package com.l1p.interop.ilp.ledger.models;

public class TransferResponse extends TransferRequest {
	
	private TransferStateEnum transferState;

	/**
	 * @return the transferState
	 */
	public TransferStateEnum getTransferState() {
		return transferState;
	}

	/**
	 * @param transferState the transferState to set
	 */
	public void setTransferState(TransferStateEnum transferState) {
		this.transferState = transferState;
	}
	
	

}
