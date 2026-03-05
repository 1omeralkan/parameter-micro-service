package com.omeralkan.parameter.controller;

import com.omeralkan.parameter.dto.TownDto;
import com.omeralkan.parameter.service.TownService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Bu sınıfın bir REST API uç noktası olduğunu belirtiriz
@RequestMapping("/api/v1/towns") // Tüm ilçe isteklerinin ana kapısı
@RequiredArgsConstructor // Constructor injection ile bağımlılıkları güvenle alır
public class TownController {

    private final TownService townService;

    // GET: http://localhost:8081/api/v1/towns/city/{cityId}
    // Amacı: Sadece seçilen şehrin ilçelerini getirmek
    @GetMapping("/city/{cityId}")
    public ResponseEntity<List<TownDto>> getTownsByCityId(@PathVariable Long cityId) {
        List<TownDto> towns = townService.getTownsByCityId(cityId);

        // İşlem başarılıysa HTTP 200 (OK) statüsüyle veriyi dönüyoruz
        return ResponseEntity.ok(towns);
    }
}