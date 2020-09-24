package ai.certifai.solution;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileZipping {
    public static void main(String[]args) throws Exception{
        String folderToZip = "C:\\Users\\Faizal\\Documents\\CDLE Bootcamp\\Data\\";
        String zipName = "C:\\Users\\Faizal\\Documents\\CDLE Bootcamp\\File\\AllFaceTest.zip";
        zipFolder(Paths.get(folderToZip), Paths.get(zipName));
    }
    public static void zipFolder(Path sourceFolderPath, Path zipPath) throws Exception {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()));
        Files.walkFileTree(sourceFolderPath, new SimpleFileVisitor<Path>() {
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                zos.putNextEntry(new ZipEntry(sourceFolderPath.relativize(file).toString()));
                Files.copy(file, zos);
                zos.closeEntry();
                return FileVisitResult.CONTINUE;
            }
        });
        zos.close();
    }
}
