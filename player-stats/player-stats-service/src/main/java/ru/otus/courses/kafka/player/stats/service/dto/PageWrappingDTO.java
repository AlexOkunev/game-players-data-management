package ru.otus.courses.kafka.player.stats.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@Schema(title = "Page", description = "Part of the data set")
public class PageWrappingDTO<T> {

  private Page<T> page;

  @Schema(title = "Total pages count")
  public int getTotalPages() {
    return page.getTotalPages();
  }

  @Schema(title = "Page size")
  public int getSize() {
    return page.getSize();
  }

  @Schema(title = "Total elements")
  public long getTotalElements() {
    return page.getTotalElements();
  }

  @Schema(title = "Page number")
  public int getNumber() {
    return page.getNumber();
  }

  @Schema(title = "Content", description = "Elements that belong to current page")
  public List<T> getContent() {
    return page.getContent();
  }
}
