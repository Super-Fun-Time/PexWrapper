package com.blalp.pexwrapper;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class PexWrapper extends JavaPlugin {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// No need to check for permissions here, as we will be executing the command as the player anyways
		if(command.getName().equals("permissionsex")) {
			String lpCommand = "luckperms ";
			// Now translate the pex command into the luckperms style
			lpCommand=translateCommand(args);
			if (lpCommand==null){
				sender.sendMessage(ChatColor.RED + "Unsupported pex to lp mapping");
				showHelp(sender);
				System.out.println(sender.getName() + " attempted to call the pex command " + join(args)
						+ " but not valid mapping exists.");
				return false;
			}
			Bukkit.getServer().dispatchCommand(sender, lpCommand);
			sender.sendMessage("Ran "+lpCommand+" on your behalf.");
			return true;
		}
		return false;
	}

	public static String translateCommand(String[] args){
		String output = "luckperms ";
		// Now translate the pex command into the luckperms style
		if(args.length>=5) {
			if(args[1].equals("user")||args[1].equals("group")){
				if(args[3].equals("add")){
					output+=args[1]+" "+args[2]+" permission set "+translatePermission(args[4]);
				} else if (args[3].equals("remove")){
					output+=args[1]+" "+args[2]+" permission unset "+translatePermission(args[4]);
				} else if (args[3].equals("prefix")) {
					output+=args[1]+" "+args[2]+" meta setprefix "+translatePrefix(args, 4);
				} else if (args[3].equals("suffix")) {
					output+=args[1]+" "+args[2]+" meta setsuffix "+translatePrefix(args, 4);
				} else if (args[3].equals("group")&&args[1].equals("user")&&args.length==6) {
					if(args[4].equals("add")) {
						output+="user "+args[2]+" parent add "+args[5];
					} else if (args[4].equals("set")) {
						output+="user "+args[2]+" parent set "+args[5];
					} else if (args[4].equals("remove")) {
						output+="user "+args[2]+" parent remove "+args[5];
					}
				}
			}
		}
		if (output.equals("luckperms ")){
			return null;
		}
		return output;
	}

	public static void showHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.YELLOW + "Supported mappings are: ");
		sender.sendMessage(ChatColor.YELLOW + "/pex [user/group] <USERNAME> [add/remove] <PERMISSION>");
		sender.sendMessage(ChatColor.YELLOW + "/pex [user/group] <USERNAME> [prefix/suffix] <PREFIX>");
		sender.sendMessage(ChatColor.YELLOW + "/pex user <USERNAME> group [add/set/remove] <GROUP>");
	}

	public static String translatePermission(String permission) {
		if (permission.startsWith("-")) {
			return permission.substring(1) + " false";
		} else {
			return permission;
		}
	}

	public static String translatePrefix(String[] args,int start) {
		if(args[start].startsWith("\"")){
			return join(args,start).replaceAll("$.*?(\".*?\").*^", "\1");
		} else {
			return args[start];
		}
	}


	public static String join(String[] arr) {
		return join(arr,0);
	}
	public static String join(String[] arr,int start) {
		boolean first = true;
		String output = "";
		for (int i=start;i<arr.length;i++) {
			if (first) {
				first = false;
			} else {
				output += " ";
			}
			output += arr[i];
		}
		return output;
	}
}