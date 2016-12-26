package io.github.bfvstats.controller;

import io.github.bfvstats.model.ChatMessage;
import io.github.bfvstats.service.ChatService;
import ro.pippo.controller.Controller;

import javax.inject.Inject;
import java.util.List;

public class ChatController extends Controller {

  private ChatService chatService;

  @Inject
  public ChatController(ChatService chatService) {
    this.chatService = chatService;
  }

  public void list() {
    List<ChatMessage> chatMessages = chatService.getChatMessages();
    getResponse().bind("chatMessages", chatMessages).render("chat/list");
  }
}
