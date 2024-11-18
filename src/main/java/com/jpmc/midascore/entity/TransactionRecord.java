package com.jpmc.midascore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class TransactionRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  @JoinColumn(name = "sender_id", nullable = false)
  private UserRecord sender;

  @ManyToOne
  @JoinColumn(name = "recipient_id", nullable = false)
  private UserRecord recipient;

  private float amount;

  private float incentive;

  public TransactionRecord(){}

  public TransactionRecord(UserRecord sender, UserRecord recipient, float amount, float incentive) {
    this.sender = sender;
    this.recipient = recipient;
    this.amount = amount;
    this.incentive = incentive;
  }

  // Getters and setters
  public float getIncentive() {
    return incentive;
  }

  public void setIncentive(float incentive) {
    this.incentive = incentive;
  }
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UserRecord getSender() {
    return sender;
  }

  public void setSender(UserRecord sender) {
    this.sender = sender;
  }

  public UserRecord getRecipient() {
    return recipient;
  }

  public void setRecipient(UserRecord recipient) {
    this.recipient = recipient;
  }

  public float getAmount() {
    return amount;
  }

  public void setAmount(float amount) {
    this.amount = amount;
  }
}
