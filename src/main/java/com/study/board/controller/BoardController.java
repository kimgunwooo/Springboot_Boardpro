package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;
    @GetMapping("/board/write") //localhost:8080/board/write
    public String boardWriteForm(){
        return "boardwrite";
    }

    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, Model model, MultipartFile file) throws Exception{
        boardService.write(board,file);
        model.addAttribute("message","글 작성이 완료되었습니다.");
//      model.addAttribute("message","글 작성이 실패하였습니다.");
        model.addAttribute("searchUrl","/board/list");
//        System.out.println(board.getId());
//        System.out.println(board.getTitle());
//        System.out.println(board.getContent());
        return "message";
    }
    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page=0,size=10,sort="id",direction = Sort.Direction.DESC) Pageable pageable,
                            String searchKeyword){
        Page<Board> list = null;
        if(searchKeyword == null){
            //        PageableDefault 어노테이션을 통해 기본 설정을 해줄 수 있음. DESE 같은 내림차순 정렬
            list = boardService.boardList(pageable);
        }
        else{
            list = boardService.boardSearchList(searchKeyword,pageable);
        }



        int nowPage = list.getPageable().getPageNumber() + 1; // pagea ble.getPageNumber()
        // pageable이 0부터 시작하기 때문에 1을 더해줘야 함.
        int startPage = Math.max(nowPage-4,1); // 두 값을 비교해서 높은 값을 반환
        int endPage = Math.min(nowPage+5,list.getTotalPages()); // 두 값을 비교해서 낮은 값 반환

        model.addAttribute("list",list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "boardlist";
    }
    @GetMapping("/board/view") // localhost:8080/board/view?id=1 이렇게 적으면 전달 됨. (get방식 파라미터)
    public String boardView(Model model, Integer id){
        model.addAttribute("board",boardService.boardView(id));
        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id){
        boardService.boardDelete(id);
        return "redirect:/board/list";
    }

    @GetMapping("/board/modify/{id}") //id부분을 인식
    public String boardModify(@PathVariable("id") Integer id,
                              Model model){// 인식한 id를 Integer형태의 id로 들어옴
                                //이 방식은 쿼리방식처럼 ?가 붙지않고 /뒤에 바로 숫자가 붙음
        model.addAttribute("board", boardService.boardView(id));
        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id,Board board,Model model,MultipartFile file) throws Exception{
        Board boardTemp = boardService.boardView(id); // 기존에 있던 내용 담아서옴
        boardTemp.setTitle(board.getTitle()); // 새로 받아온 값으로 타이틀 덮어쓰기
        boardTemp.setContent(board.getContent()); // // 내용 덮어쓰기

        boardService.write(boardTemp,file); // 적용해주기

        model.addAttribute("message","글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl","/board/list");

        return "message";
    }
}
