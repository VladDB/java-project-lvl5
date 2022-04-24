package hexlet.code.controller;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.exeptions.NotFoundException;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statuses")
public class TaskStatusController {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskStatusService taskStatusService;

    @Operation(description = "Show list of task statuses")
    @ApiResponse(responseCode = "200", description = "List of task statuses")
    @GetMapping
    public List<TaskStatus> getAllTaskStatuses() {
        return taskStatusRepository.findAll();
    }

    @Operation(description = "Show task status by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task status is found"),
            @ApiResponse(responseCode = "404", description = "Task status with that id does not found")
    })
    @GetMapping("/{id}")
    public TaskStatus getTaskStatus(
            @Parameter(description = "Task status's ID")
            @PathVariable long id) {
        return taskStatusRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Task status does not exist with ID = " + id));
    }

    @Operation(description = "Create new task status")
    @ApiResponse(responseCode = "201", description = "Task status is created")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatus createNewTaskStatus(
            @Parameter(description = "Task status's data to save")
            @RequestBody TaskStatusDto taskStatusDto) {
        return taskStatusService.createTaskStatus(taskStatusDto);
    }

    @Operation(description = "Update task status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task status is updated"),
            @ApiResponse(responseCode = "404", description = "Task status with that id does not found")
    })
    @PutMapping("/{id}")
    public TaskStatus updateTaskStatus(
            @Parameter(description = "Task status's ID")
            @PathVariable long id,
            @Parameter(description = "Task status's data for update")
            @RequestBody TaskStatusDto taskStatusDto) {
        return taskStatusService.updateTaskStatus(id, taskStatusDto);
    }

    @Operation(description = "Delete task status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task status is deleted"),
            @ApiResponse(responseCode = "404", description = "Task status with that id does not found")
    })
    @DeleteMapping("/{id}")
    public void deleteTaskStatus(
            @Parameter(description = "Task status's ID")
            @PathVariable long id) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
                        .orElseThrow(() ->
                                new NotFoundException("Task status does not exist with ID = " + id));
        taskStatusRepository.delete(taskStatus);
    }
}
