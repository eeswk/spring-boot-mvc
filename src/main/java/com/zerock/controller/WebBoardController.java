package com.zerock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zerock.domain.WebBoard;
import com.zerock.persistence.WebBoardRepository;
import com.zerock.vo.PageMaker;
import com.zerock.vo.PageVO;

import lombok.extern.java.Log;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/boards/")
@Log
public class WebBoardController {
	
	@Autowired
	private WebBoardRepository repo;

/*	@GetMapping("/list")
	public void list(
			@PageableDefault(
					direction=Sort.Direction.DESC,
					sort="bno",
					size=10,
					page=0) Pageable page) {
		log.info("list() called..." + page);
	}*/
	
	@GetMapping("/list")
	public void list(PageVO vo, Model model) {
		Pageable page = vo.makePageable(0, "bno");
		
		Page<WebBoard> result = repo.findAll(
				repo.makePredicate(vo.getType(), vo.getKeyword()), page);
		
		log.info("" + page);
		log.info("" + result);
		log.info("" + result.getContent());
		
		log.info("Total Page Number: " + result.getTotalPages() );
		
		
		model.addAttribute("result", new PageMaker(result));
	}

	@GetMapping("/register")
	public void regsiterGET(@ModelAttribute("vo") WebBoard vo) {
		log.info("register get");
	}

	@PostMapping("/register")
	public String regsterPOST(@ModelAttribute("vo") WebBoard vo, RedirectAttributes rttr) {
		log.info("register post");
		log.info("" + vo);

		repo.save(vo);
		rttr.addFlashAttribute("msg", "success");
		return "redirect:/boards/list";
	}

	@GetMapping("/view")
	public void view(Long bno, @ModelAttribute("pageVO") PageVO vo, Model model) {
		log.info("" + bno);
		repo.findById(bno).ifPresent(board -> model.addAttribute("vo", board));
	}

	@GetMapping("/modify")
	public void modify(Long bno, @ModelAttribute("pageVO") PageVO vo, Model model) {
		log.info("modify bno: " + bno);
		repo.findById(bno).ifPresent(board -> model.addAttribute("vo", board));
	}

	@PostMapping("/delete")
	public String delete(Long bno, PageVO vo, RedirectAttributes rttr) {
		log.info("delete bno: " + bno);
		repo.deleteById(bno);
		rttr.addFlashAttribute("msg","success");
		rttr.addFlashAttribute("page", vo.getPage());
		rttr.addFlashAttribute("size", vo.getSize());
		rttr.addFlashAttribute("type", vo.getType());
		rttr.addFlashAttribute("keyword", vo.getKeyword());

		return "redirect:/boards/list";
	}

	@PostMapping("/modify")
	public String modifyPost(WebBoard board, PageVO vo, RedirectAttributes rttr) {
		log.info("Modify WebBoard: " + board);

		repo.findById(board.getBno()).ifPresent(origin -> {
			origin.setTitle(board.getTitle());
			origin.setContent(board.getContent());

			repo.save(origin);
			rttr.addFlashAttribute("msg", "success");
			rttr.addFlashAttribute("bno", origin.getBno());
		});

		rttr.addFlashAttribute("page", vo.getPage());
		rttr.addFlashAttribute("size", vo.getSize());
		rttr.addFlashAttribute("type", vo.getType());
		rttr.addFlashAttribute("keyowrd", vo.getKeyword());

		return "redirect:/boards/list";

	}
	
	
}
