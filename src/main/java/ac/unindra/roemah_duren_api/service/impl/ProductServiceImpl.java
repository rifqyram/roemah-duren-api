package ac.unindra.roemah_duren_api.service.impl;

import ac.unindra.roemah_duren_api.constant.ResponseMessage;
import ac.unindra.roemah_duren_api.dto.request.PagingRequest;
import ac.unindra.roemah_duren_api.dto.request.ProductRequest;
import ac.unindra.roemah_duren_api.dto.response.ProductResponse;
import ac.unindra.roemah_duren_api.dto.response.SupplierResponse;
import ac.unindra.roemah_duren_api.entity.Product;
import ac.unindra.roemah_duren_api.entity.Supplier;
import ac.unindra.roemah_duren_api.repository.ProductRepository;
import ac.unindra.roemah_duren_api.service.ProductService;
import ac.unindra.roemah_duren_api.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final SupplierService supplierService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductResponse create(ProductRequest request) {
        log.info("Start create : {}", System.currentTimeMillis());
        Supplier supplier = supplierService.getById(request.getSupplierId());
        Product product = Product.builder()
                .code(request.getCode())
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .supplier(supplier)
                .build();
        productRepository.saveAndFlush(product);
        log.info("End create : {}", System.currentTimeMillis());
        return toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getOne(String id) {
        log.info("Start getOne : {}", System.currentTimeMillis());
        Product product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
        log.info("End getOne : {}", System.currentTimeMillis());
        return toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Product getById(String id) {
        log.info("Start getById : {}", System.currentTimeMillis());
        Product product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
        log.info("End getById : {}", System.currentTimeMillis());
        return product;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getPage(PagingRequest request) {
        log.info("Start getPage : {}", System.currentTimeMillis());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Specification<Product> specification = ProductRepository.hasSearchQuery(request.getQuery());
        Page<Product> products = productRepository.findAll(specification, pageable);
        log.info("End getPage : {}", System.currentTimeMillis());
        return products.map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductResponse update(ProductRequest request) {
        log.info("Start update : {}", System.currentTimeMillis());
        getById(request.getId());
        Supplier supplier = supplierService.getById(request.getSupplierId());
        Product product = Product.builder()
                .id(request.getId())
                .code(request.getCode())
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .supplier(supplier)
                .build();
        productRepository.saveAndFlush(product);
        log.info("End update : {}", System.currentTimeMillis());
        return toResponse(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        log.info("Start delete : {}", System.currentTimeMillis());
        Product product = getById(id);
        productRepository.delete(product);
        log.info("End delete : {}", System.currentTimeMillis());
    }

    private ProductResponse toResponse(Product product) {
        SupplierResponse supplierResponse = SupplierResponse.builder()
                .id(product.getSupplier().getId())
                .name(product.getSupplier().getName())
                .address(product.getSupplier().getAddress())
                .email(product.getSupplier().getEmail())
                .mobilePhoneNo(product.getSupplier().getMobilePhoneNo())
                .build();
        return ProductResponse.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .supplier(supplierResponse)
                .build();
    }
}
