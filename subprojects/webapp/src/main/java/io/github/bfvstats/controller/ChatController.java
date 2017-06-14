package io.github.bfvstats.controller;

import io.github.bfvstats.model.ChatMessage;
import io.github.bfvstats.service.ChatService;
import io.github.bfvstats.util.SortUtils;
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
    int page = SortUtils.getPageFromRequest(getRequest());

    List<ChatMessage> chatMessages = chatService.getChatMessages(null, page);

    Map<LocalDate, List<ChatMessage>> messagesByDay = chatMessages.stream()
        .collect(Collectors.groupingBy(r -> r.getTime().toLocalDate(), LinkedHashMap::new, Collectors.toList()));

    int totalMessagesCount = chatService.getTotalMessagesCount();

    getResponse()
        .bind("chatMessages", messagesByDay)
        .bind("totalMessagesCount", totalMessagesCount)
        .bind("currentPage", page)
        .render("chat/list");
  }
}
