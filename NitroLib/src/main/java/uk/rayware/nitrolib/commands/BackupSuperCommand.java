package uk.rayware.nitrolib.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.rayware.nitrolib.backup.BackupMain;
import uk.rayware.nitrolib.backup.Compressor;

import static uk.rayware.nitrolib.util.Color.GREEN;
import static uk.rayware.nitrolib.util.Color.RED;

public class BackupSuperCommand extends Command {
	
	private final BackupMain backupMain;
	
	public BackupSuperCommand(BackupMain bm) {
		super("backup");
		this.backupMain = bm;
	}
	
	// will be the general dropbox command,
	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(GREEN("/backup checkinet - checks if the dropbox module is connected onto the internet"));
			sender.sendMessage(GREEN("/backup token <set|show> - allows you to set or show the dropbox token"));
			sender.sendMessage(GREEN("/backup backup - backs up the server"));
			return false;
		}
		String method = args[0].toLowerCase();
		
		if (method.equalsIgnoreCase("backup")) {
			// temporary, testing the compressor class.
			BackupMain.getINSTANCE().MP_Backup(new String[] {"world", "world_nether", "world_the_end"});
			
			sender.sendMessage(GREEN("Backup started!"));
			return true;
		}
		else if (method.equalsIgnoreCase("help")) {
			sender.sendMessage(GREEN("/backup checkinet - checks if the dropbox module is connected onto the internet"));
			sender.sendMessage(GREEN("/backup token <set|show> - allows you to set or show the dropbox token"));
			return false;
		} else if (method.equalsIgnoreCase("checkinet")) {
			if (backupMain.isAuthenticated()) {
				sender.sendMessage(GREEN("The dropbox module is authenticated and connected to dropbox correctly."));
			} else {
				sender.sendMessage(RED("The dropbox module is not authenticated and connected to dropbox."));
			}
			return true;
			
		} else if (method.equalsIgnoreCase("token")) {
			// no, it is /backup token <set|show> <token>
			String option = args[1].toLowerCase();
			
			if (option.equalsIgnoreCase("set")) {
				if (args.length < 3) {
					sender.sendMessage(RED("You must specify a token."));
					return false;
				}
				String token = args[2];
				BackupMain.setACCESS_TOKEN(token);
				sender.sendMessage(GREEN("Successfully set the token."));
				return true;
			} else if (option.equalsIgnoreCase("show")) {
				// can't do class reference as the token may be set to null on boot or changed at runtime. In deployment, it will be null and will have to be defined by a user.
				sender.sendMessage(GREEN("The token is: " + BackupMain.getACCESS_TOKEN()));
				return true;
			} else {
				sender.sendMessage(RED("You must specify a valid option."));
				return false;
			}
		} else {
			sender.sendMessage(RED("You must specify a valid option."));
			return false;
		}
	}
	

	  }
