package com.l1p.interop.ilp.ledger.notification.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

abstract public class NotificationParams {
  private String id;
  private Object resource;
  private String event;

  public NotificationParams(String event, Object resource) {
    this.resource = resource;
    this.event = event;
  }

  @JsonProperty(value = "related_resources")
  private Map<String, String> relatedResources;

  public String getEvent() {
    return event;
  };

  public Object getResource() {
    return resource;
  }

  public void setResource(Object resource) {
    this.resource = resource;
  }
}
