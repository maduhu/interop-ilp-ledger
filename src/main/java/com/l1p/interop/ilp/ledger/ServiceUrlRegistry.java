package com.l1p.interop.ilp.ledger;

/**
 *  Relevant urls for ledger adapter metadata
 */
class ServiceUrlRegistry {
    private String health;
    private String transfer;
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

    public String getTransferRejection() {
        return transferRejection;
    }

    public void setTransferRejection(String transferRejection) {
        this.transferRejection = transferRejection;
    }

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

    public void setAccountTransfers(String accountTransfers) {
        this.accountTransfers = accountTransfers;
    }

    public String getAccounts() {
        return accounts;
    }

    public void setAccounts(String accounts) {
        this.accounts = accounts;
    }

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
