package com.example.learning_exam_manager.controller;

import com.example.learning_exam_manager.dto.StudyItemDto;
import com.example.learning_exam_manager.form.StudyItemForm;
import com.example.learning_exam_manager.service.StudyItemService;
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
@RequestMapping("/study-items")
public class StudyItemController {
    
    private final StudyItemService studyItemService;
    private final SubjectService subjectService;
    
    @Autowired
    public StudyItemController(StudyItemService studyItemService, SubjectService subjectService) {
        this.studyItemService = studyItemService;
        this.subjectService = subjectService;
    }
    
    @GetMapping
    public String list(Model model) {
        List<StudyItemDto> studyItems = studyItemService.findAll();
        model.addAttribute("studyItems", studyItems);
        model.addAttribute("activePage", "study-items");
        model.addAttribute("title", "学習項目一覧");
        return "study-items/list";
    }
    
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        StudyItemDto studyItem = studyItemService.findById(id);
        model.addAttribute("studyItem", studyItem);
        return "study-items/detail";
    }
    
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("studyItemForm", new StudyItemForm());
        model.addAttribute("subjects", subjectService.findAll());
        return "study-items/new";
    }
    
    @PostMapping
    public String create(@Valid @ModelAttribute StudyItemForm form, 
                        BindingResult result,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("subjects", subjectService.findAll());
            return "study-items/new";
        }
        studyItemService.create(form);
        redirectAttributes.addFlashAttribute("message", "学習項目を登録しました");
        return "redirect:/study-items";
    }
    
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        StudyItemDto studyItem = studyItemService.findById(id);
        StudyItemForm form = new StudyItemForm(
                studyItem.getSubjectId(),
                studyItem.getTitle(),
                studyItem.getStatus(),
                studyItem.getStudyTime()
        );
        model.addAttribute("studyItemForm", form);
        model.addAttribute("studyItemId", id);
        model.addAttribute("subjects", subjectService.findAll());
        return "study-items/edit";
    }
    
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                        @Valid @ModelAttribute StudyItemForm form,
                        BindingResult result,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("studyItemId", id);
            model.addAttribute("subjects", subjectService.findAll());
            return "study-items/edit";
        }
        studyItemService.update(id, form);
        redirectAttributes.addFlashAttribute("message", "学習項目を更新しました");
        return "redirect:/study-items";
    }
    
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        studyItemService.delete(id);
        redirectAttributes.addFlashAttribute("message", "学習項目を削除しました");
        return "redirect:/study-items";
    }
}

