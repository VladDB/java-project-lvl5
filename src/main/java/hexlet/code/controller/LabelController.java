package hexlet.code.controller;

import hexlet.code.dto.LabelDto;
import hexlet.code.exeptions.UserNotFoundException;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
public class LabelController {

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelService labelService;

    @GetMapping
    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    @GetMapping("/{id}")
    public Label getLabel(@PathVariable long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Label does not exist"));
    }

    @PostMapping
    public Label createLabel(@RequestBody LabelDto labelDto) {
        return labelService.createNewLabel(labelDto);
    }

    @PutMapping("/{id}")
    public Label updateLabel(@PathVariable long id,
                             @RequestBody LabelDto labelDto) {
        return labelService.updateLabel(id, labelDto);
    }

    @DeleteMapping("/{id}")
    public void deleteLabel(@PathVariable long id) {
        labelRepository.delete(
                labelRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException("Label does not exist"))
        );
    }
}
