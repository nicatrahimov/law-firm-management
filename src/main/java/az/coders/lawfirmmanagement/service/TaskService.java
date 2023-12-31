package az.coders.lawfirmmanagement.service;

import az.coders.lawfirmmanagement.dto.TaskDto;
import az.coders.lawfirmmanagement.model.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface TaskService {

    List<Task>getAllTasks();
    Task getTaskById(Long id);

    String saveTask(TaskDto taskDto);

    String deleteTask(Long taskId);

    String editTask(TaskDto task);
}
