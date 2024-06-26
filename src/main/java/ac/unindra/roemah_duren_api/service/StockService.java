package ac.unindra.roemah_duren_api.service;

import ac.unindra.roemah_duren_api.dto.request.PagingRequest;
import ac.unindra.roemah_duren_api.dto.request.StockRequest;
import ac.unindra.roemah_duren_api.dto.response.StockResponse;
import ac.unindra.roemah_duren_api.entity.Stock;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StockService {
    StockResponse create(StockRequest request);
    Stock create(Stock request);
    StockResponse getOne(String id);
    List<StockResponse> getAll();
    Page<StockResponse> getAll(PagingRequest request);
    Stock getById(String id);
    StockResponse update(StockRequest request);
    Stock update(Stock stock);
    void delete(String id);

}
