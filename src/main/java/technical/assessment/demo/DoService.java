package technical.assessment.demo;


import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DoService {

    @Autowired
    private DoRepository doRepository;

    public DoDto addTask(DoDto addingTask){
        DoDto resp=new DoDto();

        try{
            Optional<DoList> existingTitle = doRepository.findByTitle(addingTask.getTitle());
            if (existingTitle.isPresent()) {
                resp.setStatusCode(400);
                resp.setMessage("Task title exists. Try a different one.");
                return resp;
            }
            DoList doTask=new DoList();
            doTask.setId(addingTask.getId());
            doTask.setTitle(addingTask.getTitle());
            doTask.setDescription(addingTask.getDescription());
            doTask.setCreatedTime(LocalDateTime.now());
            doTask.setDueDate(addingTask.getDueDate());
            doTask.setStatus(Optional.ofNullable(addingTask.getStatus()).map(Status::valueOf).orElse(Status.ACTIVE));

            DoList taskResult= doRepository.save(doTask);

            resp.setDoList(taskResult);
            resp.setStatusCode(200);
            resp.setMessage("Added the task Successfully");

        } catch (Exception e) {

            resp.setStatusCode(500);
            resp.setError(e.getMessage());

        }
        return resp;

    }
    public DoDto deleteTask(String taskId) {
        DoDto reqRes = new DoDto();
        try {
            Optional<DoList> taskOptional = doRepository.findById(taskId);
            if (taskOptional.isPresent()) {
                doRepository.deleteById(taskId);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Task deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("Task not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting the task: " + e.getMessage());
        }
        return reqRes;
    }

    public DoDto getAllTasks(){
        DoDto doDto=new DoDto();

        try{
            List<DoList> result=doRepository.findAll();
            if(!result.isEmpty()){
                doDto.setDoLists(result);
                doDto.setStatusCode(200);
                doDto.setMessage("Successful");
            }else {
                doDto.setStatusCode(404);
                doDto.setMessage("No tasks found");
            }
            return doDto;

        }catch (Exception e){

            doDto.setStatusCode(500);
            doDto.setMessage("Error Occurred"+e.getMessage());
            return doDto;
        }
    }

    public DoDto updateTask(String title,Status newStatus) {
        DoDto doDto = new DoDto();
        try {
            System.out.println("Attempting to update task with title: " + title);

            Optional<DoList> optionalUser = doRepository.findByTitle(title);

            if (optionalUser.isPresent()) {
                DoList task = optionalUser.get();

                System.out.println("Current status of task: " + task.getStatus());

                task.setStatus(newStatus);

                DoList updatedTask = doRepository.save(task);

                System.out.println("Updated status of task: " + updatedTask.getStatus());

                doDto.setDoList(updatedTask);
                doDto.setStatusCode(200);
                doDto.setMessage("Task Status updated successfully.");
            } else {
                doDto.setStatusCode(404);
                doDto.setError("Not Found");
                doDto.setMessage("Task with Title '" + title + "' not found");
                System.out.println("Task with Title '" + title + "' not found in the database.");
            }
        } catch (Exception e) {
            doDto.setStatusCode(500);
            doDto.setError("Internal Server Error");
            doDto.setMessage("Error updating task status: " + e.getMessage());
            System.out.println("Error occurred while updating task status: " + e.getMessage());
        }
        return doDto;
    }

    public DoDto searchTaskByStatus(String status){
        DoDto doDto=new DoDto();
        try {
            List<DoList> tasks = doRepository.findByStatus(status);

            if (tasks.isEmpty()) {
                throw new NoSuchElementException("No active tasks found.");
            }

            doDto.setDoLists(tasks);
            doDto.setStatusCode(200);
            doDto.setMessage("Tasks for status '" + status + "' found successfully");

        } catch (NoSuchElementException e) {
            doDto.setStatusCode(404);
            doDto.setError("Not Found");
            doDto.setMessage(e.getMessage());

        } catch (Exception e) {
            doDto.setStatusCode(500);
            doDto.setError("Internal Server Error");
            doDto.setMessage("An unexpected error occurred: " + e.getMessage());
        }
        System.out.println(doDto);
        return doDto;
    }
}
