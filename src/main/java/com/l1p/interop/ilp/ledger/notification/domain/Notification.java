package com.l1p.interop.ilp.ledger.notification.domain;

public class Notification {
  final private String jsonRpc = "2.0";
  final private String method = "notify";
  final private String id = null; // must be null based on spec
  private NotificationParams params;

  public String getJsonRpc() {
    return jsonRpc;
  }

  public String getId() {
    return id;
  }

  public String getMethod() {
    return method;
  }

  public NotificationParams getParams() {
    return params;
  }

  public void setParams(NotificationParams params) {
    this.params = params;
  }
}
