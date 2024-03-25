package com.presence.testpresence.services;

import com.google.gson.Gson;
import com.presence.testpresence.model.entities.Conversation;
import com.presence.testpresence.model.entities.Employee;
import com.presence.testpresence.model.entities.User;
import com.presence.testpresence.model.enums.Constant;
import com.presence.testpresence.model.repositories.ConnexionRepository;
import com.presence.testpresence.model.repositories.ConversationRepository;
import com.presence.testpresence.model.repositories.EmployeeRepository;
import com.presence.testpresence.model.repositories.UserRepository;
import com.presence.testpresence.util.JwtUtil;
import com.presence.testpresence.ws.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConversationService {

    private static Logger logger = LogManager.getLogger(ConversationService.class);

    @Autowired
    ConversationRepository conversationRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ConnexionRepository connexionRepository;
    @Autowired
    FileService fileService;

    public ReponseWs sender(ConversationWs ws){
        String email = JwtUtil.extractEmail(ws.getToken());
        User user = userRepository.findOneByEmail(email);
        if(user == null) return new ReponseWs(Constant.FAILED, "user not found", 404, null);
        Employee sender = employeeRepository.findByUser(user);
        Employee receiver = employeeRepository.findOneById(ws.getReceiverId());
        if(sender == null || receiver == null) return new ReponseWs(Constant.FAILED, "sender or receiver not found", 404, null);
        Conversation conversation = new Conversation();
        conversation.setContenu(ws.getContenu());
        conversation.setCreated(new Date());
        conversation.setSender(sender);
        conversation.setReceiver(receiver);
        conversationRepository.save(conversation);
        return new ReponseWs(Constant.SUCCESS, "message enregister", 200, ws);
    }

    public ReponseWs senderWithAdmin(ConversationWs ws){
        String email = JwtUtil.extractEmail(ws.getToken());
        User user = userRepository.findOneByEmail(email);
        if(user == null) return new ReponseWs(Constant.FAILED, "user not found", 404, null);
        Employee sender = employeeRepository.findByUser(user);
        if(sender == null) return new ReponseWs(Constant.FAILED, "sender not found", 404, null);
        List<Employee> receivers = employeeRepository.findByCompanieAndIsAdmin(sender.getCompanie(), true);

        for (Employee admin: receivers){
            ws.setReceiverId(admin.getId());
            logger.debug(ws);
            this.sender(ws);
        }

        return new ReponseWs(Constant.SUCCESS, "message enregister", 200, ws);
    }


    public ReponseWs receive(String token, Integer employeeId, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Employee employeeA = employeeRepository.findOneById(employeeId);
        String email = JwtUtil.extractEmail(token);
        User user = userRepository.findOneByEmail(email);
        Employee employeeT = employeeRepository.findByUser(user);
        Page<ConversationWs> listConWs = this.getPageConversationWs(employeeA, employeeT, pageable);
        return new ReponseWs(Constant.SUCCESS, "list conversation", 200, listConWs);
    }
    private Page<ConversationWs> getPageConversationWs(Employee employeeA, Employee employeeT, Pageable pageable){
        Page<Conversation> conversations = conversationRepository.findBySenderBetweenAndReceiverBetweenOrderByCreatedDesc(employeeT, employeeA, employeeA, employeeT, pageable);
        List<ConversationWs> conversationWsList = conversations.getContent().stream().map(this::getConversationWs).collect(Collectors.toList());
        PageImpl<ConversationWs> listConWs = new PageImpl<>(conversationWsList, pageable, conversations.getTotalPages());
        return listConWs;
    }

    public ReponseWs listReceive(String token, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        String email = JwtUtil.extractEmail(token);
        User user = userRepository.findOneByEmail(email);
        if(user == null) return new ReponseWs(Constant.FAILED, "token invalide", 404, null);
        Employee employeeAdmin = employeeRepository.findByUser(user);
        if(employeeAdmin == null) return new ReponseWs(Constant.FAILED, "employer invalide", 404, null);
        Page<Employee> employees = employeeRepository.findByCompanie(employeeAdmin.getCompanie(), pageable);
        List<ListConversationWs> list = employees.stream().filter(d -> d.getUser() != employeeAdmin.getUser())
                .filter(v -> !conversationRepository.findBySenderBetweenAndReceiverBetweenOrderByCreatedDesc(employeeAdmin, v, v, employeeAdmin).isEmpty()).map(v -> this.getListConversationWs(employeeAdmin, v)).collect(Collectors.toList());
        return new ReponseWs(Constant.SUCCESS, "list des employees pour la conversation", 200, list);
    }

    private ListConversationWs getListConversationWs(Employee employeeAdmin, Employee employee){
        ListConversationWs listConversationWs = new ListConversationWs();
        listConversationWs.setEmployeeWs(this.getEmployeeWs(employee));
        List<Conversation> conversations = conversationRepository.findBySenderBetweenAndReceiverBetweenOrderByCreatedDesc(employeeAdmin, employee, employee, employeeAdmin);
        List<ConversationWs> conversationWsList = conversations.stream().map(this::getConversationWs).collect(Collectors.toList());
        listConversationWs.setConversation(conversationWsList);
        return listConversationWs;
    }

    private EmployeeWs getEmployeeWs(Employee employee){
        Gson gson = new Gson();
        EmployeeWs employeeWs = gson.fromJson(gson.toJson(employee), EmployeeWs.class);
        employeeWs.setCompany(employee.getCompanie().getNom());
        employeeWs.setIdCompany(employee.getCompanie().getId());
        employeeWs.setEnrollId(employee.getEnrollInfo().getEnrollId());
        employeeWs.setUser_id(employee.getUser().getId());
//        if(employee.getImageData() != null)
//            employeeWs.setImageProfile(this.fileService.downloadImage(employee.getImageData()));
        return employeeWs;
    }
    private ConversationWs getConversationWs(Conversation conversation){
        Gson gson = new Gson();
        ConversationWs conversationWs = gson.fromJson(gson.toJson(conversation), ConversationWs.class);
        conversationWs.setReceiverId(conversation.getReceiver().getId());
        conversationWs.setSenderId(conversation.getReceiver().getId());
        conversationWs.setDateTimestamp(conversation.getCreated().getTime());
        return conversationWs;
    }

}
