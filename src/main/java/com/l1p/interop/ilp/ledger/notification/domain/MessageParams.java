package com.l1p.interop.ilp.ledger.notification.domain;

public class MessageParams extends NotificationParams {

  public MessageParams(String event, Object resource) {
    super("message.send", resource);
  }

}
