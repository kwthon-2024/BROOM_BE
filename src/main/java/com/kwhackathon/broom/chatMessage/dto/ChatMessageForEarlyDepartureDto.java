package com.kwhackathon.broom.chatMessage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kwhackathon.broom.chatMessage.entity.ChatMessageForEarlyDeparture;
import com.kwhackathon.broom.chatRoom.entity.ChatRoomForEarlyDeparture;
import com.kwhackathon.broom.user.entity.User;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

public class ChatMessageForEarlyDepartureDto {
    @Data
    public static class Request{
        private String chatRoomId;  // 메시지를 보내는 채팅방 ID
        private String content;     // 메시지 내용
        private String senderId;    // 발신자 ID

        public ChatMessageForEarlyDeparture toEntity(ChatRoomForEarlyDeparture chatRoom, UserDetails sender) {
            ChatMessageForEarlyDeparture message = new ChatMessageForEarlyDeparture();
            message.setChatRoom(chatRoom);
            message.setSender((User) sender);
            message.setMessage(content);
            message.setCreatedAt(LocalDateTime.now());
            return message;
        }
    }

    @Data
    public static class Response{
        private Long id;         // 메시지 고유 ID
        private String chatRoomId;      // 채팅방 ID
        private String senderId;        // 발신자 ID
        private String senderNickname;  // 발신자 닉네임
        private String content;         // 메시지 내용

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalDateTime createdAt; // 메시지 생성 시간

        public static ChatMessageForEarlyDepartureDto.Response fromEntity(ChatMessageForEarlyDeparture message) {
            ChatMessageForEarlyDepartureDto.Response response = new ChatMessageForEarlyDepartureDto.Response();
            response.id = message.getId();
            response.chatRoomId = message.getChatRoom().getId();
            response.senderId = message.getSender().getUserId();
            response.senderNickname = message.getSender().getNickname();
            response.content = message.getMessage();
            response.createdAt = message.getCreatedAt();
            return response;
        }

    }
}
