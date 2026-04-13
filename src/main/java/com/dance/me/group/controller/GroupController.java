package com.dance.me.group.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dance.me.common.dto.ApiResponse;
import com.dance.me.group.dto.GroupRequest;
import com.dance.me.group.dto.GroupResponse;
import com.dance.me.group.service.GroupService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/school/{schoolId}")
    public ResponseEntity<ApiResponse<List<GroupResponse>>> getBySchoolId(@PathVariable Long schoolId) {
        return ResponseEntity.ok(ApiResponse.ok(groupService.findBySchoolId(schoolId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GroupResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(groupService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GroupResponse>> create(@Validated @RequestBody GroupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Grupo creado", groupService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GroupResponse>> update(@PathVariable Long id, @Validated @RequestBody GroupRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Grupo actualizado", groupService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable Long id) {
        groupService.deactivate(id);
        return ResponseEntity.ok(ApiResponse.ok("Grupo desactivado", null));
    }
}
