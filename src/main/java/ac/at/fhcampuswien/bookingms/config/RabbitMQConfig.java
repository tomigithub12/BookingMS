package ac.at.fhcampuswien.bookingms.config;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.*;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableRabbit
public class RabbitMQConfig {


    public static final String CARS_EXCHANGE = "cars_exchange";
    public static final String CUSTOMER_EXCHANGE = "customer_exchange";
    public static final String EXCHANGERATE_MESSAGE_QUEUE = "exchangeRate_msg_queue";
    public static final String EXCHANGERATE_REPLY_MESSAGE_QUEUE = "exchangeRate_reply_msg_queue";
    public static final String BOOKED_CARS_MESSAGE_QUEUE = "bookedCars_msg_queue";
    public static final String CUSTOMERID_MESSAGE_QUEUE = "customerID_msg_queue";

    public static final String GET_CARS_MESSAGE_QUEUE = "getCars_msg_queue";

    public static final String CUSTOM_EXCHANGERATE_MESSAGE_QUEUE = "customExchangeRate_msg_queue";

    public static final String GET_FREE_CARS_MESSAGE_QUEUE = "getFreeCars_msg_queue";


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
    Queue msgQueue3(){
        return new Queue(CUSTOMERID_MESSAGE_QUEUE);
    }

    @Bean
    Queue msgQueue4(){
        return new Queue(GET_CARS_MESSAGE_QUEUE);
    }

    @Bean
    Queue msgQueue5(){
        return new Queue(CUSTOM_EXCHANGERATE_MESSAGE_QUEUE);
    }

    @Bean
    Queue msgQueue6() {

        return new Queue(GET_FREE_CARS_MESSAGE_QUEUE);
    }

    @Bean
    TopicExchange exchange() {

        return new TopicExchange(CARS_EXCHANGE);
    }

    @Bean
    TopicExchange exchange2() {

        return new TopicExchange(CUSTOMER_EXCHANGE);
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
    Binding msgBinding3() {

        return BindingBuilder.bind(msgQueue3()).to(exchange2()).with(CUSTOMERID_MESSAGE_QUEUE);
    }

    @Bean
    Binding msgBinding4() {

        return BindingBuilder.bind(msgQueue4()).to(exchange()).with(GET_CARS_MESSAGE_QUEUE);
    }

    @Bean
    Binding msgBinding5() {

        return BindingBuilder.bind(msgQueue5()).to(exchange()).with(CUSTOM_EXCHANGERATE_MESSAGE_QUEUE);
    }

    @Bean
    Binding msgBinding6() {

        return BindingBuilder.bind(msgQueue6()).to(exchange()).with(GET_FREE_CARS_MESSAGE_QUEUE);
    }

    @Bean
    Binding replyBinding1() {

        return BindingBuilder.bind(replyQueue1()).to(exchange()).with(EXCHANGERATE_REPLY_MESSAGE_QUEUE);
    }

    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Set the desired number of threads
        executor.setMaxPoolSize(10);
        executor.setThreadNamePrefix("RabbitListener-");
        executor.initialize();
        return executor;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(10); // Set the desired number of concurrent consumers
        factory.setTaskExecutor(taskExecutor());
        return factory;
    }



}
