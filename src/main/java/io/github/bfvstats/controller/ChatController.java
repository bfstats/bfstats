package io.github.bfvstats.controller;

import io.github.bfvstats.model.ChatMessage;
import io.github.bfvstats.service.ChatService;
import ro.pippo.controller.Controller;
import ro.pippo.controller.GET;
import ro.pippo.controller.Path;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/chat")
public class ChatController extends Controller {

  private ChatService chatService;

  @Inject
  public ChatController(ChatService chatService) {
    this.chatService = chatService;
  }

  @GET("/?")
  public void list() {
    List<ChatMessage> chatMessages = chatService.getChatMessages(null);

    Map<LocalDate, List<ChatMessage>> messagesByDay = chatMessages.stream()
        .collect(Collectors.groupingBy(r -> r.getTime().toLocalDate(), LinkedHashMap::new, Collectors.toList()));
    getResponse().bind("chatMessages", messagesByDay).render("chat/list");
  }
}
