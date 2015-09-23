package com.morgane.painauchocolat.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * This class is the model of the database table in which the contributors are registered.
 * It contains the name of the contributor and the session number of the user.
 *
 * Created by morgane.hedin on 25/08/2015.
 */
@Table(name = "Contributors")
public class Contributor extends Model {

    @Column(name = "Name")
    public String name;

    @Column(name = "SessionNumber")
    public int sessionNumber;

    public Contributor() {

    }

    public Contributor(String _name) {
        name = _name;
        sessionNumber = 0;
    }

    /**
     * Get the list of the contributors who haven't bring the breakfast in this session.
     * @param sessionNumber The number of the current session.
     * @return The list of the contributors who haven't bring the breakfast in this session yet.
     */
    public static List<Contributor> getNotYetContributors(int sessionNumber) {
        return new Select()
                .from(Contributor.class)
                .where("SessionNumber = ?", sessionNumber)
                .orderBy("Name ASC")
                .execute();
    }

    /**
     * Get the list of the contributors who have already bring the breakfast in this session.
     * @param sessionNumber The number of the current session.
     * @return The list of the contributors who have already bring the breakfast in this session.
     */
    public static List<Contributor> getAlreadyContributors(int sessionNumber) {
        return new Select()
                .from(Contributor.class)
                .where("SessionNumber > ?", sessionNumber)
                .orderBy("Name ASC")
                .execute();
    }

    /**
     * Get a random contributor in the ones who haven't bring the breakfast in this session.
     * @param sessionNumber The number of the current session.
     * @return A random contributor who haven't bring the breakfast in this session.
     */
    public static Contributor getRandomBringer(int sessionNumber) {
        return new Select()
                .from(Contributor.class)
                .where("SessionNumber = ?", sessionNumber)
                .orderBy("RANDOM()")
                .executeSingle();
    }

    /**
     * Get the number of the current session, which corresponds to the minimum session number.
     * @return The number of the current session.
     */
    public static int getMinimumSessionNumber() {
        Contributor contributor = new Select()
                .from(Contributor.class)
                .orderBy("SessionNumber ASC")
                .executeSingle();

        return contributor != null ? contributor.sessionNumber : 0;
    }
}
