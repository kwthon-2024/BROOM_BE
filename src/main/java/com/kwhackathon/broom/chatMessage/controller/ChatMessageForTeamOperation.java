package com.kwhackathon.broom.chatMessage.controller;

import com.kwhackathon.broom.chatMessage.dto.ChatMessageForTeamDto;
import com.kwhackathon.broom.chatMessage.dto.ReadStatusUpdateDto;
import com.kwhackathon.broom.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ChatMessageForTeamOperation {
    // 채팅방 입장
    @MessageMapping("chat.team.enter")
    void enterUser(@Payload ChatMessageForTeamDto.Request messageDto, @AuthenticationPrincipal User user);

    // 메시지 보내기
    @MessageMapping("chat.team.message")
    public void sendMessage(@Payload ChatMessageForTeamDto.Request messageDto, SimpMessageHeaderAccessor headerAccessor) ;

    // REST API로 채팅방에 메시지 전송
//    @PostMapping("/earlyDeparture/chat/message/{chatRoomId}")
//    ResponseEntity<String> sendMessageViaApi(
//            @PathVariable String chatRoomId,
//            @RequestBody ChatMessageForEarlyDepartureDto.Request messageDto);

    @MessageMapping("chat.team.read.{roomId}")
    void updateReadStatus(@Payload ReadStatusUpdateDto.Request readStatusUpdate, @DestinationVariable String roomId, @AuthenticationPrincipal String userId);

    @GetMapping("/team/chat/list/{roomId}")
    ResponseEntity<?> listChatMessages(
            @PathVariable String roomId,
            @AuthenticationPrincipal User user
    );
}
