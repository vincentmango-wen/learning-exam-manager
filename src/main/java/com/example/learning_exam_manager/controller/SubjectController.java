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

import java.util.List;

@Controller
@RequestMapping("/subjects")
public class SubjectController {
    
    private final SubjectService subjectService;
    
    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }
    
    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        List<SubjectDto> subjects;
        if (keyword != null && !keyword.trim().isEmpty()) {
            subjects = subjectService.searchByName(keyword);
            model.addAttribute("keyword", keyword);  // 検索キーワードを保持
        } else {
            subjects = subjectService.findAll();
        }
        model.addAttribute("subjects", subjects);
        model.addAttribute("activePage", "subjects");
        model.addAttribute("title", "科目一覧");
        return "subjects/list";
    }
    
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        SubjectDto subject = subjectService.findById(id);
        model.addAttribute("subject", subject);
        return "subjects/detail";
    }
    
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("subjectForm", new SubjectForm());
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
        redirectAttributes.addFlashAttribute("message", "科目を登録しました");
        return "redirect:/subjects";
    }
    
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        SubjectDto subject = subjectService.findById(id);
        SubjectForm form = new SubjectForm(subject.getName(), subject.getDescription());
        model.addAttribute("subjectForm", form);
        model.addAttribute("subjectId", id);
        return "subjects/edit";
    }
    
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                        @Valid @ModelAttribute SubjectForm form,
                        BindingResult result,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "subjects/edit";
        }
        subjectService.update(id, form);
        redirectAttributes.addFlashAttribute("message", "科目を更新しました");
        return "redirect:/subjects";
    }
    
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        subjectService.delete(id);
        redirectAttributes.addFlashAttribute("message", "科目を削除しました");
        return "redirect:/subjects";
    }
}