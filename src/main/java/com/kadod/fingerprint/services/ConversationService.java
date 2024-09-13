package com.kadod.fingerprint.services;

import com.google.gson.Gson;
import com.kadod.commons.enums.Constant;
import com.kadod.commons.ws.*;
import com.kadod.database.model.entities.Conversation;
import com.kadod.database.model.entities.Employee;
import com.kadod.database.model.entities.RowConversation;
import com.kadod.database.model.entities.User;
import com.kadod.database.model.repositories.*;
import com.kadod.fingerprint.util.JwtUtil;
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
    RowConversationRepository rowConversationRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ConnexionRepository connexionRepository;
    @Autowired
    FileService fileService;

    public ReponseWs sender(ConversationRequestWs ws){
        String email = JwtUtil.extractEmail(ws.getToken());
        User user = userRepository.findOneByEmail(email);
        if(user == null) return new ReponseWs(Constant.FAILED, "user not found", 404, null);
        Employee sender = employeeRepository.findByUser(user);
        Employee receiver = new Employee();
        if (ws.getReceiverId() != null)
            receiver = employeeRepository.findOneById(ws.getReceiverId());
        if(sender == null || receiver == null) return new ReponseWs(Constant.FAILED, "sender or receiver not found", 404, null);
        RowConversation rowConversation = this.rowConversationRepository.findByCreateByBetweenAndCreateToBetween(sender, receiver, receiver, sender);
        if(rowConversation == null){
            rowConversation = new RowConversation();
            rowConversation.setCreateBy(sender);
            rowConversation.setCreateTo(receiver);
            rowConversation.setCreated(new Date());
        }
        Conversation conversation = new Conversation();
        conversation.setContenu(ws.getContenu());
        conversation.setCreated(new Date());
        conversation.setSender(sender);
        conversation.setReceiver(receiver);
        conversation.setRow(rowConversation);
        conversationRepository.save(conversation);
        return new ReponseWs(Constant.SUCCESS, "message enregister", 200, ws);
    }

    public ReponseWs senderWithAdmin(ConversationRequestWs ws){
        Gson gson = new Gson();
        String email = JwtUtil.extractEmail(ws.getToken());
        User user = userRepository.findOneByEmail(email);
        if(user == null) return new ReponseWs(Constant.FAILED, "user not found", 404, null);
        Employee sender = employeeRepository.findByUser(user);
        if(sender == null) return new ReponseWs(Constant.FAILED, "sender not found", 404, null);
        Employee receiver = sender.getEmployeeAdmin();
        if(sender.getAdmin())
            receiver = sender;
        ws.setReceiverId(receiver.getId());
        System.out.println(ws);
        this.sender(ws);

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
        RowConversation rowConversation = this.rowConversationRepository.findByCreateByBetweenAndCreateToBetween(employeeA, employeeT, employeeT, employeeA);
        Page<Conversation> conversations = conversationRepository.findByRowOrderByCreatedDesc(rowConversation, pageable);
        List<ConversationWs> conversationWsList = conversations.getContent().stream().map(this::getConversationWs).collect(Collectors.toList());
        return new PageImpl<>(conversationWsList, pageable, conversations.getTotalPages());
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
        RowConversation row = this.rowConversationRepository.findByCreateByBetweenAndCreateToBetween(employeeAdmin, employee, employee, employeeAdmin);
        List<Conversation> conversations = conversationRepository.findByRowOrderByCreatedDesc(row);
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
        conversationWs.setReceiver(gson.fromJson(gson.toJson(conversation.getReceiver()), EmployeeWs.class));
        conversationWs.setSender(gson.fromJson(gson.toJson(conversation.getSender()), EmployeeWs.class));
        conversationWs.setDateTimestamp(conversation.getCreated().getTime());
        return conversationWs;
    }

}
