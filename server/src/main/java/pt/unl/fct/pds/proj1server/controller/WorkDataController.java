package pt.unl.fct.pds.proj1server.controller;

import pt.unl.fct.pds.proj1server.model.WorkData;
import pt.unl.fct.pds.proj1server.model.CountRequest;
import pt.unl.fct.pds.proj1server.model.CountResponse;
import pt.unl.fct.pds.proj1server.repository.WorkDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import pt.unl.fct.pds.proj1server.dp.BudgetManager;
import pt.unl.fct.pds.proj1server.dp.LaplaceNoise;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/workdata")
public class WorkDataController {

    @Autowired
    private WorkDataRepository workDataRepository;

    @GetMapping
    public List<WorkData> getAllWorkDatas() {
        return workDataRepository.findAll();
    }

    @GetMapping("/{id}")
    public WorkData getWorkDataById(@PathVariable Long id) {
        return workDataRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "WorkData not found with id: " + id));
    }

    @Autowired
    private BudgetManager budgetManager;

    @Value("${dp.epsilon}")
    private double epsilon;

    @PostMapping("/count")
    public CountResponse getWorkDataCount(@RequestBody CountRequest countRequest) {
        long count = getCount(countRequest);
        double sensitivity = 1;
        double noisyCount = LaplaceNoise.addNoise(count, sensitivity, epsilon);
        budgetManager.reduce(epsilon);
        double remaining = budgetManager.getRemainingBudget();
        return new CountResponse(countRequest.getAttribute(), noisyCount, remaining, sensitivity);
    }

    private long getCount(CountRequest countRequest) {
        long count;
        switch (countRequest.getAttribute().toLowerCase()) {
            case "postalcode":
                count = workDataRepository.countByPostalCode(countRequest.getValue());
                break;
            case "gender":
                count = workDataRepository.countByGender(countRequest.getValue());
                break;
            case "education":
                count = workDataRepository.countByEducation(countRequest.getValue());
                break;
            case "workplace":
                count = workDataRepository.countByWorkplace(countRequest.getValue());
                break;
            case "department":
                count = workDataRepository.countByDepartment(countRequest.getValue());
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid attribute");
        }
        return count;
    }
}
