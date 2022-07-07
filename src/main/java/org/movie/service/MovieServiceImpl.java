package org.movie.service;

import java.util.List;

import org.movie.domain.MovieAttachVO;
import org.movie.domain.MovieVO;
import org.movie.mapper.MovieAttachMapper;
import org.movie.mapper.MovieMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;


@Service
@Log4j
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {
	private MovieMapper movieMapper;
	private MovieAttachMapper attachMapper;

	@Override
	public List<MovieVO> getList() {
		// TODO Auto-generated method stub
		log.info("movelist");
		
		return movieMapper.selectAllMovie();
	}
	
	@Override
	@Transactional
	public void register(MovieVO mVO) {
		log.info("register..."+mVO);
		
		movieMapper.insertMovie(mVO);
		
		if(mVO.getAttachList() == null || mVO.getAttachList().size()<0) {
			return;
		}
		
		mVO.getAttachList().forEach(attach -> {
			attach.setCode(mVO.getCode());
			
			attachMapper.insert(attach);
		});
	}
	
	@Override
	public MovieVO getMovie(Long code) {
		log.info("movie info..."+code);
		
		return movieMapper.selectProductByCode(code);
	}
	
	@Override
	public List<MovieAttachVO> getAttachList(Long code){
		log.info("get attach list by code");
		
		return attachMapper.findByCode(code);
	}

	@Override
	@Transactional
	public boolean remove(Long code) {
		log.info("delete movie..."+code);
		
		attachMapper.deleteAll(code);
		
		return movieMapper.deleteMovie(code) == 1;
	}

}







