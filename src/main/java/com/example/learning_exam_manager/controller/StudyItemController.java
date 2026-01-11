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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Long subjectId,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {
        // Pageableの作成（ページ番号、サイズ、ソート順を指定）
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        
        Page<StudyItemDto> studyItemsPage = studyItemService.search(keyword, subjectId, pageable);
        
        model.addAttribute("studyItems", studyItemsPage.getContent());  // 現在のページのデータ
        model.addAttribute("currentPage", page);  // 現在のページ番号
        model.addAttribute("totalPages", studyItemsPage.getTotalPages());  // 総ページ数
        model.addAttribute("totalItems", studyItemsPage.getTotalElements());  // 総件数
        model.addAttribute("pageSize", size);  // ページサイズ
        
        // 検索条件を保持（検索フォーム用）
        if (keyword != null && !keyword.trim().isEmpty()) {
            model.addAttribute("keyword", keyword);
        }
        if (subjectId != null) {
            model.addAttribute("selectedSubjectId", subjectId);
        }
        
        model.addAttribute("subjects", subjectService.findAll());  // 科目選択用
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

