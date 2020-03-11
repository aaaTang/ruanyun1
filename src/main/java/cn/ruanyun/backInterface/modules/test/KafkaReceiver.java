package cn.ruanyun.backInterface.modules.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class KafkaReceiver {


    @KafkaListener(topics = {"ruanyun"})
    public void listen(ConsumerRecord<?, ?> record) {

        Optional.ofNullable(record.value())
                .ifPresent(msg -> {

                    log.info("----------------- record = {}", record);
                    log.info("----------------- message = {}", msg);
                });
    }


}
