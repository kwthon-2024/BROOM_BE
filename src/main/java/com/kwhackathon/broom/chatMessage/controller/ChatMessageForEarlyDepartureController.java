package com.kwhackathon.broom.chatMessage.controller;

import com.kwhackathon.broom.chatMessage.dto.ChatMessageForEarlyDepartureDto;
import com.kwhackathon.broom.chatMessage.dto.ReadStatusUpdateDto;
import com.kwhackathon.broom.chatMessage.service.ChatMessageForCarpoolService;
import com.kwhackathon.broom.chatMessage.service.ChatMessageForEarlyDepartureService;
import com.kwhackathon.broom.chatRoom.service.ChatRoomForCarpoolService;
import com.kwhackathon.broom.chatRoom.service.ChatRoomForEarlyDepartureBoardService;
import com.kwhackathon.broom.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ChatMessageForEarlyDepartureController implements ChatMessageForEarlyDepartureOperation{
    private final ChatMessageForEarlyDepartureService chatMessageService;
    private final ChatRoomForEarlyDepartureBoardService chatRoomForEarlyDepartureBoardService;

    private static final String CHAT_EXCHANGE_NAME = "chat.earlyDeparture.exchange";
    private static final String CHAT_QUEUE_NAME = "chat.earlyDeparture.queue";
    @Override
    public void enterUser(ChatMessageForEarlyDepartureDto.Request messageDto, User user) {
        // user id, sender id 확인
        if(!messageDto.getSenderId().equals(user.getUserId())){
            throw new IllegalArgumentException("전송 유저와 로그인 유저가 일치하지 않습니다.");
        }
        messageDto.setContent(messageDto.getSenderId() + "님 입장!");
        // 구독 채널에 전송
        chatMessageService.sendMessage(CHAT_EXCHANGE_NAME, "chat.carpool.room." + messageDto.getChatRoomId(), messageDto);

    }

    @Override
    public void sendMessage(ChatMessageForEarlyDepartureDto.Request messageDto, SimpMessageHeaderAccessor headerAccessor) {
        Principal principal = (Principal) headerAccessor.getSessionAttributes().get("principal");

        if (principal == null) {
            throw new IllegalArgumentException("Principal is null. User is not authenticated.");
        }
        System.out.println(messageDto.getSenderId());
        System.out.println(principal.getName());
//        if(!messageDto.getSenderId().equals(chatRoomForCarpoolService.findByChatRoomId(messageDto.getChatRoomId()).getAuthor())
//        && !messageDto.getSenderId().equals(chatRoomForCarpoolService.findByChatRoomId(messageDto.getChatRoomId()).getParticipant())){
//                throw new IllegalArgumentException("전송 유저와 로그인 유저가 일치하지 않습니다.");
//        }
        // user id, sender id 확인
        if(!messageDto.getSenderId().equals(principal.getName())){
            throw new IllegalArgumentException("전송 유저와 로그인 유저가 일치하지 않습니다.");
        }

        // 전송 전 로그 확인
        System.out.println("sendMessage 메서드 호출됨");
        System.out.println("WebSocket 메시지 수신: " + messageDto.getChatRoomId());

        // 구독 채널에 전송
        chatMessageService.sendMessage(CHAT_EXCHANGE_NAME, "chat.carpool.room." + messageDto.getChatRoomId(), messageDto);


    }

//    @Override
//    public ResponseEntity<String> sendMessageViaApi(String chatRoomId, ChatMessageForEarlyDepartureDto.Request messageDto) {
//        return null;
//    }

    @Override
    public void updateReadStatus(ReadStatusUpdateDto.Request readStatusUpdate, String chatRoomId, String userId) {
//TODO: 읽으면 되긴하는데 다시 안 읽으면 false로 교체하는 로직 필요
        chatMessageService.updateReadStatus(chatRoomId, readStatusUpdate.getUserId());
    }
}
