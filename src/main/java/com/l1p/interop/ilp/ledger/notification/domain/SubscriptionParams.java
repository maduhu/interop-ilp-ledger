package com.l1p.interop.ilp.ledger.notification.domain;

import java.util.List;

public class SubscriptionParams {
  private List<String> accounts;

  public List<String> getAccounts() {
    return accounts;
  }

  public void setAccounts(List<String> accounts) {
    this.accounts = accounts;
  }
}
