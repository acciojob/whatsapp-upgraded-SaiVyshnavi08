package com.driver;

import org.springframework.stereotype.Repository;
import java.util.*;
@Repository

public class WhatsappRepository {

    private HashMap<Group, List<User>> groupUsersMap = new HashMap<>();
    private HashMap<Group, List<Message>> groupMessagesMap = new HashMap<>();
    private HashMap<Message, User> senderMap= new HashMap<>();
    private HashMap<Group, User> adminMap= new HashMap<>();
    private HashMap<String, User> userData= new HashMap<>();
    private HashMap<User, List<Message>> userMessagesList = new HashMap<>();
    private HashMap<Integer, Message> messageDB = new LinkedHashMap<>();


    private int GroupCount= 0;
    private int messageId=0;


    public boolean UniqueNumber(String mobile){
        if(userData.containsKey(mobile)) return false;
        return true;
    }

    public void createUser(String name , String mobile){
       userData.put(mobile , new User(name , mobile));
    }

    public Group createGroup(List<User> users){

        //personal chat
        if(users.size()==2){
            Group personalChat = new Group(users.get(1).getName() , 2);
            groupUsersMap.put(personalChat, users);
        }
        GroupCount++;
        Group NewGroup = new Group("Group"+GroupCount , users.size());
        groupUsersMap.put(NewGroup,users); // added newgroup to groupMap
        adminMap.put(NewGroup,users.get(0)); // the first person added to the group is admin
      return NewGroup;
    }

    public int messageId(String content){
     messageId++; // incremented from 0 to 1 for the first time
     Message NewMessage  = new Message(messageId,content);
     return messageId;    // the msg number is returned
    }
    public String changeAdmin(User approver, User user, Group group) throws Exception {
       if(!groupUsersMap.containsKey(group)) throw new Exception("Group does not exist");
       if(!adminMap.get(group).equals(approver)) throw new Exception("Approver does not have rights");
       if(!groupUsersMap.get(group).contains(user)) throw new Exception("User is not a participant");
        adminMap.remove(group);//Removing older admin group pair
        adminMap.put(group,user);//Adding new admin to that group
        return "SUCCESS";
    }
    public int sendMessage(Message message, User sender, Group group) throws Exception{
        if(!groupUsersMap.containsKey(group)) throw new Exception("Group does not exist");
        if(!groupUsersMap.get(group).contains(sender)) throw new Exception("You are not allowed to send message");

        List<Message> messages = new ArrayList<>();
        if(groupMessagesMap.containsKey(group)) messages = groupMessagesMap.get(group);

        messages.add(message);
        groupMessagesMap.put(group , messages);
        return messages.size();
    }
    public int removeUser(User user) throws Exception{
        Group group =  null;
        for(Group group1 : groupUsersMap.keySet()) {
            for (User user1 : groupUsersMap.get(group1)) {
                if(user==user1) {
                    if(groupUsersMap.get(group1).get(0)==user) {
                        throw new Exception("Cannot remove admin");
                    }
                    group = group1;
                    break;
                }
            }
        }
        if(group==null) {
            throw new Exception("User not found");
        }
        for(Message message : userMessagesList.get(user)) {
            messageDB.remove(message.getId());
            groupMessagesMap.get(group).remove(message);
        }
        userMessagesList.remove(user);
        groupUsersMap.get(group).remove(user);
        userData.remove(user.getMobile());
        return groupUsersMap.get(group).size()+groupMessagesMap.get(group).size()+messageDB.size();
    }
    }


