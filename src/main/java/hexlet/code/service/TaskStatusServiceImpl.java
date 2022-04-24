package hexlet.code.service;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.exeptions.NotFoundException;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Override
    public TaskStatus createTaskStatus(TaskStatusDto taskStatusDto) {
        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setName(taskStatusDto.getName());
        return taskStatusRepository.save(taskStatus);
    }

    @Override
    public TaskStatus updateTaskStatus(long id, TaskStatusDto taskStatusDto) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task status does not exist with ID = " + id));

        taskStatus.setName(taskStatusDto.getName());

        return taskStatusRepository.save(taskStatus);
    }
}
