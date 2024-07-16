package ac.unindra.roemah_duren_api.service.impl;

import ac.unindra.roemah_duren_api.constant.ResponseMessage;
import ac.unindra.roemah_duren_api.constant.TransactionType;
import ac.unindra.roemah_duren_api.dto.request.PagingRequest;
import ac.unindra.roemah_duren_api.dto.request.TransactionDetailRequest;
import ac.unindra.roemah_duren_api.dto.request.TransactionRequest;
import ac.unindra.roemah_duren_api.dto.response.TransactionDetailResponse;
import ac.unindra.roemah_duren_api.dto.response.TransactionResponse;
import ac.unindra.roemah_duren_api.entity.*;
import ac.unindra.roemah_duren_api.repository.TransactionRepository;
import ac.unindra.roemah_duren_api.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final CustomerService customerService;
    private final BranchService branchService;
    private final StockService stockService;
    private final AdminService adminService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse create(TransactionRequest request) {
        log.info("Start create transaction : {}", System.currentTimeMillis());
        Transaction transaction = new Transaction();

        Admin admin = adminService.findByContext();

        if (request.getCustomerId() != null) {
            Customer customer = customerService.getById(request.getCustomerId());
            transaction.setCustomer(customer);
        }

        Branch targetBranch = null;
        if (request.getTargetBranchId() != null) {
            if (!TransactionType.TRANSFER.equals(TransactionType.fromString(request.getTransactionType())))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer Cabang hanya bisa dilakukan pada transaksi transfer");
            targetBranch = branchService.getById(request.getTargetBranchId());
            transaction.setTargetBranch(targetBranch);
        }

        Branch branch = branchService.getById(request.getBranchId());
        transaction.setAdmin(admin);
        transaction.setBranch(branch);
        transaction.setTransDate(LocalDateTime.now());
        transaction.setTransactionType(TransactionType.fromString(request.getTransactionType()));
        transactionRepository.saveAndFlush(transaction);

        List<TransactionDetail> transactionDetails = new ArrayList<>();
        for (TransactionDetailRequest transactionDetail : request.getTransactionDetails()) {
            TransactionDetail detail = new TransactionDetail();
            Stock stock = stockService.getById(transactionDetail.getStockId());
            detail.setStock(stock);

            if (transaction.getTransactionType().equals(TransactionType.INBOUND)) {
                stock.setStock(stock.getStock() + transactionDetail.getQty());
                stockService.update(stock);
            } else if (transaction.getTransactionType().equals(TransactionType.SALE) || transaction.getTransactionType().equals(TransactionType.TRANSFER)) {
                stock.setStock(stock.getStock() - transactionDetail.getQty());
                stockService.update(stock);
            }

            if (targetBranch != null && transaction.getTransactionType().equals(TransactionType.TRANSFER)) {
                Optional<Stock> targetBranchStock = targetBranch.getStocks().stream().filter(s -> s.getProduct().getId().equals(stock.getProduct().getId())).findFirst();
                if (targetBranchStock.isPresent()) {
                    Stock branchStock = targetBranchStock.get();
                    branchStock.setStock(branchStock.getStock() + transactionDetail.getQty());
                    stockService.update(branchStock);
                } else {
                    Stock newStock = Stock.builder()
                            .product(stock.getProduct())
                            .branch(targetBranch)
                            .stock(transactionDetail.getQty())
                            .build();
                    stockService.create(newStock);
                }
            }

            detail.setPrice(transactionDetail.getPrice());
            detail.setQty(transactionDetail.getQty());
            detail.setTransaction(transaction);
            transactionDetails.add(detail);
        }
        transaction.setTransactionDetails(transactionDetails);
        transactionRepository.saveAndFlush(transaction);

        log.info("End create transaction : {}", System.currentTimeMillis());
        return toTransactionResponse(transaction);
    }

    @Override
    public Page<TransactionResponse> getPage(PagingRequest request) {
        log.info("Start Get transaction page : {}", System.currentTimeMillis());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.Direction.DESC, "transDate");
        Specification<Transaction> specification = Specification.where(TransactionRepository.hasSearchQuery(request.getQuery()));
        Page<Transaction> transactions = transactionRepository.findAll(specification, pageable);
        Page<TransactionResponse> transactionResponses = transactions.map(this::toTransactionResponse);
        log.info("End Get transaction page : {}", System.currentTimeMillis());
        return transactionResponses;
    }

    @Override
    public List<TransactionResponse> getAll() {
        log.info("Start Get all transaction : {}", System.currentTimeMillis());
        List<Transaction> transactions = transactionRepository.findAll();
        List<TransactionResponse> transactionResponses = transactions.stream()
                .map(this::toTransactionResponse)
                .toList();
        log.info("End Get all transaction : {}", System.currentTimeMillis());
        return transactionResponses;
    }

    @Override
    public List<TransactionResponse> getAll(LocalDateTime startDate, LocalDateTime endDate, String branchId, String transactionType) {
        log.info("Start Get all transaction by branch id : {}", System.currentTimeMillis());

        if (branchId != null) {
            List<TransactionResponse> list = transactionRepository.findAllByTransDateBetweenAndBranch_IdAndTransactionType(startDate, endDate, branchId, TransactionType.fromString(transactionType))
                    .stream().map(this::toTransactionResponse).toList();
            log.info("End Get all transaction by branch id : {}", System.currentTimeMillis());
            return list;
        }
        List<TransactionResponse> list = transactionRepository.findAllByTransDateBetweenAndTransactionType(startDate, endDate, TransactionType.fromString(transactionType))
                .stream().map(this::toTransactionResponse).toList();
        log.info("End Get all transaction by branch id : {}", System.currentTimeMillis());
        return list;
    }

    @Override
    public TransactionResponse get(String id) {
        log.info("Start Get transaction by id : {}", System.currentTimeMillis());
        TransactionResponse transactionResponse = transactionRepository.findById(id)
                .map(this::toTransactionResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
        log.info("End Get transaction by id : {}", System.currentTimeMillis());
        return transactionResponse;
    }

    @Override
    public Transaction getById(String id) {
        log.info("Start Get transaction by id : {}", System.currentTimeMillis());
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
        log.info("End Get transaction by id : {}", System.currentTimeMillis());
        return transaction;
    }

    @Override
    public List<TransactionDetailResponse> getDetail(String id) {
        log.info("Start Get transaction detail by id : {}", System.currentTimeMillis());
        List<TransactionDetailResponse> transactionDetailResponses = transactionRepository.findById(id)
                .map(transaction -> transaction.getTransactionDetails().stream()
                        .map(TransactionDetail::toResponse)
                        .toList())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
        log.info("End Get transaction detail by id : {}", System.currentTimeMillis());
        return transactionDetailResponses;
    }

    private TransactionResponse toTransactionResponse(Transaction transaction) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = dtf.format(transaction.getTransDate());

        long totalPrice = 0;

        if (transaction.getTransactionType().equals(TransactionType.SALE) || transaction.getTransactionType().equals(TransactionType.INBOUND)) {
            totalPrice = transaction.getTransactionDetails().stream()
                    .mapToLong(detail -> detail.getPrice() * detail.getQty())
                    .sum();
        }

        List<TransactionDetailResponse> transactionDetailResponses = new ArrayList<>();
        for (TransactionDetail transactionDetail : transaction.getTransactionDetails()) {
            transactionDetailResponses.add(transactionDetail.toResponse());
        }

        return TransactionResponse.builder()
                .id(transaction.getId())
                .admin(transaction.getAdmin().toAdminResponse())
                .transDate(date)
                .customer(transaction.getCustomer() != null ? transaction.getCustomer().toResponse() : null)
                .branch(transaction.getBranch().toResponse())
                .targetBranch(transaction.getTargetBranch() != null ? transaction.getTargetBranch().toResponse() : null)
                .transactionType(transaction.getTransactionType().getType())
                .transactionDetails(transactionDetailResponses)
                .totalPrice(totalPrice)
                .build();
    }
}
