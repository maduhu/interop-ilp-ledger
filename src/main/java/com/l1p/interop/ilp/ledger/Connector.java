package com.l1p.interop.ilp.ledger;

/**
 * Represents connector
 */
class Connector {
    private String id;
    private String name;
    private String connector;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConnector() {
        return connector;
    }

    public void setConnector(String connector) {
        this.connector = connector;
    }
}
