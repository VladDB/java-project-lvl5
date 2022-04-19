package hexlet.code.controller;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDto;
import hexlet.code.exeptions.UserNotFoundException;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
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

    @GetMapping
    public Iterable<Task> getAllTasks(@QuerydslPredicate(root = Task.class) Predicate predicate) {
        return taskRepository.findAll(predicate);
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Task does not exist"));
    }

    @PostMapping
    public Task createTask(@RequestBody TaskDto taskDto) {
        return taskService.createNewTask(taskDto);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable long id, @RequestBody TaskDto taskDto) {
        return taskService.updateTask(id, taskDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable long id) {
        taskRepository.delete(taskRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Task does not exist")));
    }
}
