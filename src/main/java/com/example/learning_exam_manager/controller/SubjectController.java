package com.example.learning_exam_manager.controller;

import com.example.learning_exam_manager.dto.SubjectDto;
import com.example.learning_exam_manager.form.SubjectForm;
import com.example.learning_exam_manager.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

    private static final Logger logger = LoggerFactory.getLogger(SubjectController.class);
    
    private final SubjectService subjectService;
    
    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }
    
    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {
        logger.debug("科目一覧を表示します: keyword={}, page={}, size={}", keyword, page, size);
        // Pageableの作成（ページ番号、サイズ、ソート順を指定）
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        
        Page<SubjectDto> subjectsPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            subjectsPage = subjectService.searchByName(keyword, pageable);
            model.addAttribute("keyword", keyword);
        } else {
            subjectsPage = subjectService.findAll(pageable);
        }
        
        model.addAttribute("subjects", subjectsPage.getContent());  // 現在のページのデータ
        model.addAttribute("currentPage", page);  // 現在のページ番号
        model.addAttribute("totalPages", subjectsPage.getTotalPages());  // 総ページ数
        model.addAttribute("totalItems", subjectsPage.getTotalElements());  // 総件数
        model.addAttribute("pageSize", size);  // ページサイズ
        model.addAttribute("activePage", "subjects");
        model.addAttribute("title", "科目一覧");
        logger.info("科目一覧を表示しました: totalPages={}, totalItems={}, pageSize={}", subjectsPage.getTotalPages(), subjectsPage.getTotalElements(), size);
        return "subjects/list";
    }
    
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        logger.debug("科目詳細を表示します: ID={}", id);
        SubjectDto subject = subjectService.findById(id);
        model.addAttribute("subject", subject);
        logger.info("科目詳細を表示しました: ID={}, 名前={}", id, subject.getName());
        return "subjects/detail";
    }
    
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("subjectForm", new SubjectForm());
        logger.debug("科目登録画面を表示します");
        logger.info("科目登録画面を表示しました");
        return "subjects/new";
    }
    
    @PostMapping
    public String create(@Valid @ModelAttribute SubjectForm form, 
                        BindingResult result,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "subjects/new";
        }
        subjectService.create(form);
        logger.info("科目を登録しました: 名前={}", form.getName());
        redirectAttributes.addFlashAttribute("message", "科目を登録しました");
        logger.info("リダイレクト先に遷移します");
        return "redirect:/subjects";
    }
    
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        logger.debug("科目編集画面を表示します: ID={}", id);
        SubjectDto subject = subjectService.findById(id);
        SubjectForm form = new SubjectForm(subject.getName(), subject.getDescription());
        model.addAttribute("subjectForm", form);
        model.addAttribute("subjectId", id);
        logger.info("科目編集画面を表示しました: ID={}, 名前={}", id, subject.getName());
        return "subjects/edit";
    }
    
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                        @Valid @ModelAttribute SubjectForm form,
                        BindingResult result,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            logger.warn("科目編集画面でエラーが発生しました: ID={}", id);
            return "subjects/edit";
        }
        subjectService.update(id, form);
        logger.info("科目を更新しました: ID={}, 名前={}", id, form.getName());
        redirectAttributes.addFlashAttribute("message", "科目を更新しました");
        return "redirect:/subjects";
    }
    
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        logger.warn("科目を削除します: ID={}", id);
        subjectService.delete(id);
        logger.info("科目を削除しました: ID={}", id);
        redirectAttributes.addFlashAttribute("message", "科目を削除しました");
        return "redirect:/subjects";
    }
}