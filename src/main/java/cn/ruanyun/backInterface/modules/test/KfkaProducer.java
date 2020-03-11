package cn.ruanyun.backInterface.modules.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class KfkaProducer {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send() {

    }
}
