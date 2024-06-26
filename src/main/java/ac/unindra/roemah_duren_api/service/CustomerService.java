package ac.unindra.roemah_duren_api.service;

import ac.unindra.roemah_duren_api.dto.request.CustomerRequest;
import ac.unindra.roemah_duren_api.dto.request.PagingRequest;
import ac.unindra.roemah_duren_api.dto.response.CustomerResponse;
import ac.unindra.roemah_duren_api.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {
    CustomerResponse create(CustomerRequest request);
    CustomerResponse getOne(String id);
    Customer getById(String id);
    Page<CustomerResponse> getPagination(PagingRequest request);
    List<CustomerResponse> getAll();
    CustomerResponse update(CustomerRequest request);

    void deleteById(String id);
}
