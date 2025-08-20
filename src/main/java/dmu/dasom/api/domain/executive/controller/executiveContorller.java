package dmu.dasom.api.domain.executive.controller;

import dmu.dasom.api.domain.executive.service.executiveService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "EXECUTIVE API", description = "임원진 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/executive")
public class executiveContorller {

    private final executiveService executiveService;
}
