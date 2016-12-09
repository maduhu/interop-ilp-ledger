package com.l1p.interop.ilp.ledger.notification.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Timeline {
  @JsonProperty(value = "prepared_at")
  private Date preparedAt;
  @JsonProperty(value = "executed_at")
  private Date executedAt;
  @JsonProperty(value = "rejected_at")
  private Date rejectedAt;

  public Timeline() {
  }

  public Date getPreparedAt() {
    return preparedAt;
  }

  public void setPreparedAt(Date preparedAt) {
    this.preparedAt = preparedAt;
  }

  public Date getExecutedAt() {
    return executedAt;
  }

  public void setExecutedAt(Date executedAt) {
    this.executedAt = executedAt;
  }

  public Date getRejectedAt() {
    return rejectedAt;
  }

  public void setRejectedAt(Date rejectedAt) {
    this.rejectedAt = rejectedAt;
  }
}
