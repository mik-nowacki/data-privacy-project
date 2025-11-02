package pt.unl.fct.pds.proj1server.controller;

import pt.unl.fct.pds.proj1server.model.MedData;
import pt.unl.fct.pds.proj1server.model.MedDataDeidentified;
import pt.unl.fct.pds.proj1server.model.MedDataKAnonymous;
import pt.unl.fct.pds.proj1server.model.AvgRequest;
import pt.unl.fct.pds.proj1server.model.HistographRequest;
import pt.unl.fct.pds.proj1server.model.CountRequest;
import pt.unl.fct.pds.proj1server.model.CountResponse;
import pt.unl.fct.pds.proj1server.repository.MedDataRepository;
import pt.unl.fct.pds.proj1server.repository.MedDataDeidentifiedRepository;
import pt.unl.fct.pds.proj1server.repository.MedDataKAnonymousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pt.unl.fct.pds.proj1server.dp.BudgetManager;
import pt.unl.fct.pds.proj1server.dp.LaplaceNoise;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/meddata")
public class MedDataController {

    @Value("${dp.epsilon}")
    private double epsilon;

    @Value("${dp.minValue.age}")
    private int minAge;

    @Value("${dp.maxValue.age}")
    private int maxAge;

    @Autowired
    private MedDataRepository medDataRepository;

    @GetMapping
    public List<MedData> getAllMedDatas() {
        return medDataRepository.findAll();
    }

    @GetMapping("/{id}")
    public MedData getMedDataById(@PathVariable Long id) {
        return medDataRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "MedData not found with id: " + id));
    }

    @Autowired
    private BudgetManager budgetManager;

    @PostMapping("/histograph")
    public CountResponse getMedDataHistograph(@RequestBody HistographRequest histographRequest) {
        List<MedData> allData = medDataRepository.findAll();

        String diagnosis = histographRequest.getValue();
        List<MedData> filteredData = allData.stream()
                .filter(m -> m.getDiagnosis().equalsIgnoreCase(diagnosis))
                .toList();

        double total = sumAges(filteredData);
        double count = filteredData.size();
        double countSensitivity = 1;
        double noisyCount = LaplaceNoise.addNoise(count, countSensitivity, epsilon / 2);
        double sumSensitivity = maxAge - minAge;
        double noisySum = LaplaceNoise.addNoise(total, sumSensitivity, epsilon / 2);

        budgetManager.reduce(epsilon);
        double val = 0;

        if (noisyCount != 0)
            val = noisySum / noisyCount;

        budgetManager.reduce(epsilon);
        double remaining = budgetManager.getRemainingBudget();
        double sensitivity = sumSensitivity / count;
        return new CountResponse("age", val, remaining, sensitivity);
    }

    @PostMapping("/count")
    public CountResponse getMedDataCount(@RequestBody CountRequest countRequest) {
        long count = getCount(countRequest);
        double sensitivity = 1;
        double val = LaplaceNoise.addNoise(count, sensitivity, epsilon);
        budgetManager.reduce(epsilon);
        double remaining = budgetManager.getRemainingBudget();
        return new CountResponse(countRequest.getAttribute(), val, remaining, sensitivity);
    }

    @PostMapping("/avg")
    public CountResponse getMedDataAvg(@RequestBody AvgRequest avgRequest) {
        if (!avgRequest.getAttribute().equalsIgnoreCase("age")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid attribute");
        }
        // age is only numerical column
        long count = medDataRepository.count();

        double countSensitivity = 1;
        double noisyCount = LaplaceNoise.addNoise(count, countSensitivity, epsilon / 2);
        budgetManager.reduce(epsilon);

        List<MedData> allData = medDataRepository.findAll();
        int total = sumAges(allData);
        double sumSensitivity = maxAge - minAge;
        double noisySum = LaplaceNoise.addNoise(total, sumSensitivity, epsilon / 2);
        double remaining = budgetManager.getRemainingBudget();
        double val = noisySum / noisyCount;
        double sensitivity = sumSensitivity / count;
        return new CountResponse(avgRequest.getAttribute(), val, remaining, sensitivity);
    }

    @Autowired
    private MedDataDeidentifiedRepository medDataDeidentifiedRepository;

    @GetMapping("/deidentified")
    public List<MedDataDeidentified> getAllMedDatasDeidentified() {
        return medDataDeidentifiedRepository.findAll();
    }

    @Autowired
    private MedDataKAnonymousRepository medDataKAnonymousRepository;

    @GetMapping("/k_anonymous")
    public List<MedDataKAnonymous> getAllMedDatasKAnonymous() {
        return medDataKAnonymousRepository.findAll();
    }

    private long getCount(CountRequest countRequest) {
        long count;
        switch (countRequest.getAttribute().toLowerCase()) {
            case "name":
                count = medDataRepository.countByName(countRequest.getValue());
                break;
            case "age":
                try {
                    count = medDataRepository.countByAge(Integer.parseInt(countRequest.getValue()));
                } catch (NumberFormatException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Age must be an integer");
                }
                break;
            case "address":
                count = medDataRepository.countByAddress(countRequest.getValue());
                break;
            case "email":
                count = medDataRepository.countByEmail(countRequest.getValue());
                break;
            case "gender":
                count = medDataRepository.countByGender(countRequest.getValue());
                break;
            case "postalcode":
                count = medDataRepository.countByPostalCode(countRequest.getValue());
                break;
            case "diagnosis":
                count = medDataRepository.countByDiagnosis(countRequest.getValue());
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid attribute");
        }
        return count;
    }

    public int sumAges(List<MedData> allData) {
        int sum = 0;
        for (MedData m : allData) {
            sum += m.getAge();
        }
        return sum;
    }
}
