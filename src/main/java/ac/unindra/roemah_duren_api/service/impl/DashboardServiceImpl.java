package ac.unindra.roemah_duren_api.service.impl;

import ac.unindra.roemah_duren_api.constant.TransactionType;
import ac.unindra.roemah_duren_api.dto.response.DashboardResponse;
import ac.unindra.roemah_duren_api.dto.response.TransactionDetailResponse;
import ac.unindra.roemah_duren_api.dto.response.TransactionResponse;
import ac.unindra.roemah_duren_api.repository.CustomerRepository;
import ac.unindra.roemah_duren_api.repository.ProductRepository;
import ac.unindra.roemah_duren_api.repository.TransactionRepository;
import ac.unindra.roemah_duren_api.service.DashboardService;
import ac.unindra.roemah_duren_api.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;

    @Override
    public DashboardResponse getDashboardInfo() {
        long productCount = productRepository.count();
        long customerCount = customerRepository.count();

        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);

        long transactionCount = transactionRepository.countTransactionsToday(startOfDay, endOfDay);
        List<TransactionResponse> transactionResponses = transactionService.getAll();

        int totalRevenue = 0;
        int totalExpend = 0;

        for (TransactionResponse transactionResponse : transactionResponses) {
            if (transactionResponse.getTransactionType().equals(TransactionType.SALE.getType())) {
                totalRevenue += transactionResponse.getTotalPrice();
            } else if (transactionResponse.getTransactionType().equals(TransactionType.INBOUND.getType())) {
                totalExpend += transactionResponse.getTotalPrice();
            }
        }

        return DashboardResponse.builder()
                .totalProducts(productCount)
                .totalCustomers(customerCount)
                .totalTransactionsPerDay(transactionCount)
                .totalRevenue(totalRevenue)
                .totalExpend(totalExpend)
                .transactions(transactionResponses)
                .build();
    }
}
