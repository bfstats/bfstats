package io.github.bfstats.controller;

import io.github.bfstats.model.ChatMessage;
import io.github.bfstats.service.ChatService;
import io.github.bfstats.util.SortUtils;
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

    Map<LocalDate, List<ChatMessage>> chatMessagesPerDay = chatMessages.stream()
        .collect(Collectors.groupingBy(r -> r.getTime().toLocalDate(), LinkedHashMap::new, Collectors.toList()));

    int totalMessagesCount = chatService.getTotalMessagesCount();

    getResponse()
        .bind("chatMessagesPerDay", chatMessagesPerDay)
        .bind("totalMessagesCount", totalMessagesCount)
        .bind("currentPage", page)
        .render("chat/list");
  }
}
