package org.example.dto;

import com.google.gson.annotations.Expose;
import lombok.*;
import org.example.constants.Constants;
import org.example.model.File;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDTO {

    @Expose
    private Long id;
    @Expose
    private String fileName;
    @Expose
    private String downloadLink;

    public static FileDTO fromEntity(File file) {
        return FileDTO.builder()
                .id(file.getId())
                .downloadLink(Constants.SAVE_PATH + file.getFileName())
                .fileName(file.getFileName())
                .build();
    }
}
