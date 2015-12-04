package com.morgane.painauchocolat.model;

import android.os.Parcel;
import android.os.Parcelable;

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
public class Contributor extends Model implements Parcelable {

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

    public Contributor(Parcel in){
        this.name = in.readString();
        this.sessionNumber = in.readInt();
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
     * Method to know if there is contributor registered in the application, or if
     * the application has never been used yet.
     * @return True if there is contributor in the database, false otherwise.
     */
    public static boolean isThereContributors() {
        return new Select()
                .from(Contributor.class)
                .execute()
                .size() > 0;
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

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.sessionNumber);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Contributor createFromParcel(Parcel in) {
            return new Contributor(in);
        }

        public Contributor[] newArray(int size) {
            return new Contributor[size];
        }
    };
}
