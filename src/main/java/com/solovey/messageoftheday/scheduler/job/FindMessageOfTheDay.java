package com.solovey.messageoftheday.scheduler.job;

import com.solovey.messageoftheday.dto.MessageDto;
import com.solovey.messageoftheday.model.Message;
import com.solovey.messageoftheday.repository.MessageRepository;
import com.solovey.messageoftheday.service.MessageService;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Component
public class FindMessageOfTheDay {

    private final MessageRepository messageRepository;

    @Autowired
    public FindMessageOfTheDay(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Scheduled(cron = "${quartz.config.string}")
    public Message messageSelect() throws JobExecutionException {
        LocalDate currentDate = LocalDate.now();
        try {
            List<Message> messages = messageRepository.findByCreationDate(currentDate);
            if (messages.size() > 0) {
                return messages.get(0);
            } else {
                Random rand = new Random();
                List<Message> allMessages = messageRepository.findAll();
                Message randomMessage = allMessages.get(rand.nextInt(allMessages.size()));
                return randomMessage;
            }
        } catch (Exception exception) {
            throw new JobExecutionException(exception);
        }
    }

}
