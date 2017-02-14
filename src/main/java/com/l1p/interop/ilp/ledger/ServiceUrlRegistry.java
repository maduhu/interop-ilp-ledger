package com.l1p.interop.ilp.ledger;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *  Relevant urls for ledger adapter metadata
 */
class ServiceUrlRegistry {
    private String health;
    private String transfer;
    private String transferState;
    
    private String transferRejection;
    private String transferFulfillment;
    private String account;
    private String accountTransfers;
    private String accounts;
    private String authToken;
    private String message;
    private String websocket;

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getTransfer() {
        return transfer;
    }

    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }

    @JsonProperty("transfer_state")
    public String getTransferState() {
        return transferState;
    }

    public void setTransferState(String transferState) {
        this.transferState = transferState;
    }

    @JsonProperty("transfer_rejection")
    public String getTransferRejection() {
        return transferRejection;
    }

    public void setTransferRejection(String transferRejection) {
        this.transferRejection = transferRejection;
    }

    @JsonProperty("transfer_fulfillment")
    public String getTransferFulfillment() {
        return transferFulfillment;
    }

    public void setTransferFulfillment(String transferFulfillment) {
        this.transferFulfillment = transferFulfillment;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountTransfers() {
        return accountTransfers;
    }

    @JsonProperty("account_transfers")
    public void setAccountTransfers(String accountTransfers) {
        this.accountTransfers = accountTransfers;
    }

    public String getAccounts() {
        return accounts;
    }

    public void setAccounts(String accounts) {
        this.accounts = accounts;
    }

    @JsonProperty("auth_token")
    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWebsocket() {
        return websocket;
    }

    public void setWebsocket(String websocket) {
        this.websocket = websocket;
    }
}
