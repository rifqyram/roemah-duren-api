package ac.unindra.roemah_duren_api.service;

import ac.unindra.roemah_duren_api.dto.request.BranchRequest;
import ac.unindra.roemah_duren_api.dto.request.PagingRequest;
import ac.unindra.roemah_duren_api.dto.response.BranchResponse;
import ac.unindra.roemah_duren_api.entity.Branch;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BranchService {
    BranchResponse create(BranchRequest request);
    BranchResponse getOne(String id);
    Branch getById(String id);
    Page<BranchResponse> getPagination(PagingRequest request);
    List<BranchResponse> getAll();
    BranchResponse update(BranchRequest request);
    void deleteById(String id);
}
