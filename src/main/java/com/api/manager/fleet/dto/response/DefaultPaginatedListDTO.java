package com.api.manager.fleet.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.GsonBuilder;

import java.util.Collection;
import java.util.List;

public class DefaultPaginatedListDTO<T> {

    private Long lastRow;

    private List<T> rows;

    public DefaultPaginatedListDTO(Long lastRow, List<T> rows) {
        this.lastRow = lastRow;
        this.rows = rows;
    }

    public String toJson(){
        return new GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }

    public Long getTotalCount() {
        return this.lastRow;
    }

    public List<T> getItems() {
        return this.rows;
    }
}
