package com.solovey.messageoftheday.service.impl;

import com.solovey.messageoftheday.dto.MessageDto;
import com.solovey.messageoftheday.exception.NotFoundException;
import com.solovey.messageoftheday.model.Customer;
import com.solovey.messageoftheday.model.Message;
import com.solovey.messageoftheday.repository.MessageRepository;
import com.solovey.messageoftheday.scheduler.job.FindMessageOfTheDay;
import com.solovey.messageoftheday.service.CustomerService;
import com.solovey.messageoftheday.service.MessageService;
import org.quartz.JobExecutionException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final CustomerService customerService;
    private final FindMessageOfTheDay findMessageOfTheDay;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, CustomerService customerService, FindMessageOfTheDay findMessageOfTheDay) {
        this.messageRepository = messageRepository;
        this.customerService = customerService;
        this.findMessageOfTheDay = findMessageOfTheDay;
    }

    @Transactional
    @Override
    public Message addOrUpdateMessage(MessageDto messageDto) {
        Message message;
        if (messageDto.getId() != null) {
            message = getMessageById(messageDto.getId());
        } else {
            message = new Message();
            messageRepository.save(message);
        }
        Customer customer = customerService.getCustomerById(messageDto.getCustomer_id());
        BeanUtils.copyProperties(messageDto, message, "id");
        message.setCustomer(customer);
        List<Message> messagesByClient = messageRepository.findByCustomerId(customer.getId());
        return message;
    }

    @Transactional
    @Override
    public MessageDto add(MessageDto messageDto) {
        return convertToDto(messageRepository.saveAndFlush(addOrUpdateMessage(messageDto)));
    }

    @Transactional
    @Override
    public Message getMessageById(Long id) {
        Optional<Message> message = messageRepository.findById(id);
        if (message.isPresent()) {
            return message.get();
        }
        throw new NotFoundException("Not found element by id " + id.toString());
    }

    @Override
    public MessageDto findMessageById(Long id) {
        return convertToDto(getMessageById(id));
    }

    @Transactional
    @Override
    public List<MessageDto> findAllMessages() {
        List<Message> messages = messageRepository.findAll();
        if (messages.size() > 0) {
            return convertToDto(messages);
        }
        throw new NotFoundException("Not found any elements");
    }

    @Transactional
    @Override
    public MessageDto showMessageByCreationDate() throws JobExecutionException {
            Message message = findMessageOfTheDay.messageSelect();
            return convertToDto(message);
    }


    @Transactional
    @Override
    public void deleteMessageById(Long id) {
        messageRepository.deleteById(id);
    }

    @Override
    public MessageDto convertToDto(Message message) {
        MessageDto messageDto = new MessageDto();
        BeanUtils.copyProperties(message, messageDto);
        Customer customer = message.getCustomer();
        messageDto.setCustomer_id(customerService.convertToDto(customer).getId());
        return messageDto;
    }

    @Override
    public List<MessageDto> convertToDto(Iterable<Message> messages) {
        List<MessageDto> result = new ArrayList<>();
        for (Message message : messages) {
            result.add(convertToDto(message));
        }
        return result;
    }
}
