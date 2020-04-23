package com.solovey.messageoftheday.controller;

import com.solovey.messageoftheday.dto.MessageDto;
import com.solovey.messageoftheday.service.MessageService;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("messages")
public class MessageController {
    MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public MessageDto createMessage(@RequestBody MessageDto messageDto) {
        return messageService.add(messageDto);
    }

    @GetMapping("/all")
    public List<MessageDto> findAllMessages() {
        return messageService.findAllMessages();
    }

    @GetMapping
    public MessageDto showMessageOfTheDay() throws JobExecutionException {
        return messageService.showMessageByCreationDate();
    }

    @GetMapping("/{id}")
    public MessageDto findById(@PathVariable Long id) {
        return messageService.findMessageById(id);
    }

    @PutMapping("/{id}")
    public MessageDto update(
            @PathVariable Long id,
            @Validated @RequestBody MessageDto messageDto
    ) {
        messageDto.setId(id);
        return messageService.add(messageDto);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Long id) {
        messageService.deleteMessageById(id);
    }

}
