package org.asciidocgenerator.domain.files;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

import javax.annotation.Resource;
import javax.enterprise.context.Dependent;

import org.asciidocgenerator.Logged;

@Dependent
@Logged
public class ExtractArchiveService {

	@Resource(lookup = "java:global/htmldirectory")
	private String baseDirectory;

	public Path extract(Path source, String subdirectory) throws IOException {
		try (FileSystem zipFileSystem = FileSystems.newFileSystem(source, null)) {
			final Path root = zipFileSystem.getPath("/");

			Path destination = Files.createDirectories(Paths.get(baseDirectory, subdirectory));
			deleteDirectoryIfExists(destination);
			Unzip unzip = new Unzip(destination);

			Files.walkFileTree(root, unzip);

			return destination;
		}
	}

	void deleteDirectoryIfExists(Path directory) throws IOException {
		if (Files.notExists(directory)) {
			return;
		}

		Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	static class Unzip
			extends SimpleFileVisitor<Path> {

		private final Path directory;

		public Unzip(Path directory) {
			this.directory = directory;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			if (!isCommitFolder(dir)) {
				final Path dirToCreate = Paths.get(directory.toString(), removeCommitFolderFromFile(dir).toString());
				if (Files.notExists(dirToCreate)) {
					Files.createDirectory(dirToCreate);
				}
			}
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			if (!isCommitFolder(file)) {
				final Path destFile = Paths.get(directory.toString(), removeCommitFolderFromFile(file).toString());
				Files.copy(file, destFile, StandardCopyOption.REPLACE_EXISTING);
			}
			return FileVisitResult.CONTINUE;
		}

		Path removeCommitFolderFromFile(Path file) {
			if (file.getNameCount() < 1) {
				return file;
			}
			return file.subpath(1, file.getNameCount());
		}

		boolean isCommitFolder(Path file) {
			return file.getNameCount() == 1;
		}
	}
}
