package com.example.learning_exam_manager.controller;

import com.example.learning_exam_manager.dto.ExamDto;
import com.example.learning_exam_manager.form.ExamForm;
import com.example.learning_exam_manager.service.ExamService;
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
@RequestMapping("/exams")
public class ExamController {
    
    private static final Logger logger = LoggerFactory.getLogger(ExamController.class);
    
    private final ExamService examService;
    private final SubjectService subjectService;
    
    @Autowired
    public ExamController(ExamService examService, SubjectService subjectService) {
        this.examService = examService;
        this.subjectService = subjectService;
    }
    
    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {
        logger.debug("試験一覧を表示します: keyword={}, page={}, size={}", keyword, page, size);
        // Pageableの作成（ページ番号、サイズ、ソート順を指定）
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        
        Page<ExamDto> examsPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            examsPage = examService.searchByName(keyword, pageable);
            model.addAttribute("keyword", keyword);  // 検索キーワードを保持
        } else {
            examsPage = examService.findAll(pageable);
        }
        
        model.addAttribute("exams", examsPage.getContent());  // 現在のページのデータ
        model.addAttribute("currentPage", page);  // 現在のページ番号
        model.addAttribute("totalPages", examsPage.getTotalPages());  // 総ページ数
        model.addAttribute("totalItems", examsPage.getTotalElements());  // 総件数
        model.addAttribute("pageSize", size);  // ページサイズ
        model.addAttribute("activePage", "exams");
        model.addAttribute("title", "試験一覧");
        logger.info("試験一覧を表示しました: totalPages={}, totalItems={}, pageSize={}", examsPage.getTotalPages(), examsPage.getTotalElements(), size);
        return "exams/list";
    }
    
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        logger.debug("試験詳細を表示します: ID={}", id);
        ExamDto exam = examService.findById(id);
        model.addAttribute("exam", exam);
        logger.info("試験詳細を表示しました: ID={}, 試験名={}", id, exam.getExamName());
        return "exams/detail";
    }
    
    @GetMapping("/new")
    public String newForm(Model model) {
        logger.debug("試験登録画面を表示します");
        model.addAttribute("examForm", new ExamForm());
        model.addAttribute("subjects", subjectService.findAll());
        logger.info("試験登録画面を表示しました");
        return "exams/new";
    }
    
    @PostMapping
    public String create(@Valid @ModelAttribute ExamForm form, 
                        BindingResult result,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("subjects", subjectService.findAll());
            return "exams/new";
        }
        examService.create(form);
        logger.info("試験を登録しました: 試験名={}", form.getExamName());
        redirectAttributes.addFlashAttribute("message", "試験を登録しました");
        return "redirect:/exams";
    }
    
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        logger.debug("試験編集画面を表示します: ID={}", id);
        ExamDto exam = examService.findById(id);
        ExamForm form = new ExamForm(
                exam.getSubjectId(),
                exam.getExamName(),
                exam.getMaxScore(),
                exam.getPassingScore()
        );
        model.addAttribute("examForm", form);
        model.addAttribute("examId", id);
        model.addAttribute("subjects", subjectService.findAll());
        logger.info("試験編集画面を表示しました: ID={}, 試験名={}", id, exam.getExamName());
        return "exams/edit";
    }
    
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                        @Valid @ModelAttribute ExamForm form,
                        BindingResult result,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            logger.warn("試験編集画面でエラーが発生しました: ID={}", id);
            model.addAttribute("examId", id);
            model.addAttribute("subjects", subjectService.findAll());
            return "exams/edit";
        }
        examService.update(id, form);
        logger.info("試験を更新しました: ID={}, 試験名={}", id, form.getExamName());
        redirectAttributes.addFlashAttribute("message", "試験を更新しました");
        return "redirect:/exams";
    }
    
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        logger.warn("試験を削除します: ID={}", id);
        examService.delete(id);
        logger.info("試験を削除しました: ID={}", id);
        redirectAttributes.addFlashAttribute("message", "試験を削除しました");
        return "redirect:/exams";
    }
}

