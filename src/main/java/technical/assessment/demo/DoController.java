package technical.assessment.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DoController {

    @Autowired
    private DoService doService;

    @PostMapping(value="/addTask")
    public ResponseEntity<DoDto> addTask(@RequestBody DoDto doDto){
        return ResponseEntity.ok(doService.addTask(doDto));
    }

    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<DoDto> deleteTask(@PathVariable String taskId){
        return ResponseEntity.ok(doService.deleteTask(taskId));
    }
    @GetMapping("/getTasks")
    public ResponseEntity<DoDto> getAllTasks(){
        return ResponseEntity.ok(doService.getAllTasks());

    }
    @PutMapping("/update-status/{title}")
    public ResponseEntity<DoDto> updateCarStatus(@PathVariable String title, @RequestBody UpdateDto updateDto) {
        try {
            Status newStatus = Status.valueOf(updateDto.getStatus().toUpperCase());

            DoDto response = doService.updateTask(title, newStatus);

            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (IllegalArgumentException e) {
            DoDto errorResponse = new DoDto();
            errorResponse.setStatusCode(400);
            errorResponse.setMessage("Invalid status value. Allowed values: ACTIVE, LOST, FOUND.");
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            DoDto errorResponse = new DoDto();
            errorResponse.setStatusCode(500);
            errorResponse.setMessage("Error: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    @GetMapping("/getActiveTasks")
    public ResponseEntity<DoDto> searchTasksByStatus(String status) {
        status="ACTIVE";
        DoDto doDto = doService.searchTaskByStatus(status);

        if (doDto.getStatusCode() == 200) {
            return ResponseEntity.ok(doDto);
        } else if (doDto.getStatusCode() == 404) {
            return ResponseEntity.status(404).body(doDto);
        } else {
            return ResponseEntity.status(500).body(doDto);
        }
    }

    @GetMapping("/getCompletedTasks")
    public ResponseEntity<DoDto> searchCompletedTasksByStatus(String status) {
        status="COMPLETED";
        DoDto doDto = doService.searchTaskByStatus(status);

        if (doDto.getStatusCode() == 200) {
            return ResponseEntity.ok(doDto);
        } else if (doDto.getStatusCode() == 404) {
            return ResponseEntity.status(404).body(doDto);
        } else {
            return ResponseEntity.status(500).body(doDto);
        }
    }
}
