package fi.seamk.harjoitustyo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/events")
    public String getAllEvents(Model model) {
        List<Event> events = eventRepository.findAll();
        model.addAttribute("events", events);
        return "list";
    }

    @GetMapping("/categories")
    public String getAllCategories(Model model) {
    List<Category> categories = categoryRepository.findAll();
    model.addAttribute("categories", categories);
    return "categories"; // Renders categories.html
}

    @GetMapping("/events/{id}")
    public String getEventById(@PathVariable Long id, Model model) {
        Event event = eventRepository.findById(id).orElse(null); // Use findById instead of getReferenceById
        System.out.println(event);
        model.addAttribute("event", event);
        System.out.println(event);
        return "view";
    }

    @GetMapping("/events/new")
    public String showEventForm(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("categories", categoryRepository.findAll());
        return "form";
    }
    @Transactional
    @PostMapping("/events/new")
    public String saveEvent(@ModelAttribute Event event) {
        if (event.getCategory() != null && event.getCategory().getId() != null) {
            Category category = categoryRepository.findById(event.getCategory().getId()).orElse(null);
            event.setCategory(category);
            }
        System.out.println(event);
        Event saved = eventRepository.save(event);
        System.out.println(saved);
        return "redirect:/events";
        }
}
