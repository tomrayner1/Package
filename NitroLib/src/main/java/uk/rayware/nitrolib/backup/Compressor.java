package uk.rayware.nitrolib.backup;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Compressor {
	private static final int BUFFER_SIZE = 4096;
	
	private static final String[] filesFolders = {
		//"plugins", can't do this due to os permissions, it just goes kablooey and stops the backup (?)
		"world",
		"world_nether",
		"world_the_end",
		"server.properties",
		"bukkit.yml",
		"spigot.yml",
		"paper.yml",
		"paperclip.jar",
		"eula.txt",
		"ops.json",
		"whitelist.json",
		"banned-ips.json",
		"banned-players.json",
		"usercache.json",
		"permissions.yml",
		"config.yml",
		"commands.yml",
		"help.yml",
		"spigot.yml",
		"paper.yml",
		"paperclip.jar",
		"eula.txt",
		"ops.json",
		"whitelist.json",
		"banned-ips.json",
		"banned-players.json",
		"usercache.json",
		"permissions.yml",
		"config.yml",
		"commands.yml",
		"help.yml"
	};
	
	public static String Compress() {
		// no filenames, we will use a pre-defined list of files to compress, all folders and files except for log
		return Compress(filesFolders);
	}
	
	public static String Compress(String[] names) {
		// makes a List<File> for all the files in the names array
		// then calls the other Compress method
		
		String aName = newName();
		
		try {
			TEMPIFY();
		} catch (IOException e) {
			BackupMain.log("Error while tempifying files. Backup failed.");
			e.printStackTrace();
		}
		List<File> files = new ArrayList<>();
		
		for (String name : names) {
			// attempt to open, if not found, log to console and continue, DO not add the file object in this instance
			File file = new File("temp_backup/" + name);
			if (file.exists()) {
				files.add(file);
			} else {
				BackupMain.log("Ignoring '" + "temp_backup/" + name + "' for backup compression as the file or folder does not exist.");
			}
		}
		
		try {
			Zip(files, aName);
		} catch (FileNotFoundException e) {
			BackupMain.log("Could not create backup file.");
			e.printStackTrace();
		}
		
		return aName;
	}
	
	private static void Zip(List<File> files, String zipFilePath) throws FileNotFoundException {
		try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
			for (File file : files) {
				try (FileInputStream fis = new FileInputStream(file)) {
					zos.putNextEntry(new ZipEntry(file.getName()));
					byte[] buffer = new byte[1024];
					int length;
					while ((length = fis.read(buffer)) > 0) {
						zos.write(buffer, 0, length);
					}
					zos.closeEntry();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void TEMPIFY() throws IOException {
		Path targetPath = Paths.get("temp_backup");
		
		// Deletes the contents of temp_backup
		if (Files.exists(targetPath)) {
			try (Stream<Path> paths = Files.walk(targetPath)) {
				paths.sorted(Comparator.reverseOrder())
					.map(Path::toFile)
					.forEach(File::delete);
			}
		}
		
		
		if (!Files.exists(targetPath)) {
			Files.createDirectory(targetPath);
		}
		
		for (String fileFolder : filesFolders) {
			Path sourcePath = Paths.get(fileFolder);
			if (Files.exists(sourcePath)) {
				// copy the file/folder to the target folder
				Files.copy(sourcePath, targetPath.resolve(sourcePath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
			}
		}
	}
	
	private static String newName() {
		long unixTime = System.currentTimeMillis() / 1000L;
		
		return "backup-" + unixTime + ".zip";
	}
}
