package com.example.notesapp.controller;

import com.example.notesapp.model.Note;
import com.example.notesapp.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/")
    public String index(@RequestParam(value = "q", required = false) String query, Model model) {
        model.addAttribute("notes", noteService.findAll(query));
        model.addAttribute("query", query == null ? "" : query);
        model.addAttribute("noteForm", new Note());
        return "index";
    }

    @PostMapping("/notes")
    public String create(@Valid @ModelAttribute("noteForm") Note noteForm,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes,
                         @RequestParam(value = "q", required = false) String query) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("notes", noteService.findAll(query));
            model.addAttribute("query", query == null ? "" : query);
            return "index";
        }
        noteService.create(noteForm);
        redirectAttributes.addFlashAttribute("message", "Note created successfully");
        return "redirect:/";
    }

    @GetMapping("/notes/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("noteForm", noteService.getById(id));
        return "edit";
    }

    @PostMapping("/notes/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("noteForm") Note noteForm,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("noteForm", noteForm);
            return "edit";
        }
        noteService.update(id, noteForm);
        redirectAttributes.addFlashAttribute("message", "Note updated successfully");
        return "redirect:/";
    }

    @PostMapping("/notes/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        noteService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Note deleted successfully");
        return "redirect:/";
    }
}
