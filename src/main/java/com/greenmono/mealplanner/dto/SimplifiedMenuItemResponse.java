package com.greenmono.mealplanner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Simplified menu item response with day, date, and meal name")
public class SimplifiedMenuItemResponse {

    @Schema(description = "Day of the week in Turkish", example = "Pazartesi")
    private String day;

    @Schema(description = "Date in dd.MM.yyyy format", example = "26.01.2026")
    private String date;

    @Schema(description = "Meal name", example = "Mantı")
    private String meal;
}
