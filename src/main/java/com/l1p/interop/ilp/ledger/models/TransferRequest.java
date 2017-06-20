package com.l1p.interop.ilp.ledger.models;

import java.util.Arrays;
import java.util.Date;

public class TransferRequest {
	
	private String id;
	
	private String ledger;
	
	private String rejectionReason;
	
	private String executionCondition;
	
	private String cancellationCondition;
	
	private long expiresAt;
	
	private String state;
	
	private Funds[] credits;
	
	private Funds[] debits;
	
	private long timeline;
	
	private String additionalInfo;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the ledger
	 */
	public String getLedger() {
		return ledger;
	}

	/**
	 * @param ledger the ledger to set
	 */
	public void setLedger(String ledger) {
		this.ledger = ledger;
	}

	/**
	 * @return the rejectionReason
	 */
	public String getRejectionReason() {
		return rejectionReason;
	}

	/**
	 * @param rejectionReason the rejectionReason to set
	 */
	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	/**
	 * @return the executionCondition
	 */
	public String getExecutionCondition() {
		return executionCondition;
	}

	/**
	 * @param executionCondition the executionCondition to set
	 */
	public void setExecutionCondition(String executionCondition) {
		this.executionCondition = executionCondition;
	}

	/**
	 * @return the cancellationCondition
	 */
	public String getCancellationCondition() {
		return cancellationCondition;
	}

	/**
	 * @param cancellationCondition the cancellationCondition to set
	 */
	public void setCancellationCondition(String cancellationCondition) {
		this.cancellationCondition = cancellationCondition;
	}

	

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the credits
	 */
	public Funds[] getCredits() {
		return credits;
	}

	/**
	 * @param credits the credits to set
	 */
	public void setCredits(Funds[] credits) {
		this.credits = credits;
	}

	/**
	 * @return the debits
	 */
	public Funds[] getDebits() {
		return debits;
	}

	/**
	 * @param debits the debits to set
	 */
	public void setDebits(Funds[] debits) {
		this.debits = debits;
	}

	/**
	 * @return the additionalInfo
	 */
	public String getAdditionalInfo() {
		return additionalInfo;
	}

	/**
	 * @param additionalInfo the additionalInfo to set
	 */
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	/**
	 * @return the expiresAt
	 */
	public long getExpiresAt() {
		return expiresAt;
	}

	/**
	 * @param expiresAt the expiresAt to set
	 */
	public void setExpiresAt(long expiresAt) {
		this.expiresAt = expiresAt;
	}

	/**
	 * @return the timeline
	 */
	public long getTimeline() {
		return timeline;
	}

	/**
	 * @param timeline the timeline to set
	 */
	public void setTimeline(long timeline) {
		this.timeline = timeline;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TransferRequest [id=");
		builder.append(id);
		builder.append(", ledger=");
		builder.append(ledger);
		builder.append(", rejectionReason=");
		builder.append(rejectionReason);
		builder.append(", executionCondition=");
		builder.append(executionCondition);
		builder.append(", cancellationCondition=");
		builder.append(cancellationCondition);
		builder.append(", expiresAt=");
		builder.append(expiresAt);
		builder.append(", state=");
		builder.append(state);
		builder.append(", credits=");
		builder.append(Arrays.toString(credits));
		builder.append(", debits=");
		builder.append(Arrays.toString(debits));
		builder.append(", timeline=");
		builder.append(timeline);
		builder.append(", additionalInfo=");
		builder.append(additionalInfo);
		builder.append("]");
		return builder.toString();
	}

	
	
	

}
