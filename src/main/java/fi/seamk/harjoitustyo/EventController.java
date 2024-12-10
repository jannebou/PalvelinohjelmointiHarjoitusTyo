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


@Controller
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    
    @GetMapping("/")
    public String index(Model model) {
        List<Event> events = eventRepository.findAll();
        int eventCount = events.size();
        model.addAttribute("eventCount", eventCount);
        return "index";
    }

    @GetMapping("/events")
    public String getAllEvents(Model model) {
        List<Event> events = eventRepository.findAll();
        model.addAttribute("events", events);
        return "events";
    }

    @GetMapping("/categories")
    public String getAllCategories(Model model) {
    List<Category> categories = categoryRepository.findAll();
    model.addAttribute("categories", categories);
    return "categories";
    }

    @GetMapping("/events/{id}")
    public String getEventById(@PathVariable Long id, Model model) {
        Event event = eventRepository.findById(id).orElse(null);
        model.addAttribute("event", event);
        return "eventById";
    }

    @GetMapping("/events/edit/{id}")
    public String getEventEdit(@PathVariable Long id, Model model) {
        Event event = eventRepository.findById(id).orElse(null);
        List<Category> category = categoryRepository.findAll();
        model.addAttribute("event", event);
        model.addAttribute("categories", category);
        System.out.println(event);
        return "eventEdit";
    }
    
    @GetMapping("/categories/new")
    public String showCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "newCategory";
    }

    @GetMapping("/events/new")
    public String showEventForm(Model model) {
        if (categoryRepository.findAll().isEmpty()) {
            return "redirect:/categories/new";
        }
        model.addAttribute("event", new Event());
        model.addAttribute("categories", categoryRepository.findAll());
        return "newEvent";
    }

    //#region DELETE
    @Transactional
    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id, Model model) {
        categoryRepository.deleteById(id);
 
        List<Event> events = eventRepository.findAll();
        for (Event event : events) {
            if (event.getCategory().getId() == id) {
                eventRepository.deleteById(event.getId());
                System.out.println(event);
            }
        }
        return "redirect:/categories";
    }
    @Transactional
    @GetMapping("/events/delete/{id}")
    public String deleteEvent(@PathVariable Long id, Model model) {
        eventRepository.deleteById(id);
        return "redirect:/events";
    }
    //#endregion

    //#region POST
    @Transactional
    @PostMapping("/categories/new")
    public String saveCategory(@ModelAttribute Category category) {
        categoryRepository.save(category);
        return "redirect:/categories";
    }
    @Transactional
    @PostMapping("/events/edit/{id}")
    public String saveEventEdit(@ModelAttribute Event event) {
        Event eventToUpdate = eventRepository.findById(event.getId()).orElse(null);
        if (eventToUpdate != null) {
            eventToUpdate.setTitle(event.getTitle());
            eventToUpdate.setDescription(event.getDescription());    
            eventToUpdate.setEventDateTime(event.getEventDateTime());
            eventToUpdate.setCategory(event.getCategory());
            eventRepository.save(eventToUpdate);
        }
        return "redirect:/events";
    }


    @Transactional
    @PostMapping("/events/new")
    public String saveEvent(@ModelAttribute Event event) {
        if (event.getCategory() != null && event.getCategory().getId() != null) {
            Category category = categoryRepository.findById(event.getCategory().getId()).orElse(null);
            event.setCategory(category);
            }
        eventRepository.save(event);
        return "redirect:/events";
        }
    //#endregion
}
