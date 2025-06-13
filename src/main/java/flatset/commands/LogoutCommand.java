package flatset.commands;

import flatset.Flat;
import flatset.auth.User;

import java.util.HashSet;

public class LogoutCommand implements Command {

    @Override
    public void execute(HashSet<Flat> flatSet, String argument, User currentUser) {
        System.out.println("User " + (currentUser != null ? currentUser.getUsername() : "unknown") + " logged out.");
    }
}
