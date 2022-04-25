package hexlet.code.controller;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/labels")
public class LabelController {

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelService labelService;

    @Operation(description = "Show list of labels")
    @ApiResponse(responseCode = "200", description = "List of labels")
    @GetMapping
    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    @Operation(description = "Show label by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label is found"),
            @ApiResponse(responseCode = "404", description = "Label with that id does not found")
    })
    @GetMapping("/{id}")
    public Label getLabel(
            @Parameter(description = "Label's ID")
            @PathVariable long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Label does not exist"));
    }

    @Operation(description = "Create new label")
    @ApiResponse(responseCode = "201", description = "Label is created")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Label createLabel(
            @Parameter(description = "Label's data to save")
            @RequestBody LabelDto labelDto) {
        return labelService.createNewLabel(labelDto);
    }

    @Operation(description = "Update label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label is updated"),
            @ApiResponse(responseCode = "404", description = "Label with that id does not found")
    })
    @PutMapping("/{id}")
    public Label updateLabel(
            @Parameter(description = "Label's ID")
            @PathVariable long id,
            @Parameter(description = "Label's data for update")
            @RequestBody LabelDto labelDto) {
        return labelService.updateLabel(id, labelDto);
    }

    @Operation(description = "Delete label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label is deleted"),
            @ApiResponse(responseCode = "404", description = "Label with that id does not found")
    })
    @DeleteMapping("/{id}")
    public void deleteLabel(
            @Parameter(description = "Label's ID")
            @PathVariable long id) {
        labelRepository.delete(
                labelRepository.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("Label does not exist"))
        );
    }
}
