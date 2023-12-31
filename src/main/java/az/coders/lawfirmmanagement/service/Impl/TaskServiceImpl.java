package az.coders.lawfirmmanagement.service.Impl;

import az.coders.lawfirmmanagement.dto.TaskDto;
import az.coders.lawfirmmanagement.exception.CaseNotFoundException;
import az.coders.lawfirmmanagement.exception.TaskNotFound;
import az.coders.lawfirmmanagement.model.Case;
import az.coders.lawfirmmanagement.model.Task;
import az.coders.lawfirmmanagement.repository.CaseRepository;
import az.coders.lawfirmmanagement.repository.TaskRepository;
import az.coders.lawfirmmanagement.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final CaseRepository caseRepository;

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFound("Task not found"));
    }

    @Override
    public String saveTask(TaskDto taskDto) {

        Case aCase = caseRepository.findById(taskDto.getCaseId()).orElseThrow(CaseNotFoundException::new);
        Task task = Task.builder()
                .name(taskDto.getName())
                .description(taskDto.getDescription())
                .priority(taskDto.getPriority())
                .remindDate(taskDto.getRemindDate())
                .aCase(aCase)
                .build();
        taskRepository.save(task);
        return "Saved successfully";
    }

    @Override
    public String deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFound("Task not found with this id:" + taskId));
        taskRepository.delete(task);
        return "Deleted successfully";
    }

    @Override
    public String editTask(TaskDto task) {
        Task task1 = taskRepository.findById(task.getId()).orElseThrow(() -> new TaskNotFound("Task not found"));
Case aCase = caseRepository.findById(task.getCaseId()).orElseThrow(CaseNotFoundException::new);

        if (task.getName() != null) {
            task1.setName(task.getName());
        }
        if (task.getDescription() != null) {
            task1.setDescription(task.getDescription());
        }
        if (task.getPriority() != null) {
            task1.setPriority(task.getPriority());
        }
        if (task.getCaseId() != null) {
            task1.setACase(aCase);
        }
        taskRepository.save(task1);

        return "Edited successfully";

    }
}
