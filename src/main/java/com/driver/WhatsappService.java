package com.driver;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WhatsappService {
    @Autowired
    WhatsappRepository whatsappRepository= new WhatsappRepository();


    public boolean UniqueNumber(String mobile){
        return whatsappRepository.UniqueNumber(mobile);
    }
    public String createUser(String name , String mobile){
        whatsappRepository.createUser(name , mobile);
        return "SUCCESS";
    }
     public Group createGroup(List<User> users){
        return whatsappRepository.createGroup(users);
     }
    public int createMessage(String content){
   return whatsappRepository.messageId(content);
    }
    public String changeAdmin(User approver, User user, Group group) throws Exception {
    return whatsappRepository.changeAdmin(approver, user, group);
    }
    public int sendMessage(Message message, User sender, Group group) throws Exception{
   return whatsappRepository.sendMessage(message, sender, group);
    }
    public int removeUser(User user) throws Exception{
   return whatsappRepository.removeUser(user);
    }
}
