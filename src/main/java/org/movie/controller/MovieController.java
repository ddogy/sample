package org.movie.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.movie.domain.MovieAttachVO;
import org.movie.domain.MovieVO;
import org.movie.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/movie")
@Log4j
@AllArgsConstructor
public class MovieController {
	private MovieService service;
	
	@GetMapping("/list")
	public void list(Model model) {
		log.info("movie list");
		
		model.addAttribute("mlist", service.getList());
	}
	
	@GetMapping("/register")
	public void register() {
		
	}
	
	@PostMapping("/register")
	public String register(MovieVO mVO, RedirectAttributes rttr) {
		log.info("register"+mVO);
		
		if(mVO.getAttachList() != null) {
			mVO.getAttachList().forEach(attach -> log.info(attach));
		}
		
		service.register(mVO);
		rttr.addAttribute("result", mVO.getCode());
		
		return "redirect:/movie/list";
	}
	
	@GetMapping("/get")
	public void get(@RequestParam("code") Long code, Model model) {
		log.info("/get");
		
		model.addAttribute("movie", service.getMovie(code));
	}
	
	public ResponseEntity<List<MovieAttachVO>> getAttachList(Long code){
		log.info("getattchlist:"+code);
		
		return new ResponseEntity<>(service.getAttachList(code), HttpStatus.OK);
	}
	@GetMapping("/uploadAjax") 
	public void uploadAjax() {
		log.info("upload ajax");
	}
	
	
	
	@GetMapping("/delete")
	public String remove(@RequestParam("code") Long code, RedirectAttributes rttr) {
		log.info("delete....."+code);
		
		List<MovieAttachVO> attachList = service.getAttachList(code);
		
		if(service.remove(code)) {
			deleteFiles(attachList);
			rttr.addFlashAttribute("result", "success");
		}
		
		return "redirect:/movie/list";
	}
	
	private void deleteFiles(List<MovieAttachVO> attachList) {
		if(attachList== null || attachList.size() == 0) {
			return;
		}
		
		attachList.forEach(attach ->{
			try {
				Path file = Paths.get("c:\\movieUpload\\"+attach.getUuid()+"_"+attach.getFileName());
				Files.deleteIfExists(file);
				
				Path thumbNail = Paths.get("c:\\movieUpload\\"+"s_"+attach.getUuid()+"_"+attach.getFileName());
				Files.delete(thumbNail);
			}catch(Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	@PostMapping("/uploadAjaxAction")
	public void uploadAjaxPost(MultipartFile[] uploadFile) {
		log.info("update ajax post.........");
		String uploadFolder = "C:\\moviepload";
		
		//make folder
		File uploadPath = new File(uploadFolder);
		log.info("upload path : " + uploadPath);
		
		if(uploadPath.exists() ==false) {
			uploadPath.mkdirs();
		} //make folder
		
		for(MultipartFile multipartFile : uploadFile) {
			log.info("-------------");
			log.info("Upload File Name : " + multipartFile.getOriginalFilename());
			log.info("Upload File Size : " + multipartFile.getSize());
			String uploadFileName = multipartFile.getOriginalFilename();
			
			//IE has file path
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
			log.info("only file name : " + uploadFileName);
			
			UUID uuid = UUID.randomUUID();
			uploadFileName = uuid.toString() + "_" + uploadFileName;
			File saveFile = new File(uploadPath, uploadFileName);
			
			try {
				multipartFile.transferTo(saveFile);
				
			}catch(Exception e) {
				log.error(e.getMessage());
			}
		}
	}
}
		






