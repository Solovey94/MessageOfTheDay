package com.solovey.messageoftheday.service;

import com.solovey.messageoftheday.dto.MessageDto;
import com.solovey.messageoftheday.model.Message;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


public interface MessageService {

    Message addOrUpdateMessage(MessageDto messageDto);

    MessageDto add(MessageDto messageDto);

    Message getMessageById(Long id);

    MessageDto findMessageById(Long id);

    List<MessageDto> findAllMessages();

    MessageDto showMessageByCreationDate() throws JobExecutionException;

    void deleteMessageById(Long id);

    MessageDto convertToDto(Message message);

    List<MessageDto> convertToDto(Iterable<Message> messages);

}
