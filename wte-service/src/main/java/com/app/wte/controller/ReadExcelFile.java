package com.app.wte.controller;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ReadExcelFile {/*

    private static final String PROPERTY_EXCEL_SOURCE_FILE_PATH = "excel.to.database.job.source.file.path";

    @Bean
    ItemReader<CsvDTO> excelStudentReader(Environment environment) {
        PoiItemReader<CsvDTO> reader = new PoiItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new ClassPathResource(environment.getRequiredProperty(PROPERTY_EXCEL_SOURCE_FILE_PATH)));
        reader.setRowMapper(excelRowMapper());
        return reader;
    }

    private RowMapper<CsvDTO> excelRowMapper() {
        BeanWrapperRowMapper<CsvDTO> rowMapper = new BeanWrapperRowMapper<>();
        rowMapper.setTargetType(CsvDTO.class);
        return rowMapper;
    }


*/}
