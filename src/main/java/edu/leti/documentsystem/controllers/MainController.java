package edu.leti.documentsystem.controllers;

import edu.leti.documentsystem.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    private DocumentService documentService;

    @GetMapping("/")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/search")
    public String searchDocuments(String query, Model model) {
        if (query == null || query.isEmpty()) {
            model.addAttribute("documents", documentService.findAllDocuments());
        } else {
            model.addAttribute("documents", documentService.findDocumentsByContent(query));
        }
        model.addAttribute("query", query);

        return "search";
    }
}
