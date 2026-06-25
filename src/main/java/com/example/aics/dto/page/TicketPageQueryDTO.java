package com.example.aics.dto.page;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TicketPageQueryDTO extends PageQueryDTO{
    private String status;
    private String keyword;
}
