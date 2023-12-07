package BoardProject.demo.service;


import BoardProject.demo.Component.FileStore;
import BoardProject.demo.domain.Attachment;
import BoardProject.demo.domain.Board;
import BoardProject.demo.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final FileStore fileStore;
    @Autowired
    public AttachmentService(AttachmentRepository attachmentRepository, FileStore fileStore) {
        this.attachmentRepository = attachmentRepository;
        this.fileStore = fileStore;
    }

    //READ
    //입력 게시판번호, 파일이름
    //출력 Attachment 객체
    public Attachment findAttachmentsByBoard(Board board) {
        return attachmentRepository.findByBoard(board);
    }

    //바일 저장
    public void saveAttachments(Attachment attachments) throws IOException {
        attachmentRepository.save(attachments);
    }
    public void checkNullByAttachments(MultipartFile multipartFile, Board board) {
        try {
            if (multipartFile != null && !multipartFile.isEmpty()) {
                Attachment attachments = fileStore.saveAttachments(multipartFile, board);
                saveAttachments(attachments);
            }
        } catch (IOException e) {
            System.out.println("파일 체크에서 문제 발생");
        }
    }
    @Transactional
    public void deleteAttachmentsByBoardId(Long id) {
        attachmentRepository.deleteByBoardId(id);
    }
}
