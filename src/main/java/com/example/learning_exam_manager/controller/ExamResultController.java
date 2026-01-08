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

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/exam-results")
public class ExamResultController {
    
    private final ExamResultService examResultService;
    private final ExamService examService;
    
    @Autowired
    public ExamResultController(ExamResultService examResultService, ExamService examService) {
        this.examResultService = examResultService;
        this.examService = examService;
    }
    
    @GetMapping
    public String list(Model model) {
        List<ExamResultDto> examResults = examResultService.findAll();
        model.addAttribute("examResults", examResults);
        model.addAttribute("activePage", "exam-results");
        model.addAttribute("title", "試験結果一覧");
        return "exam-results/list";
    }
    
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        ExamResultDto examResult = examResultService.findById(id);
        model.addAttribute("examResult", examResult);
        return "exam-results/detail";
    }
    
    @GetMapping("/new")
    public String newForm(Model model) {
        ExamResultForm form = new ExamResultForm();
        form.setTakenAt(LocalDate.now());
        model.addAttribute("examResultForm", form);
        model.addAttribute("exams", examService.findAll());
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
        redirectAttributes.addFlashAttribute("message", "試験結果を登録しました");
        return "redirect:/exam-results";
    }
    
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        ExamResultDto examResult = examResultService.findById(id);
        ExamResultForm form = new ExamResultForm(
                examResult.getExamId(),
                examResult.getScore(),
                examResult.getTakenAt()
        );
        model.addAttribute("examResultForm", form);
        model.addAttribute("examResultId", id);
        model.addAttribute("exams", examService.findAll());
        return "exam-results/edit";
    }
    
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                        @Valid @ModelAttribute ExamResultForm form,
                        BindingResult result,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("examResultId", id);
            model.addAttribute("exams", examService.findAll());
            return "exam-results/edit";
        }
        examResultService.update(id, form);
        redirectAttributes.addFlashAttribute("message", "試験結果を更新しました");
        return "redirect:/exam-results";
    }
    
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        examResultService.delete(id);
        redirectAttributes.addFlashAttribute("message", "試験結果を削除しました");
        return "redirect:/exam-results";
    }
}

