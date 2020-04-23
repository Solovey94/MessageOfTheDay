package com.solovey.messageoftheday;

import com.solovey.messageoftheday.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class MessageOfTheDayApplication  implements CommandLineRunner {

    @Autowired
    CustomerService customerService;

    public static void main(String[] args) {
        SpringApplication.run(MessageOfTheDayApplication.class, args);
    }
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    public void run(String... params) throws Exception {
/*        Customer admin = new Customer();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_ADMIN)));

        customerService.signUp(admin);

        Customer client = new Customer();
        client.setUsername("client");
        client.setPassword("client");
        client.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_CLIENT)));

        customerService.signUp(client);*/
    }



}
