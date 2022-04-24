package hexlet.code.controller;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDto;
import hexlet.code.exeptions.UserNotFoundException;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@Transactional
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @Operation(description = "Show list of tasks")
    @ApiResponse(responseCode = "200", description = "List of tasks")
    @GetMapping
    public Iterable<Task> getAllTasks(@QuerydslPredicate(root = Task.class) Predicate predicate) {
        return taskRepository.findAll(predicate);
    }

    @Operation(description = "Show task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task is found"),
            @ApiResponse(responseCode = "404", description = "Task with that id does not found")
    })
    @GetMapping("/{id}")
    public Task getTask(
            @Parameter(description = "Task's ID")
            @PathVariable long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Task does not exist"));
    }

    @Operation(description = "Create new task")
    @ApiResponse(responseCode = "201", description = "Task is created")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(
            @Parameter(description = "Task's data to save")
            @RequestBody TaskDto taskDto) {
        return taskService.createNewTask(taskDto);
    }

    @Operation(description = "Update task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task is updated"),
            @ApiResponse(responseCode = "404", description = "Task with that id does not found")
    })
    @PutMapping("/{id}")
    public Task updateTask(
            @Parameter(description = "Task's ID")
            @PathVariable long id,
            @Parameter(description = "Task's data for update")
            @RequestBody TaskDto taskDto) {
        return taskService.updateTask(id, taskDto);
    }

    @Operation(description = "Delete task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task is deleted"),
            @ApiResponse(responseCode = "404", description = "Task with that id does not found")
    })
    @DeleteMapping("/{id}")
    public void deleteTask(
            @Parameter(description = "Task's ID")
            @PathVariable long id) {
        taskRepository.delete(taskRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Task does not exist")));
    }
}
