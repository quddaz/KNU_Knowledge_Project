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

    /**
     * 특정 게시글에 대한 파일을 반환하는 메소드
     *
     * @param board 게시글 객체
     * @return 해당 게시글의 파일을 반환합니다.
     * @throws IllegalArgumentException 만약 게시글이 null이면 예외를 던집니다.
     */
    public Attachment findAttachmentsByBoard(Board board) {
        if (board == null) {
            throw new IllegalArgumentException("findAttachmentsByBoard : 게시글이 null입니다.");
        }
        return attachmentRepository.findByBoard(board);
    }

    /**
     * 파일을 저장하는 메소드
     *
     * @param attachments 객체
     * @return
     * @throws IllegalArgumentException 만약 게시글이 null이면 예외를 던집니다.
     */
    public void saveAttachments(Attachment attachments)  {
        if (attachments == null) {
            throw new IllegalArgumentException("saveAttachments : 파일객체가 null입니다.");
        }
        attachmentRepository.save(attachments);
    }

    /**
     * 첨부 파일이 비어 있지 않으면 해당 파일을 저장하고, 게시글에 연결하는 메소드
     *
     * @param multipartFile 첨부 파일
     * @param board 게시글 객체
     */
    public void checkNullByAttachments(MultipartFile multipartFile, Board board) {
        try {
            if (multipartFile != null && !multipartFile.isEmpty()) {
                Attachment attachments = fileStore.saveAttachments(multipartFile, board);
                saveAttachments(attachments);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("checkNullByAttachments : 파일 체크에서 문제 발생.");
        }
    }

}
