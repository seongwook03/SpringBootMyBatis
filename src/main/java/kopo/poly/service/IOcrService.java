package kopo.poly.service;

import kopo.poly.dto.OcrDTO;

public interface IOcrService {

    OcrDTO getReadforImageText(OcrDTO pDTO) throws Exception;
}
