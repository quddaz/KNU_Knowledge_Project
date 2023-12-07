package BoardProject.demo.service;
import BoardProject.demo.domain.GptAnswer;
import BoardProject.demo.repository.GptAnswerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GptAnswerService {
    private final GptAnswerRepository gptAnswerRepository;

    public GptAnswerService(GptAnswerRepository gptAnswerRepository) {
        this.gptAnswerRepository = gptAnswerRepository;
    }

    @Value("${gpt.api-key}")
    private String API_KEY;
    private static final String ENDPOINT = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL = "gpt-3.5-turbo";

    public String sendMessage(String prompt, float temperature, int maxTokens) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        Map<String, Object> requestBody = new HashMap<>();
        List<Map<String, String>> messages = new ArrayList<>(); // Change the type to Map<String, String>

        // Create a user message
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", "user");
        messages.add(userMessage);

        // Create a prompt message
        Map<String, String> promptMessage = new HashMap<>();
        promptMessage.put("role", "system");
        promptMessage.put("content", prompt);
        messages.add(promptMessage);

        requestBody.put("model", MODEL);
        requestBody.put("temperature", temperature);
        requestBody.put("max_tokens", maxTokens);
        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(ENDPOINT, requestEntity, Map.class);

        // 이 부분을 필요에 따라 응답 데이터를 가공하거나 로그에 남기는 등의 작업을 수행할 수 있습니다.
        // 예: String responseBody = response.getBody().toString();

        // Jackson ObjectMapper를 사용하여 응답 데이터를 JSON 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String responseBody = objectMapper.writeValueAsString(response.getBody());
            return responseBody;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error converting response to JSON string";
        }
    }


    //CREATE
    public void createGptAnswer(GptAnswer gptAnswer) {
        gptAnswerRepository.save(gptAnswer);
    }

    // ChatGPT 에게 질문을 던집니다.
    public String getChatResponse(String prompt) {
        prompt = prompt + "에 대해 답변 해줘";
        return sendMessage(prompt, 1.0f, 1000);
    }

    public GptAnswer getGptAnswerByBoardId(Long id) {
        Optional<GptAnswer> gptAnswerOptional = gptAnswerRepository.findByBoardId(id);
        return gptAnswerOptional.orElse(null);
    }
    public void updateGptAnswer(GptAnswer gptAnswer) {
        // Assuming GptAnswerRepository extends JpaRepository or a similar interface
        gptAnswerRepository.save(gptAnswer);
    }
}
