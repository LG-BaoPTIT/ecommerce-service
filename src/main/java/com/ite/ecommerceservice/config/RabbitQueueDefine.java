package com.ite.ecommerceservice.config;


import com.ite.ecommerceservice.constants.JobQueue;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitQueueDefine {

    @Autowired
    @Qualifier("amqpAdmin")
    private AmqpAdmin rabbitAdminMain;

//    @Bean
//    public Queue emailApproveAccountQueue() {
//        return new Queue(JobQueue.APPROVED_ACCOUNT_NOTICE_QUEUE, false);
//    }
//
//    @Bean
//    public Queue createAdminAccountQueue(){ return new Queue(JobQueue.CREATE_ADMIN_ACCOUNT_QUEUE,false);}
//
//    @Bean
//    public Queue updateAdminAccountQueue(){return new Queue(JobQueue.UPDATE_ADMIN_ACCOUNT_QUEUE,false);}
//
//    @Bean
//    public Queue resetPasswordQueue(){return  new Queue(JobQueue.RESET_PASSWORD_NOTICE_QUEUE,false);}
////    ===========================================================================================================================
//    @Bean
//    public DirectExchange emailExchange(){
//        return new DirectExchange("email-exchange");
//    }
//
//    @Bean
//    public DirectExchange adminAccountExchange(){ return  new DirectExchange("adminAccount-exchange");}
//
////    ===========================================================================================================================
//
//    @Bean
//    public Binding bindingEmailExchangeToQueue(Queue emailApproveAccountQueue, DirectExchange emailExchange) {
//        return BindingBuilder.bind(emailApproveAccountQueue).to(emailExchange).with("approveAccountNotice.routing.key");
//    }
//
//    @Bean
//    public Binding bindingCreateAdminAccountQueueToAdminAccountExchange(Queue createAdminAccountQueue, DirectExchange adminAccountExchange){
//        return BindingBuilder.bind(createAdminAccountQueue).to(adminAccountExchange).with("createAdminAccount.routing.key");
//    }
//
//    @Bean
//    public Binding bindingUpdateAdminAccountQueueToAdminAccountExchange(Queue updateAdminAccountQueue, DirectExchange adminAccountExchange){
//        return BindingBuilder.bind(updateAdminAccountQueue).to(adminAccountExchange).with("updateAdminAccount.routing.key");
//    }
//
//    @Bean
//    public Binding bindingResetPasswordQueueToEmailExchange(Queue resetPasswordQueue, DirectExchange emailExchange){
//        return BindingBuilder.bind(resetPasswordQueue).to(emailExchange).with("resetPassword.routing.key");
//    }

}
