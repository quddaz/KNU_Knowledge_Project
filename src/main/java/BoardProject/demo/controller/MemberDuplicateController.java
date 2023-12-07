package BoardProject.demo.controller;

import BoardProject.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberDuplicateController {
    private final MemberService memberService;

    @PostMapping("/member/idValidation")
    public ResponseEntity<Map<String, Object>> validateId(@RequestBody Map<String, String> requestData) {
        String id = requestData.get("id");
        Map<String, Object> response = new HashMap<>();
        boolean isValid = memberService.isExistMemberById(id);
        response.put("isValid", isValid);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/member/nameValidation")
    public ResponseEntity<Map<String, Object>> validateName(@RequestBody Map<String, String> requestData) {
        String name = requestData.get("name");
        Map<String, Object> response = new HashMap<>();
        boolean isValid = memberService.isExistMemberByName(name);
        response.put("isValid", isValid);
        return ResponseEntity.ok(response);
    }
}
