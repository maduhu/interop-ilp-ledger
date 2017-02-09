package com.l1p.interop.ilp.ledger;

import java.util.List;

/**
 * Represents the metadata for actual ledger
 *
 */
public class LedgerAdapterMetadata {
    private String currencyCode;
    private String currencySymbol;
    private List<Connector> connectors;
    private ServiceUrlRegistry urls;
    private int precision;
    private int scale;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public List<Connector> getConnectors() {
        return connectors;
    }

    public void setConnectors(List<Connector> connectors) {
        this.connectors = connectors;
    }

    public ServiceUrlRegistry getUrls() {
        return urls;
    }

    public void setUrls(ServiceUrlRegistry urls) {
        this.urls = urls;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}

