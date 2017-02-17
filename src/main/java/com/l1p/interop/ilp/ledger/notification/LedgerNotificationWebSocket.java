package com.l1p.interop.ilp.ledger.notification;

import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.websockets.DefaultWebSocket;
import org.glassfish.grizzly.websockets.ProtocolHandler;
import org.glassfish.grizzly.websockets.WebSocketListener;

import java.util.Collections;
import java.util.List;

public class LedgerNotificationWebSocket extends DefaultWebSocket {

  private List<String> accounts = Collections.emptyList();

  public LedgerNotificationWebSocket(ProtocolHandler protocolHandler, HttpRequestPacket request, WebSocketListener... listeners) {
    super(protocolHandler, request, listeners);
  }

  public List<String> getAccounts() {
    return accounts;
  }

  public void setAccounts(List<String> accounts) {
    this.accounts = Collections.unmodifiableList(accounts);
  }
}
