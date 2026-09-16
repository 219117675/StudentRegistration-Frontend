package za.ac.cput.service;

public final class Session {

    private static LoginResponse currentUser;

    private Session() {
    }

    public static void setCurrentUser(
            LoginResponse user) {

        currentUser = user;
    }

    public static LoginResponse getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null
                && currentUser.isSuccess();
    }

    public static String getRole() {

        if (currentUser == null) {
            return null;
        }

        return currentUser.getRole();
    }

    public static Integer getPersonId() {

        if (currentUser == null) {
            return null;
        }

        return currentUser.getPersonId();
    }

    public static String getEmail() {

        if (currentUser == null) {
            return null;
        }

        return currentUser.getEmail();
    }

    public static String getFirstName() {

        if (currentUser == null) {
            return null;
        }

        return currentUser.getFirstName();
    }

    public static String getLastName() {

        if (currentUser == null) {
            return null;
        }

        return currentUser.getLastName();
    }

    public static void logout() {
        currentUser = null;
    }
}