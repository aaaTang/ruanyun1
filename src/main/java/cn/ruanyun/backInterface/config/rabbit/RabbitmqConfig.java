/*
package cn.ruanyun.backInterface.config.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@Slf4j
public class RabbitmqConfig {


    @Autowired
    private Environment environment;

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    @Autowired
    private SimpleRabbitListenerContainerFactoryConfigurer simpleRabbitListenerContainerFactoryConfigurer;


    */
/**
     * 单一消费者
     * @return
     *//*

    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainer(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cachingConnectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);
        factory.setPrefetchCount(1);
        factory.setTxSize(1);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;
    }


    */
/**
     * 多个消费者
     * @return
     *//*

    @Bean(name = "multiListenerContainer")
    public SimpleRabbitListenerContainerFactory multiListenerContainer(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        simpleRabbitListenerContainerFactoryConfigurer.configure(factory,cachingConnectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.NONE);
        factory.setConcurrentConsumers(environment.getProperty("spring.rabbitmq.listener.concurrency",int.class));
        factory.setMaxConcurrentConsumers(environment.getProperty("spring.rabbitmq.listener.max-concurrency",int.class));
        factory.setPrefetchCount(environment.getProperty("spring.rabbitmq.listener.prefetch",int.class));
        return factory;
    }


    @Bean
    public RabbitTemplate rabbitTemplate() {

        cachingConnectionFactory.setPublisherConfirms(true);
        cachingConnectionFactory.setPublisherReturns(true);

        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setConfirmCallback((correlationData, b, s) -> log.info("消息发送成功:correlationData({}),ack({}),cause({})",correlationData,b,s));

       rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) ->
               log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}",exchange,routingKey,replyCode,replyText,message));
       return rabbitTemplate;
   }

}
*/
