package dev.shahul.came_rest_dsl.service;

import dev.shahul.came_rest_dsl.model.RequestDTO;
import dev.shahul.came_rest_dsl.repository.MyRestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.data.couchbase.core.CouchbaseTemplate;

import java.util.Optional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ArrayList;

@Service
public class MyRestService {

    @Autowired
    private MyRestRepository repository;

    @Autowired
    private CouchbaseTemplate couchbaseTemplate;

    public RequestDTO getShipperById(String id) {
        RequestDTO result = new RequestDTO();
        try {
            Optional<RequestDTO> resultContainer = repository.findById(id);
            result = resultContainer.get();
            result.setReturnCode("000");
            result.setMessage("Shipper is found");
        } catch (NoSuchElementException e){
            result.setShipperNumber(id);
            result.setReturnCode("999");
            result.setMessage(e.getMessage());
        } catch (Exception e) {
            result.setShipperNumber(id);
            result.setReturnCode("500");
            result.setMessage("Internal Server Error: " + e.getMessage() + " from " + e.getClass().getSimpleName());
        }
        return result;
    }

    public List<RequestDTO> getAllShippers() {
        List<RequestDTO> results = new ArrayList<>();
        RequestDTO requestDTO = new RequestDTO();
        try {
            results = repository.findAll();
            if (results.isEmpty()) {
                requestDTO.setReturnCode("999");
                requestDTO.setMessage("No data found!");
                results.add(requestDTO);
            }
            return results;
        } catch (Exception e) {
            requestDTO.setReturnCode("500");
            requestDTO.setMessage("Internal Server Error: " + e.getMessage() + " from " + e.getClass().getSimpleName());
            results.add(requestDTO);
            return results;
        }
    }

    public RequestDTO createShipperEntry(RequestDTO request) {
        String shipperNumber = request.getShipperNumber();
        request.setDocumentId(shipperNumber);
        RequestDTO result = new RequestDTO();
        try {            
            result = couchbaseTemplate.insertById(RequestDTO.class).one(request);
            result.setReturnCode("000");
            result.setMessage("Created Successfully");
            return result;
        } catch(DuplicateKeyException e) {
            result.setShipperNumber(shipperNumber);
            result.setReturnCode("998");
            result.setMessage("Document with the given id already exists");
            return result;
        }catch (Exception e) {
            result.setShipperNumber(shipperNumber);
            result.setReturnCode("500");
            result.setMessage("Internal Server Error: " + e.getMessage() + " from " + e.getClass().getSimpleName());
            return result;
        }
    }

    public List<RequestDTO> deleteShipperBatch(List<String> ids) {
        List<RequestDTO> results = new ArrayList<>();
        for (String id : ids) {
            RequestDTO result = deleteShipperById(id);
            results.add(result);
        }
        return results;
    }
    
    public List<RequestDTO> createShipperBatch(List<RequestDTO> requests) {
        List<RequestDTO> results = new ArrayList<>();
        for (RequestDTO request : requests) {
            RequestDTO result = createShipperEntry(request);
            results.add(result);
        }
        return results;
    }

    public RequestDTO deleteShipperById(String id) {
        RequestDTO result = new RequestDTO();
        result = getShipperById(id);
        if (result.getReturnCode().equals("999"))
            return result;
        try {
            repository.deleteById(id);
            result.setShipperNumber(id);
            result.setReturnCode("000");
            result.setMessage("Deleted successfully");
            return result;
        } catch (Exception e) {
            result.setShipperNumber(id);
            result.setReturnCode("500");
            result.setMessage("Internal Server Error: " + e.getMessage() + " from " + e.getClass().getSimpleName());
            return result;
        }
    }
}
