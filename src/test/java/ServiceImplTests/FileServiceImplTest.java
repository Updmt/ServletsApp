package ServiceImplTests;

import org.example.dao.FileDao;
import org.example.model.File;
import org.example.service.impl.FileServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileServiceImplTest {

    @Mock
    private FileDao fileDao;

    @InjectMocks
    private FileServiceImpl fileService;

    @Test
    public void testSaveFile() {
        File file = new File();
        file.setText("Sample text");
        file.setFileName("sample.txt");

        File savedFile = new File(1L, "Sample text", "sample.txt", null);

        when(fileDao.save(file)).thenReturn(savedFile);

        File result = fileService.save(file);

        assertThat(result.getText()).isEqualTo(file.getText());
        assertThat(result.getFileName()).isEqualTo(file.getFileName());
    }

    @Test
    public void testGetFileById() {
        File file = new File();
        file.setId(1L);
        file.setText("Sample text");
        file.setFileName("sample.txt");

        when(fileDao.getById(1L)).thenReturn(file);

        File result = fileService.get(1L);

        assertThat(result.getText()).isEqualTo(file.getText());
        assertThat(result.getFileName()).isEqualTo(file.getFileName());
    }

    @Test
    public void testGetAllFiles() {
        List<File> files = Arrays.asList(
                new File(1L, "Text1", "file1.txt", null),
                new File(2L, "Text2", "file2.txt", null)
        );
        when(fileDao.getAll()).thenReturn(files);

        List<File> result = fileService.getAll();
        assertThat(result).isEqualTo(files);
    }

    @Test
    public void testUpdateFile() {
        File oldFile = new File(1L, "OldText", "oldfile.txt", null);
        File updatedFile = new File(1L, "NewText", "newfile.txt", null);

        when(fileDao.update(oldFile)).thenReturn(updatedFile);

        File result = fileService.update(oldFile);

        assertThat(result.getText()).isEqualTo(updatedFile.getText());
        assertThat(result.getFileName()).isEqualTo(updatedFile.getFileName());
    }

    @Test
    public void testDeleteFile() {
        doNothing().when(fileDao).deleteById(1L);

        fileService.delete(1L);

        verify(fileDao, times(1)).deleteById(1L);
    }
}