package ac.unindra.roemah_duren_api.service.impl;

import ac.unindra.roemah_duren_api.constant.ResponseMessage;
import ac.unindra.roemah_duren_api.dto.request.BranchRequest;
import ac.unindra.roemah_duren_api.dto.request.PagingRequest;
import ac.unindra.roemah_duren_api.dto.response.BranchResponse;
import ac.unindra.roemah_duren_api.entity.Branch;
import ac.unindra.roemah_duren_api.entity.Stock;
import ac.unindra.roemah_duren_api.repository.BranchRepository;
import ac.unindra.roemah_duren_api.service.BranchService;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BranchServiceImpl implements BranchService {
    private final BranchRepository branchRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BranchResponse create(BranchRequest request) {
        log.info("Start create : {}", System.currentTimeMillis());
        Branch branch = branchRepository.saveAndFlush(Branch.builder()
                .code(request.getCode())
                .name(request.getName())
                .address(request.getAddress())
                .mobilePhoneNo(request.getMobilePhoneNo())
                .build());
        log.info("End create : {}", System.currentTimeMillis());
        return convertBranchToBranchResponse(branch);
    }

    @Transactional(readOnly = true)
    @Override
    public BranchResponse getOne(String id) {
        log.info("Start getOne : {}", System.currentTimeMillis());
        BranchResponse response = convertBranchToBranchResponse(getById(id));
        log.info("End getOne : {}", System.currentTimeMillis());
        return response;
    }

    @Transactional
    @Override
    public Branch getById(String id) {
        log.info("Start getById : {}", System.currentTimeMillis());
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
        log.info("End getById : {}", System.currentTimeMillis());
        return branch;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BranchResponse> getPagination(PagingRequest request) {
        log.info("Start getPagination : {}", System.currentTimeMillis());
        Specification<Branch> branchSpecification = BranchRepository.hasSearchQuery(request.getQuery());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Branch> branches = branchRepository.findAll(branchSpecification, pageable);
        log.info("End getPagination : {}", System.currentTimeMillis());
        return branches.map(this::convertBranchToBranchResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BranchResponse> getAll() {
        log.info("Start getAll : {}", System.currentTimeMillis());
        List<BranchResponse> responses = branchRepository.findAll().stream().map(this::convertBranchToBranchResponse).toList();
        log.info("End getAll : {}", System.currentTimeMillis());
        return responses;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BranchResponse update(BranchRequest request) {
        log.info("Start update : {}", System.currentTimeMillis());
        Branch branch = branchRepository.saveAndFlush(Branch.builder()
                .id(request.getId())
                .code(request.getCode())
                .name(request.getName())
                .address(request.getAddress())
                .mobilePhoneNo(request.getMobilePhoneNo())
                .build());
        log.info("End update : {}", System.currentTimeMillis());
        return convertBranchToBranchResponse(branch);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        log.info("Start deleteById : {}", System.currentTimeMillis());
        Branch branch = getById(id);
        branchRepository.delete(branch);
        log.info("End deleteById : {}", System.currentTimeMillis());
    }

    private BranchResponse convertBranchToBranchResponse(Branch branch) {
        return BranchResponse.builder()
                .id(branch.getId())
                .code(branch.getCode())
                .name(branch.getName())
                .address(branch.getAddress())
                .mobilePhoneNo("0" + branch.getMobilePhoneNo())
                .stocks(branch.getStocks() != null && !branch.getStocks().isEmpty() ? branch.getStocks().stream().map(Stock::toResponse).collect(Collectors.toList()) : Collections.emptyList())
                .build();
    }
}
