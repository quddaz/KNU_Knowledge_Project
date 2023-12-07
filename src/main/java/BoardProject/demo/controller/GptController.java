package BoardProject.demo.controller;

import BoardProject.demo.domain.Board;
import BoardProject.demo.domain.GptAnswer;
import BoardProject.demo.service.BoardService;
import BoardProject.demo.service.GptAnswerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class GptController {
    private final GptAnswerService gptAnswerService;
    private final BoardService boardService;

    @GetMapping("/board/ai")
    public String useGpt(@ModelAttribute(name = "id") Long boardId) {
        Board board = boardService.findById(boardId);
        String getResponse = gptAnswerService.getChatResponse(board.getContent());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Parse the JSON response
            JsonNode jsonResponse = objectMapper.readTree(getResponse);

            // Access the content field inside the choices array
            String content = jsonResponse
                .path("choices")
                .get(0)
                .path("message")
                .path("content")
                .asText();

            // Check if a GptAnswer already exists for the board
            GptAnswer existingAnswer = gptAnswerService.getGptAnswerByBoardId(boardId);

            if (existingAnswer != null) {
                // If it exists, update the content
                existingAnswer.setContent(content);
                gptAnswerService.updateGptAnswer(existingAnswer);
            } else {
                // If it doesn't exist, create a new GptAnswer
                GptAnswer newGptAnswer = new GptAnswer(board, content);
                gptAnswerService.createGptAnswer(newGptAnswer);
            }

        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        return "redirect:/board/view?id=" + boardId;
    }

}
