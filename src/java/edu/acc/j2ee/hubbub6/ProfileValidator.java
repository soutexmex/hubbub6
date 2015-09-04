package edu.acc.j2ee.hubbub6;

public class ProfileValidator {
    public static boolean isValid(ProfileBean p) {
        if (p.getPassword1().length() > 0 && !p.getPassword1().matches("^[^'\"&<>]{6,15}$"))
            return false;
        if (!p.getPassword2().equals(p.getPassword1()))
            return false;
        if (!p.getEmail().matches("^[\\w-.]+@[\\w-.]+$"))
            return false;
        if (!p.getZip().matches("^\\d{5}(-\\d{4})?"))
            return false;
        if (!(p.getBiography().length() > 0) || !(p.getBiography().length() <= 1000))
            return false;
        return true;
    }
}
