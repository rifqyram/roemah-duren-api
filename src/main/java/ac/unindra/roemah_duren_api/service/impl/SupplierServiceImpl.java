package ac.unindra.roemah_duren_api.service.impl;

import ac.unindra.roemah_duren_api.constant.ResponseMessage;
import ac.unindra.roemah_duren_api.dto.request.PagingRequest;
import ac.unindra.roemah_duren_api.dto.request.SupplierRequest;
import ac.unindra.roemah_duren_api.dto.response.SupplierResponse;
import ac.unindra.roemah_duren_api.entity.Supplier;
import ac.unindra.roemah_duren_api.repository.SupplierRepository;
import ac.unindra.roemah_duren_api.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;

    @Override
    public SupplierResponse create(SupplierRequest request) {
        log.info("Start create supplier {}", System.currentTimeMillis());
        Supplier supplier = Supplier.builder()
                .name(request.getName())
                .address(request.getAddress())
                .email(request.getEmail())
                .mobilePhoneNo(request.getMobilePhoneNo())
                .build();
        supplierRepository.saveAndFlush(supplier);
        log.info("End create supplier {}", System.currentTimeMillis());
        return toResponse(supplier);
    }

    @Override
    public SupplierResponse getOne(String id) {
        log.info("Start getOne supplier {}", System.currentTimeMillis());
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, ResponseMessage.SUCCESS_GET_DATA));
        log.info("End getOne supplier {}", System.currentTimeMillis());
        return toResponse(supplier);
    }

    @Override
    public Supplier getById(String id) {
        log.info("Start getById supplier {}", System.currentTimeMillis());
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, ResponseMessage.SUCCESS_GET_DATA));
        log.info("End getById supplier {}", System.currentTimeMillis());
        return supplier;
    }

    @Override
    public Page<SupplierResponse> getPage(PagingRequest request) {
        log.info("Start getPage supplier {}", System.currentTimeMillis());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Specification<Supplier> specification = SupplierRepository.hasSearchQuery(request.getQuery());
        log.info("End getPage supplier {}", System.currentTimeMillis());
        return supplierRepository.findAll(specification, pageable).map(this::toResponse);
    }

    @Override
    public List<SupplierResponse> getAll() {
        log.info("Start getAll supplier {}", System.currentTimeMillis());
        List<SupplierResponse> responses = supplierRepository.findAll().stream().map(this::toResponse).toList();
        log.info("End getAll supplier {}", System.currentTimeMillis());
        return responses;
    }

    @Override
    public SupplierResponse update(SupplierRequest request) {
        Supplier supplier = getById(request.getId());
        supplier.setName(request.getName());
        supplier.setAddress(request.getAddress());
        supplier.setEmail(request.getEmail());
        supplier.setMobilePhoneNo(request.getMobilePhoneNo());
        return toResponse(supplierRepository.saveAndFlush(supplier));
    }

    @Override
    public void delete(String id) {
        Supplier supplier = getById(id);
        supplierRepository.delete(supplier);
    }

    private SupplierResponse toResponse(Supplier supplier) {
        return SupplierResponse.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .address(supplier.getAddress())
                .email(supplier.getEmail())
                .mobilePhoneNo("0" + supplier.getMobilePhoneNo())
                .build();
    }
}
