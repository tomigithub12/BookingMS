package ac.at.fhcampuswien.bookingms.config;

import org.springframework.amqp.core.*;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {


    public static final String CARS_EXCHANGE = "cars_exchange";
    public static final String EXCHANGERATE_MESSAGE_QUEUE = "exchangeRate_msg_queue";
    public static final String EXCHANGERATE_REPLY_MESSAGE_QUEUE = "exchangeRate_reply_msg_queue";
    public static final String BOOKED_CARS_MESSAGE_QUEUE = "bookedCars_msg_queue";


    @Bean
    Queue msgQueue1() {

        return new Queue(EXCHANGERATE_MESSAGE_QUEUE);
    }

    @Bean
    Queue replyQueue1() {

        return new Queue(EXCHANGERATE_REPLY_MESSAGE_QUEUE);
    }

    @Bean
    Queue msgQueue2(){
        return new Queue(BOOKED_CARS_MESSAGE_QUEUE);
    }

    @Bean
    TopicExchange exchange() {

        return new TopicExchange(CARS_EXCHANGE);
    }

    @Bean
    Binding msgBinding1() {

        return BindingBuilder.bind(msgQueue1()).to(exchange()).with(EXCHANGERATE_MESSAGE_QUEUE);
    }

    @Bean
    Binding msgBinding2() {

        return BindingBuilder.bind(msgQueue2()).to(exchange()).with(BOOKED_CARS_MESSAGE_QUEUE);
    }

    @Bean
    Binding replyBinding1() {

        return BindingBuilder.bind(replyQueue1()).to(exchange()).with(EXCHANGERATE_REPLY_MESSAGE_QUEUE);
    }

    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

}
