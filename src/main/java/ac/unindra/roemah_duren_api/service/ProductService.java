package ac.unindra.roemah_duren_api.service;

import ac.unindra.roemah_duren_api.dto.request.PagingRequest;
import ac.unindra.roemah_duren_api.dto.request.ProductRequest;
import ac.unindra.roemah_duren_api.dto.response.ProductResponse;
import ac.unindra.roemah_duren_api.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    ProductResponse create(ProductRequest request);
    ProductResponse getOne(String id);
    Product getById(String id);
    Page<ProductResponse> getPage(PagingRequest request);
    List<ProductResponse> getAll();
    ProductResponse update(ProductRequest request);
    void delete(String id);
}
