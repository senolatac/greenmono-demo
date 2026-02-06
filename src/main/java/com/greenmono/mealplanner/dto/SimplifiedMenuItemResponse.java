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
@Schema(description = "Simplified menu item response with day, date, and 3-component meal")
public class SimplifiedMenuItemResponse {

    @Schema(description = "Day of the week in Turkish", example = "Pazartesi")
    private String day;

    @Schema(description = "Date in dd.MM.yyyy format", example = "26.01.2026")
    private String date;

    @Schema(description = "Soup name", example = "Mercimek \u00c7orbas\u0131")
    private String soup;

    @Schema(description = "Main course name", example = "Mant\u0131")
    private String mainCourse;

    @Schema(description = "Side dish name", example = "Pilav")
    private String sideDish;
}
