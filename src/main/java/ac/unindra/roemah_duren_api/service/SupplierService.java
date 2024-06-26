package ac.unindra.roemah_duren_api.service;

import ac.unindra.roemah_duren_api.dto.request.PagingRequest;
import ac.unindra.roemah_duren_api.dto.request.SupplierRequest;
import ac.unindra.roemah_duren_api.dto.response.SupplierResponse;
import ac.unindra.roemah_duren_api.entity.Supplier;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SupplierService {
    SupplierResponse create(SupplierRequest request);
    SupplierResponse getOne(String id);
    Supplier getById(String id);
    Page<SupplierResponse> getPage(PagingRequest request);
    List<SupplierResponse> getAll();
    SupplierResponse update(SupplierRequest request);
    void delete(String id);
}
