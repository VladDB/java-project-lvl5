package hexlet.code.service;

import hexlet.code.dto.TaskDto;
import hexlet.code.exeptions.UserNotFoundException;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private LabelRepository labelRepository;

    @Override
    public Task createNewTask(TaskDto taskDto) {
        Task task = new Task();

        User author = userService.getCurrentUser();
        User executor = userRepository.findById(taskDto.getExecutorId())
                .orElse(null);
        TaskStatus taskStatus = taskStatusRepository.findById(taskDto.getTaskStatusId())
                .orElseThrow(() -> new UserNotFoundException("Task status does not exist"));

        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setAuthor(author);
        task.setExecutor(executor);
        task.setTaskStatus(taskStatus);
        task.setLabels(labelRepository.findAllById(taskDto.getLabelIds()));

        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(long id, TaskDto taskDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Task does not exist"));

        User executor = userRepository.findById(taskDto.getExecutorId())
                .orElse(null);
        TaskStatus taskStatus = taskStatusRepository.findById(taskDto.getTaskStatusId())
                .orElseThrow(() -> new UserNotFoundException("Task status does not exist"));

        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setExecutor(executor);
        task.setTaskStatus(taskStatus);
        task.setLabels(labelRepository.findAllById(taskDto.getLabelIds()));

        return taskRepository.save(task);
    }
}
