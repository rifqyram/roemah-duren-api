package ac.unindra.roemah_duren_api.service.impl;

import ac.unindra.roemah_duren_api.constant.ResponseMessage;
import ac.unindra.roemah_duren_api.dto.request.BranchRequest;
import ac.unindra.roemah_duren_api.dto.request.PagingRequest;
import ac.unindra.roemah_duren_api.dto.response.BranchResponse;
import ac.unindra.roemah_duren_api.entity.Branch;
import ac.unindra.roemah_duren_api.repository.BranchRepository;
import ac.unindra.roemah_duren_api.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {
    private final BranchRepository branchRepository;

    @Override
    public BranchResponse create(BranchRequest request) {
        Branch branch = branchRepository.saveAndFlush(Branch.builder()
                .name(request.getName())
                .address(request.getAddress())
                .mobilePhoneNo(request.getMobilePhoneNo())
                .build());
        return convertBranchToBranchResponse(branch);
    }

    @Override
    public BranchResponse getOne(String id) {
        return convertBranchToBranchResponse(getById(id));
    }

    @Override
    public Branch getById(String id) {
        return branchRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
    }

    @Override
    public Page<BranchResponse> getPagination(PagingRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Branch> branches = branchRepository.findAll(pageable);
        return branches.map(this::convertBranchToBranchResponse);
    }

    @Override
    public List<BranchResponse> getAll() {
        return branchRepository.findAll().stream().map(this::convertBranchToBranchResponse).toList();
    }

    @Override
    public BranchResponse update(BranchRequest request) {
        getById(request.getId());
        Branch branch = branchRepository.saveAndFlush(Branch.builder()
                .id(request.getId())
                .name(request.getName())
                .address(request.getAddress())
                .mobilePhoneNo(request.getMobilePhoneNo())
                .build());
        return convertBranchToBranchResponse(branch);
    }

    @Override
    public void deleteById(String id) {
        Branch branch = getById(id);
        branchRepository.delete(branch);
    }

    private BranchResponse convertBranchToBranchResponse(Branch branch) {
        return BranchResponse.builder()
                .id(branch.getId())
                .name(branch.getName())
                .address(branch.getAddress())
                .mobilePhoneNo(branch.getMobilePhoneNo())
                .build();
    }
}
