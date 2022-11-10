package com.example.springtask.Service;

import com.example.model.FileInformations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    @Autowired
    @Qualifier("kafkaTemplate")
    private KafkaTemplate<String, FileInformations> kafkaTemplate;

//    public void sendMessageToTopic(String key, FileInformations message, String topicName){
//        kafkaTemplate.send(topicName,key,message);
//    }
}
