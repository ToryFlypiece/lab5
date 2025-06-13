package flatset.commands;

import flatset.Flat;
import flatset.auth.User;

import java.util.Set;

public class LogoutCommand implements Command {

    @Override
    public void execute(Set<Flat> flatSet, String argument, User currentUser) {
        System.out.println("User " + (currentUser != null ? currentUser.getUsername() : "unknown") + " logged out.");
    }
}
