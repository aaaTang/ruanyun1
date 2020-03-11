/*
package cn.exrick.xboot.modules.weChat.service.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Objects;

*/
/**
 * @program: xboot-plus
 * @description:
 * @author: fei
 * @create: 2020-02-09 16:02
 **//*

@Configuration
public class WeixinMq {

    @Autowired
    private Environment environment;


    */
/**
     * 生成微信的队列
     * @return
     *//*

    @Bean
    public Queue createQueue() {
        return new Queue(Objects.requireNonNull(environment.getProperty("param.weixin.queue")),true);
    }


    */
/**
     * 生成微信的交换机
     * @return
     *//*

    @Bean
    public DirectExchange createExchange() {
        return new DirectExchange(environment.getProperty("param.weixin.exchange"),true,false);
    }

    */
/**
     * 生成微信的绑定key
     * @return
     *//*

    @Bean
    public Binding createBindingKey() {
        return BindingBuilder.bind(createQueue()).to(createExchange()).with(environment.getProperty("param.weixin.key"));
    }


}
*/
