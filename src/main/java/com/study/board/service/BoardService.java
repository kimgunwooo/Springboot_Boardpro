package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;
    // 글 작성 처리 + 파일 처리
    public void write(Board board, MultipartFile file) throws Exception{

        String projectPath = System.getProperty("user.dir") +"\\src\\main\\resources\\static\\files";
        // 저장할 경로를 지정

        UUID uuid = UUID.randomUUID();
        // 파일 이름에 붙일 랜덤 식별자 생성

        String fileName = uuid + "_"+ file.getOriginalFilename();
        // uuid_ + 입력값으로 들어온 file의 이름을 붙여서 저장

        File saveFile = new File(projectPath,fileName);
        // File 클래스를 사용해 껍데기 생성, 입력값으로 들어온 file을 저장해주기 위해
        file.transferTo(saveFile);

        board.setFilename(fileName); //파일의 이름
        board.setFilepath("/files/" + fileName); //파일의 경로와 이름

        boardRepository.save(board);
    }

    // 게시글 리스트 처리
    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
//      findAll-DB의 모든 정보를 가지고옴.
//      Pageable 클래스를 넘겨주게 되면, 페이지가 몇 페이지인지와 size를 담아서 보내줄 수 있음
    }

    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable){
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }


    //특정 게시글 불러오기
    public Board boardView(Integer id){
        return boardRepository.findById(id).get();
    }

    //특정 게시글 삭제
    public void boardDelete(Integer id){
        boardRepository.deleteById(id);
    }
}
