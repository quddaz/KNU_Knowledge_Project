package BoardProject.demo.Component;


import BoardProject.demo.domain.Attachment;
import BoardProject.demo.domain.Board;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileStore {
  //파일 기본 저장 위치 경로 프로퍼티에 저장되어 있음
  @Value("${file.dir}/")
  private String fileDirPath;

  //파일의 이름을 만들어주는 메소드
  //UUID로 랜덤으로 생성
  private String createStoreFilename(String originalFilename) {
    String uuid = UUID.randomUUID().toString();
    // 기존 파일의 확장자를 가져옴
    String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
    String storeFilename = uuid + ext;
    return storeFilename;
  }

  public Attachment saveAttachments(MultipartFile multipartFile, Board board) throws IOException {
    Attachment attachment = storeFile(multipartFile);
    attachment.setBoard(board); // Board 설정
    return attachment;
  }
  //파일 저장 경로를 만드는 위치
  public String createPath(String storeFilename) {
    String viaPath = "images/";
    return fileDirPath + viaPath + storeFilename;
  }
  //파일 확장자 추출
  private String extractFileExtension(String originalFilename) {
    int lastIndex = originalFilename.lastIndexOf(".");

    if (lastIndex != -1 && lastIndex < originalFilename.length() - 1) {
      return originalFilename.substring(lastIndex + 1).toLowerCase(); // 확장자를 소문자로 반환
    } else {
      return ""; //확장자가 없는 경우 예외 처리
    }
  }
  // 파일을 저장하는 메서드
  public Attachment storeFile(MultipartFile multipartFile) throws IOException {
    if (multipartFile.isEmpty()) {
      return null;
    }
    String originalFilename = multipartFile.getOriginalFilename();
    String storeFilename = createStoreFilename(originalFilename);
    multipartFile.transferTo(new File(createPath(storeFilename)));
    String filePath = createPath(storeFilename);
    String fileExtension = extractFileExtension(originalFilename);

    return Attachment.builder()
        .originFilename(originalFilename)
        .storePath(storeFilename)
        .filePath(filePath)
        .fileExtension(fileExtension)
        .build();
  }
}