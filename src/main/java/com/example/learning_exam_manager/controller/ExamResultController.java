package com.example.learning_exam_manager.controller;

import com.example.learning_exam_manager.dto.ExamResultDto;
import com.example.learning_exam_manager.form.ExamResultForm;
import com.example.learning_exam_manager.service.ExamResultService;
import com.example.learning_exam_manager.service.ExamService;
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
import java.time.LocalDate;

@Controller
@RequestMapping("/exam-results")
public class ExamResultController {
    
    private static final Logger logger = LoggerFactory.getLogger(ExamResultController.class);
    
    private final ExamResultService examResultService;
    private final ExamService examService;
    
    @Autowired
    public ExamResultController(ExamResultService examResultService, ExamService examService) {
        this.examResultService = examResultService;
        this.examService = examService;
    }
    
    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {
        logger.debug("試験結果一覧を表示します: keyword={}, page={}, size={}", keyword, page, size);
        // Pageableの作成（ページ番号、サイズ、ソート順を指定）
        // 試験結果は受験日の降順でソート（新しい順）
        Pageable pageable = PageRequest.of(page, size, Sort.by("takenAt").descending());
        
        Page<ExamResultDto> examResultsPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            examResultsPage = examResultService.search(keyword, pageable);
            model.addAttribute("keyword", keyword);  // 検索キーワードを保持
        } else {
            examResultsPage = examResultService.findAll(pageable);
        }
        
        model.addAttribute("examResults", examResultsPage.getContent());  // 現在のページのデータ
        model.addAttribute("currentPage", page);  // 現在のページ番号
        model.addAttribute("totalPages", examResultsPage.getTotalPages());  // 総ページ数
        model.addAttribute("totalItems", examResultsPage.getTotalElements());  // 総件数
        model.addAttribute("pageSize", size);  // ページサイズ
        model.addAttribute("activePage", "exam-results");
        model.addAttribute("title", "試験結果一覧");
        logger.info("試験結果一覧を表示しました: totalPages={}, totalItems={}, pageSize={}", examResultsPage.getTotalPages(), examResultsPage.getTotalElements(), size);
        return "exam-results/list";
    }
    
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        logger.debug("試験結果詳細を表示します: ID={}", id);
        ExamResultDto examResult = examResultService.findById(id);
        model.addAttribute("examResult", examResult);
        logger.info("試験結果詳細を表示しました: ID={}, 試験名={}, 得点={}", id, examResult.getExamName(), examResult.getScore());
        return "exam-results/detail";
    }
    
    @GetMapping("/new")
    public String newForm(Model model) {
        logger.debug("試験結果登録画面を表示します");
        ExamResultForm form = new ExamResultForm();
        form.setTakenAt(LocalDate.now());
        model.addAttribute("examResultForm", form);
        model.addAttribute("exams", examService.findAll());
        logger.info("試験結果登録画面を表示しました");
        return "exam-results/new";
    }
    
    @PostMapping
    public String create(@Valid @ModelAttribute ExamResultForm form, 
                        BindingResult result,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("exams", examService.findAll());
            return "exam-results/new";
        }
        examResultService.create(form);
        logger.info("試験結果を登録しました: 試験ID={}, 得点={}", form.getExamId(), form.getScore());
        redirectAttributes.addFlashAttribute("message", "試験結果を登録しました");
        return "redirect:/exam-results";
    }
    
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        logger.debug("試験結果編集画面を表示します: ID={}", id);
        ExamResultDto examResult = examResultService.findById(id);
        ExamResultForm form = new ExamResultForm(
                examResult.getExamId(),
                examResult.getScore(),
                examResult.getTakenAt()
        );
        model.addAttribute("examResultForm", form);
        model.addAttribute("examResultId", id);
        model.addAttribute("exams", examService.findAll());
        logger.info("試験結果編集画面を表示しました: ID={}, 試験名={}, 得点={}", id, examResult.getExamName(), examResult.getScore());
        return "exam-results/edit";
    }
    
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                        @Valid @ModelAttribute ExamResultForm form,
                        BindingResult result,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            logger.warn("試験結果編集画面でエラーが発生しました: ID={}", id);
            model.addAttribute("examResultId", id);
            model.addAttribute("exams", examService.findAll());
            return "exam-results/edit";
        }
        examResultService.update(id, form);
        logger.info("試験結果を更新しました: ID={}, 得点={}", id, form.getScore());
        redirectAttributes.addFlashAttribute("message", "試験結果を更新しました");
        return "redirect:/exam-results";
    }
    
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        logger.warn("試験結果を削除します: ID={}", id);
        examResultService.delete(id);
        logger.info("試験結果を削除しました: ID={}", id);
        redirectAttributes.addFlashAttribute("message", "試験結果を削除しました");
        return "redirect:/exam-results";
    }
}

