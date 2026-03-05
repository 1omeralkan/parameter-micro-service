package com.omeralkan.parameter.service;

import com.omeralkan.parameter.dto.TownDto;
import java.util.List;

public interface TownService {
    // İş Kuralı: Sadece belirli bir şehre ait (Örn: 34 ID'li İstanbul) aktif ilçeleri getir
    List<TownDto> getTownsByCityId(Long cityId);
}