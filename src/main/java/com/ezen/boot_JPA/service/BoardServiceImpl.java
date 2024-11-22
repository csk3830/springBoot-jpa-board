package com.ezen.boot_JPA.service;

import com.ezen.boot_JPA.dto.BoardDTO;
import com.ezen.boot_JPA.dto.BoardFileDTO;
import com.ezen.boot_JPA.dto.FileDTO;
import com.ezen.boot_JPA.entity.Board;
import com.ezen.boot_JPA.entity.File;
import com.ezen.boot_JPA.repository.BoardRepository;
import com.ezen.boot_JPA.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;

    @Override
    public Long insert(BoardDTO boardDTO) {
        // 저장 객체는 Board
        // save() : insert 후 저장 객체의 id를 리턴
        // save() Entity 객체를 파라미터로 전송
        return boardRepository.save(convertDtoToEntity(boardDTO)).getBno();
    }

    @Transactional
    @Override
    public long insert(BoardFileDTO boardFileDTO) {
        long bno = insert(boardFileDTO.getBoardDTO());
        if(bno > 0 && boardFileDTO.getFileDTOList() != null) {
            for(FileDTO fileDTO : boardFileDTO.getFileDTOList()) {
                fileDTO.setBno(bno);
                bno = fileRepository.save(convertDtoToEntity(fileDTO)).getBno();
            }
        }
        return bno;
    }

    /*@Override
    public List<BoardDTO> getList() {
        // 컨트롤러로 보내야 하는 리턴은 List<BoardDTO>
        // DB에서 가져오는 리턴은 List<Board>    > BoardDTO 객체로 변환
        // findAll()
        // 정렬 : Sort.by(Sort.Direction.DESC, "정렬기준 컬럼명")
        List<Board> boardList = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "bno"));
        *//*List<BoardDTO> boardDTOList = new ArrayList<>();
        for (Board board : boardList) {
            boardDTOList.add(convertEntityToDto(board));
        }*//*

        List<BoardDTO> boardDTOList = boardList.stream().map(b -> convertEntityToDto(b)).toList();

        return boardDTOList;
    }*/

    @Override
    public Page<BoardDTO> getList(int pageNo) {
        // pageNo = 0부터 시작
        // 0 => limit 0, 10 / 1 => limit 10, 10
        Pageable pageable = PageRequest.of(pageNo, 10, Sort.by("bno").descending());
        Page<Board> list = boardRepository.findAll(pageable);
        Page<BoardDTO> boardDTOlist = list.map(b -> convertEntityToDto(b));
        return boardDTOlist;
    }

    @Override
    public long fileRemove(String uuid) {
        Optional<File> optional = fileRepository.findById(uuid);
        if(optional.isPresent()){
            fileRepository.deleteById(uuid);
            return optional.get().getBno();
        }
        return 0;
    }

    @Override
    public Long modify(BoardFileDTO boardFileDTO) {
        long bno = insert(boardFileDTO);
        return bno;

        /*Optional<Board> optional = boardRepository.findById(boardFileDTO.getBoardDTO().getBno());

        if(optional.isPresent()) {
            Board board = optional.get();
            board.setTitle(boardFileDTO.getBoardDTO().getTitle());
            board.setContent(boardFileDTO.getBoardDTO().getContent());
            // 다시 저장 (기존 객체에 덮어쓰기
            long bno = insert(boardFileDTO);
            if(bno > 0 && boardFileDTO.getFileDTOList() != null) {
                for(FileDTO fileDTO : boardFileDTO.getFileDTOList()) {
                    fileDTO.setBno(bno);
                    bno = fileRepository.save(convertDtoToEntity(fileDTO)).getBno();
                }
            }
            return bno;
        }
        return 0L;*/
    }

    @Override
    public FileDTO getFile(String uuid) {
        Optional<File> optional = fileRepository.findById(uuid);
        if(optional.isPresent()){
            FileDTO fileDTO = convertEntityToDto(optional.get());
            return fileDTO;
        }

        return null;
    }

    /*@Override
    public Long modify(BoardDTO boardDTO) {
        // update : jpa는 업데이트가 없음.
        // 기존 객체를 가져와서 set 수정 후 다시 저장
        Optional<Board> optional = boardRepository.findById(boardDTO.getBno());
        if(optional.isPresent()) {
            Board entity = optional.get();
            entity.setTitle(boardDTO.getTitle());
            entity.setContent(boardDTO.getContent());
            // 다시 저장 (기존 객체에 덮어쓰기
            return boardRepository.save(entity).getBno();
        }
        return null;

    }*/

    /*@Override
    public BoardDTO getDetail(Long bno) {
        *//*  findById : 아이디(PK)를 주고 해당 객체를 리턴
            findById의 리턴타입 Optional<Board> 타입으로 리턴
            Optional<T> : nullPointException이 발생하지 않도록 도와줌.
            Optional.isEmtpy() : null일 경우 확인가능 true / false
            Optional.isPresent() : 값이 있는지 확인 true / false
            Optional.get() : 객체 가져오기
         *//*
        Optional<Board> optional = boardRepository.findById(bno);
        if(optional.isPresent()) {
            BoardDTO boardDTO = convertEntityToDto(optional.get());
            return boardDTO;
        }
        return null;
    }*/

    @Override
    public BoardFileDTO getDetail(Long bno){
        Optional<Board> optional = boardRepository.findById(bno);
        if(optional.isPresent()) {
            BoardDTO boardDTO = convertEntityToDto(optional.get());

            // file bno에 일치하는 모든 파일 리스트를 가져오기
            // select * from file where bno = #{bno}
            List<File> fileList = fileRepository.findByBno(bno);
            List<FileDTO> fileDTOList = fileList.stream().map(f -> convertEntityToDto(f)).toList();
            BoardFileDTO boardFileDTO = new BoardFileDTO(boardDTO, fileDTOList);
            log.info(">>>> boardFileDTO >> {}", boardFileDTO);
            return boardFileDTO;
        }

        return null;
    }

    // 삭제 : deleteById(id)
    @Override
    public void delete(Long bno) {
        boardRepository.deleteById(bno);
    }

}
