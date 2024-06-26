package ac.unindra.roemah_duren_api.service.impl;

import ac.unindra.roemah_duren_api.constant.ResponseMessage;
import ac.unindra.roemah_duren_api.dto.request.CustomerRequest;
import ac.unindra.roemah_duren_api.dto.request.PagingRequest;
import ac.unindra.roemah_duren_api.dto.response.CustomerResponse;
import ac.unindra.roemah_duren_api.entity.Customer;
import ac.unindra.roemah_duren_api.repository.CustomerRepository;
import ac.unindra.roemah_duren_api.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerResponse create(CustomerRequest request) {
        log.info("Start create customer {}", System.currentTimeMillis());
        Customer customer = Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .address(request.getAddress())
                .mobilePhoneNo(request.getMobilePhoneNo())
                .build();
        customerRepository.saveAndFlush(customer);
        log.info("End create customer {}", System.currentTimeMillis());
        return toResponse(customer);
    }

    @Override
    public CustomerResponse getOne(String id) {
        log.info("Start getOne customer {}", System.currentTimeMillis());
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.SUCCESS_SAVE_DATA));
        log.info("End getOne customer {}", System.currentTimeMillis());
        return toResponse(customer);
    }

    @Override
    public Customer getById(String id) {
        log.info("Start getById customer {}", System.currentTimeMillis());
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.SUCCESS_SAVE_DATA));
        log.info("End getById customer {}", System.currentTimeMillis());
        return customer;
    }

    @Override
    public Page<CustomerResponse> getPagination(PagingRequest request) {
        log.info("Start getPagination customer {}", System.currentTimeMillis());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Specification<Customer> specification = CustomerRepository.hasSearchQuery(request.getQuery());
        log.info("End getPagination customer {}", System.currentTimeMillis());
        return customerRepository.findAll(specification, pageable).map(this::toResponse);
    }

    @Override
    public List<CustomerResponse> getAll() {
        log.info("Start getAll customer {}", System.currentTimeMillis());
        List<CustomerResponse> customerResponses = customerRepository.findAll().stream().map(this::toResponse).toList();
        log.info("End getAll customer {}", System.currentTimeMillis());
        return customerResponses;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerResponse update(CustomerRequest request) {
        log.info("Start update customer {}", System.currentTimeMillis());
        Customer customer = getById(request.getId());
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        customer.setMobilePhoneNo(request.getMobilePhoneNo());
        customerRepository.saveAndFlush(customer);
        log.info("End update customer {}", System.currentTimeMillis());
        return toResponse(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) {
        log.info("Start deleteById customer {}", System.currentTimeMillis());
        Customer customer = getById(id);
        customerRepository.deleteById(customer.getId());
        log.info("End deleteById customer {}", System.currentTimeMillis());
    }

    private CustomerResponse toResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .mobilePhoneNo("0" + customer.getMobilePhoneNo())
                .address(customer.getAddress())
                .build();
    }

}
