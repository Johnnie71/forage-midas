package com.jpmc.midascore.foundation;

import org.slf4j.Logger;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.TransactionRespository;
import com.jpmc.midascore.repository.UserRepository;

@Component
public class KafkaTransactionListener {
  static final Logger logger = LoggerFactory.getLogger(KafkaTransactionListener.class);

  @Autowired 
  private UserRepository userRespository;

  @Autowired
  private TransactionRespository transactionRepository;

  @Autowired
  private RestTemplate restTemplate;

  
  @KafkaListener(topics = "${general.kafka-topic}")
  public void listen(ConsumerRecord<String, Transaction> record) {
    Transaction transaction = record.value();

    // Validate transaction
    UserRecord sender = userRespository.findById(transaction.getSenderId());
    UserRecord recipient = userRespository.findById(transaction.getRecipientId());

    if ( sender == null || recipient == null) {
      logger.warn("Invalid transaction: sender or recipient does not exist.");
      return;
    }

    if (sender.getBalance() < transaction.getAmount()) {
      logger.warn("Invalid transaction: insufficeint balance for sender.");
      return;
    }

    IncentiveResponse incentiveResponse = restTemplate.postForObject("http://localhost:8080/incentive", transaction, IncentiveResponse.class);

    float incentive = incentiveResponse != null ? incentiveResponse.getAmount() : 0;
    logger.info("Incentive amount: " + incentive);


    // Adjust balances and save the transaction
    sender.setBalance(sender.getBalance() - transaction.getAmount());
    recipient.adjustBalance(transaction.getAmount() + incentive);
    userRespository.save(sender);
    userRespository.save(recipient);

    TransactionRecord transactionRecord = new TransactionRecord(sender, recipient, transaction.getAmount(), incentive);
    transactionRepository.save(transactionRecord);

    logger.info("Recieved transaction: " + transaction);

    if (transaction != null) {
      logger.info("Transaction amount:" + transaction.getAmount());
    } else {
      logger.warn("Transaction is null");
    }
  }
}
