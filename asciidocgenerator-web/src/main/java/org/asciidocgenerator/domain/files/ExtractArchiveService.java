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
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.asciidocgenerator.BaseDirectoryService;
import org.asciidocgenerator.Logged;

@Dependent
@Logged
public class ExtractArchiveService {

	@Inject
	private BaseDirectoryService baseDirectoryService;

	public Path extract(Path source, String subdirectory) throws IOException {
		try (FileSystem zipFileSystem = FileSystems.newFileSystem(source, null)) {
			final Path root = zipFileSystem.getPath("/");

			Path destination = Files.createDirectories(Paths.get(	baseDirectoryService.getBaseDirectory(),
																	subdirectory));
			deleteDirectoryIfExists(destination);
			Unzip unzip = new Unzip(destination);

			Files.walkFileTree(root, unzip);

			return destination;
		}
	}

	public Path copy(Path source, String subdirectory) throws IOException {
		Path destination = Files.createDirectories(Paths.get(baseDirectoryService.getBaseDirectory(), subdirectory));
		deleteDirectoryIfExists(destination);

		Logger	.getLogger("asciidocgenerator-web")
				.log(Level.INFO, "Copying files from {0} to {1}", new Object[] { source, destination });

		Files.walkFileTree(source, new CopyAll(source, destination));

		return destination;
	}

	void deleteDirectoryIfExists(Path directory) throws IOException {
		if (Files.notExists(directory)) {
			return;
		}

		final Logger logger = Logger.getLogger("asciidocgenerator-web");

		Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				logger.log(Level.INFO, MessageFormat.format("Deleted file: {0}", dir));
				return super.preVisitDirectory(dir, attrs);
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				logger.log(Level.WARNING, MessageFormat.format("Failed to visit file:", file), exc);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				logger.log(Level.INFO, MessageFormat.format("Deleted file: {0}", file));
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				logger.log(Level.INFO, MessageFormat.format("Deleted file:{0}", dir));
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

	static class CopyAll
			extends SimpleFileVisitor<Path> {

		private final Path destination;
		private final Path source;

		public CopyAll(Path source, Path directory) {
			this.destination = directory;
			this.source = source;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			final Path dirToCreate = destination.resolve(source.relativize(dir));
			if (Files.notExists(dirToCreate)) {
				Files.createDirectory(dirToCreate);
			}
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			final Path destFile = destination.resolve(source.relativize(file));

			Files.copy(file, destFile, StandardCopyOption.REPLACE_EXISTING);
			return FileVisitResult.CONTINUE;
		}
	}
}
