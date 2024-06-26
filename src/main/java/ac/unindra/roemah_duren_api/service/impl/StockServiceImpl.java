package ac.unindra.roemah_duren_api.service.impl;

import ac.unindra.roemah_duren_api.constant.ResponseMessage;
import ac.unindra.roemah_duren_api.dto.request.PagingRequest;
import ac.unindra.roemah_duren_api.dto.request.StockRequest;
import ac.unindra.roemah_duren_api.dto.response.StockResponse;
import ac.unindra.roemah_duren_api.entity.Branch;
import ac.unindra.roemah_duren_api.entity.Product;
import ac.unindra.roemah_duren_api.entity.Stock;
import ac.unindra.roemah_duren_api.entity.TransactionDetail;
import ac.unindra.roemah_duren_api.repository.StockRepository;
import ac.unindra.roemah_duren_api.service.BranchService;
import ac.unindra.roemah_duren_api.service.ProductService;
import ac.unindra.roemah_duren_api.service.StockService;
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

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;
    private final ProductService productService;
    private final BranchService branchService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StockResponse create(StockRequest request) {
        log.info("Start create stock: {}", System.currentTimeMillis());
        Product product = productService.getById(request.getProductId());
        Branch branch = branchService.getById(request.getBranchId());

        if (stockRepository.existsByProductAndBranch(product, branch)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ResponseMessage.ERROR_DUPLICATE_DATA);
        }

        Stock stock = Stock.builder()
                .product(product)
                .stock(request.getStock())
                .branch(branch)
                .build();
        stockRepository.saveAndFlush(stock);

        log.info("End create stock: {}", System.currentTimeMillis());
        return toResponse(stock);
    }

    @Override
    public Stock create(Stock stock) {
        log.info("Start create stock: {}", System.currentTimeMillis());
        stockRepository.saveAndFlush(stock);
        log.info("End create stock: {}", System.currentTimeMillis());
        return stock;
    }

    @Transactional(readOnly = true)
    @Override
    public StockResponse getOne(String id) {
        log.info("Start getOne stock: {}", System.currentTimeMillis());
        Stock stock = stockRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
        log.info("End getOne stock: {}", System.currentTimeMillis());
        return toResponse(stock);
    }

    @Override
    public List<StockResponse> getAll() {
        return stockRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public Page<StockResponse> getAll(PagingRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return stockRepository.findAll(Specification.where(StockRepository.hasSearchQuery(request.getQuery())), pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public Stock getById(String id) {
        log.info("Start getOne stock: {}", System.currentTimeMillis());
        Stock stock = stockRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
        log.info("End getOne stock: {}", System.currentTimeMillis());
        return stock;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StockResponse update(StockRequest request) {
        log.info("Start update stock: {}", System.currentTimeMillis());
        getById(request.getId());
        Product product = productService.getById(request.getProductId());
        Branch branch = branchService.getById(request.getBranchId());
        Stock stock = Stock.builder()
                .id(request.getId())
                .product(product)
                .stock(request.getStock())
                .branch(branch)
                .updatedDate(LocalDateTime.now())
                .build();
        stockRepository.saveAndFlush(stock);
        log.info("End update stock: {}", System.currentTimeMillis());
        return toResponse(stock);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Stock update(Stock stock) {
        log.info("Start update stock: {}", System.currentTimeMillis());
        Stock currentStock = getById(stock.getId());
        currentStock.setProduct(stock.getProduct());
        currentStock.setStock(stock.getStock());
        currentStock.setBranch(stock.getBranch());
        currentStock.setUpdatedDate(LocalDateTime.now());
        stockRepository.saveAndFlush(currentStock);
        log.info("End update stock: {}", System.currentTimeMillis());
        return currentStock;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        log.info("Start delete stock: {}", System.currentTimeMillis());
        Stock stock = getById(id);
        stockRepository.delete(stock);
        log.info("End delete stock: {}", System.currentTimeMillis());
    }

    private StockResponse toResponse(Stock stock) {
        DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String format = stock.getUpdatedDate().format(simpleDateFormat);
        return StockResponse.builder()
                .id(stock.getId())
                .stock(stock.getStock())
                .product(stock.getProduct().toResponse())
                .branch(stock.getBranch().toResponse())
                .updatedDate(format)
                .transactionDetails(stock.getTransactionDetails() != null ? stock.getTransactionDetails().stream().map(TransactionDetail::toResponse).toList() : Collections.emptyList())
                .build();
    }
}
