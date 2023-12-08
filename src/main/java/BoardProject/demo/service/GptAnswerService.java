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

    /**
     * 주어진 프롬프트와 온도, 토큰 제한을 이용하여 OpenAI GPT 모델에 메시지를 전송하고, 응답을 반환하는 메소드입니다.
     *
     * @param prompt     사용자에게 보여질 프롬프트 메시지
     * @param temperature 모델의 창의성을 조절하는 온도
     * @param maxTokens   생성될 텍스트의 최대 토큰 수
     * @return OpenAI GPT 모델에서 생성된 응답 메시지(JSON 형식)
     */
    public String sendMessage(String prompt, float temperature, int maxTokens) {
        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        // HTTP 요청 본문 설정
        Map<String, Object> requestBody = new HashMap<>();
        List<Map<String, String>> messages = new ArrayList<>();

        // 사용자 메시지 생성 및 추가
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", "user");
        messages.add(userMessage);

        // 시스템(프롬프트) 메시지 생성 및 추가
        Map<String, String> promptMessage = new HashMap<>();
        promptMessage.put("role", "system");
        promptMessage.put("content", prompt);
        messages.add(promptMessage);

        // HTTP 요청 본문에 필요한 정보 추가
        requestBody.put("model", MODEL);
        requestBody.put("temperature", temperature);
        requestBody.put("max_tokens", maxTokens);
        requestBody.put("messages", messages);

        // HTTP 요청 엔터티 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // REST 템플릿을 사용하여 OpenAI GPT에 요청을 전송하고 응답을 받음
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(ENDPOINT, requestEntity, Map.class);

        // 응답 데이터를 JSON 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String responseBody = objectMapper.writeValueAsString(response.getBody());
            return responseBody;
        } catch (Exception e) {
            // 예외 발생 시 로깅하고 에러 메시지 반환
            e.printStackTrace();
            return "Error converting response to JSON string";
        }
    }


    /**
     * ChatGPT 답변을 생성합니다.
     *
     * @param gptAnswer ChatGPT 답변 엔터티
     */
    public void createGptAnswer(GptAnswer gptAnswer) {
        gptAnswerRepository.save(gptAnswer);
    }

    /**
     * ChatGPT에게 특정 프롬프트에 대한 응답을 요청하고, 응답을 반환합니다.
     *
     * @param prompt ChatGPT에 전달할 프롬프트 메시지
     * @return ChatGPT의 응답 메시지(JSON 형식)
     */
    public String getChatResponse(String prompt) {
        // ChatGPT에게 질문을 전송하여 응답을 받아옴
        prompt = prompt + "에 대해 답변 해줘";
        return sendMessage(prompt, 1.0f, 1000);
    }

    /**
     * 특정 게시글에 대한 ChatGPT 답변을 조회합니다.
     *
     * @param id 조회할 게시글의 ID
     * @return 게시글에 대한 ChatGPT 답변 엔터티
     */
    public GptAnswer getGptAnswerByBoardId(Long id) {
        Optional<GptAnswer> gptAnswerOptional = gptAnswerRepository.findByBoardId(id);
        return gptAnswerOptional.orElse(null);
    }

    /**
     * 주어진 ChatGPT 답변을 업데이트합니다.
     *
     * @param gptAnswer 업데이트할 ChatGPT 답변 엔터티
     */
    public void updateGptAnswer(GptAnswer gptAnswer) {
        gptAnswerRepository.save(gptAnswer);
    }
}
