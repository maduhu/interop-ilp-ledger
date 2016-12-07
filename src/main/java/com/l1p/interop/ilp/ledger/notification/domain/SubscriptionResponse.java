package com.l1p.interop.ilp.ledger.notification.domain;

public class SubscriptionResponse {

  private String id;
  private String jsonrpc;
  private int result;

  public SubscriptionResponse(String id, String jsonrpc, int size) {
    this.id = id;
    this.jsonrpc = jsonrpc;
    this.result = size;
  }

  public SubscriptionResponse() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getJsonrpc() {
    return jsonrpc;
  }

  public void setJsonrpc(String jsonrpc) {
    this.jsonrpc = jsonrpc;
  }

  public int getResult() {
    return result;
  }

  public void setResult(int result) {
    this.result = result;
  }
}
