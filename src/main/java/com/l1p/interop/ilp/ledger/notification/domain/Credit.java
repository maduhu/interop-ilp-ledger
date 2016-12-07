package com.l1p.interop.ilp.ledger.notification.domain;

public class Credit {
  private String account;
  private String amount;
  private Object memo;

  public Credit(String account, String amount, Object memo) {
    this.account = account;
    this.amount = amount;
    this.memo = memo;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public Object getMemo() {
    return memo;
  }

  public void setMemo(Object memo) {
    this.memo = memo;
  }
}
