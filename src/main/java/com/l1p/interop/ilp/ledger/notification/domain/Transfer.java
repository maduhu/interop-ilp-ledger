package com.l1p.interop.ilp.ledger.notification.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Transfer {
  @JsonProperty(value = "additional_info")
  private Object additionalInfo;

  @JsonProperty(value = "cancellation_condition")
  private String cancellationCondition;
  private List<Credit> credits = Collections.emptyList();
  private List<Debit> debits = Collections.emptyList();

  @JsonProperty(value = "execution_condition")
  private String executionCondition;

  @JsonProperty(value = "expires_at")
  private Date expiresAt;
  private String id;
  private String ledger;

  @JsonProperty(value = "rejection_reason")
  private String rejectionReason;

  private String state;
  private Timeline timeline;

  public Transfer() {
  }

  public Object getAdditionalInfo() {
    return additionalInfo;
  }

  public void setAdditionalInfo(Object additionalInfo) {
    this.additionalInfo = additionalInfo;
  }

  public String getCancellationCondition() {
    return cancellationCondition;
  }

  public void setCancellationCondition(String cancellationCondition) {
    this.cancellationCondition = cancellationCondition;
  }

  public List<Credit> getCredits() {
    return credits;
  }

  public void setCredits(List<Credit> credits) {
    this.credits = credits;
  }

  public List<Debit> getDebits() {
    return debits;
  }

  public void setDebits(List<Debit> debits) {
    this.debits = debits;
  }

  public String getExecutionCondition() {
    return executionCondition;
  }

  public void setExecutionCondition(String executionCondition) {
    this.executionCondition = executionCondition;
  }

  public Date getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(Date expiresAt) {
    this.expiresAt = expiresAt;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLedger() {
    return ledger;
  }

  public void setLedger(String ledger) {
    this.ledger = ledger;
  }

  public String getRejectionReason() {
    return rejectionReason;
  }

  public void setRejectionReason(String rejectionReason) {
    this.rejectionReason = rejectionReason;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public Timeline getTimeline() {
    return timeline;
  }

  public void setTimeline(Timeline timeline) {
    this.timeline = timeline;
  }
}

