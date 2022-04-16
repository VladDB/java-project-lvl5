package hexlet.code.controller;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.exeptions.UserNotFoundException;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statuses")
public class TaskStatusController {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskStatusService taskStatusService;

    @GetMapping
    public List<TaskStatus> getAllTaskStatuses() {
        return taskStatusRepository.findAll();
    }

    @GetMapping("/{id}")
    public TaskStatus getTaskStatus(@PathVariable long id) {
        return taskStatusRepository.getById(id);
    }

    @PostMapping
    public TaskStatus createNewTaskStatus(@RequestBody TaskStatusDto taskStatusDto) {
        return taskStatusService.createTaskStatus(taskStatusDto);
    }

    @PutMapping("/{id}")
    public TaskStatus updateTaskStatus(@PathVariable long id,
                                       @RequestBody TaskStatusDto taskStatusDto) {
        return taskStatusService.updateTaskStatus(id, taskStatusDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTaskStatus(@PathVariable long id) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
                        .orElseThrow(() ->
                                new UserNotFoundException("Task status does not exist with ID = " + id));
        taskStatusRepository.delete(taskStatus);
    }
}
