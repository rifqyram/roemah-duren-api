package ac.unindra.roemah_duren_api.service;

import ac.unindra.roemah_duren_api.dto.request.AdminRequest;
import ac.unindra.roemah_duren_api.dto.request.PagingRequest;
import ac.unindra.roemah_duren_api.dto.response.AdminResponse;
import ac.unindra.roemah_duren_api.entity.Admin;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminService {
    AdminResponse create(AdminRequest request);
    AdminResponse getOne(String id);
    Admin findByContext();
    Page<AdminResponse> getPage(PagingRequest request);
    List<AdminResponse> getAll();
    Admin getById(String id);
    AdminResponse update(AdminRequest request);
    void delete(String id);
}
