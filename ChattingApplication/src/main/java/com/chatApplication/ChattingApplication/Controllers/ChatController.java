package com.chatApplication.ChattingApplication.Controllers;


import com.chatApplication.ChattingApplication.Payload.MessageRequest;
import com.chatApplication.ChattingApplication.Repository.RoomRepository;
import com.chatApplication.ChattingApplication.entities.Message;
import com.chatApplication.ChattingApplication.entities.Room;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Controller
@CrossOrigin("https://groupchatapp1.netlify.app/")
public class ChatController {

    private RoomRepository roomRepository;

    public ChatController(RoomRepository roomRepository){
        this.roomRepository=roomRepository;
    }

    //for sending and receiving messages
    @MessageMapping("/sendMessage/{roomId}")// /app/sendMessage/roomId
    @SendTo("/topic/room/{roomId}")// subscribe
    public Message sendMessage(@RequestBody MessageRequest request,
    @DestinationVariable String roomId){

        Room room=roomRepository.findByRoomId(request.getRoomId());

        Message message=new Message();
        message.setContent(request.getContent());
        message.setSender(request.getSender());
        message.setTimeStamp(LocalDateTime.now());

        if(room!=null){
            room.getMessages().add(message);
            roomRepository.save(room);
        }else {
            throw new RuntimeException("room not found!");
        }

        return  message;
    }

}

