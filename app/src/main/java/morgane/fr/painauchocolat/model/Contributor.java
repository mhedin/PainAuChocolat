package morgane.fr.painauchocolat.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by morgane.hedin on 25/08/2015.
 */
@Table(name = "Contributors")
public class Contributor extends Model {

    @Column(name = "Name")
    public String name;

    @Column(name = "BroughtNumber")
    public int broughtNumber;

    public Contributor() {

    }

    public Contributor(String _name) {
        name = _name;
        broughtNumber = 0;
    }

    public static List<Contributor> getAll() {
        return new Select()
                .from(Contributor.class)
                .orderBy("Name ASC")
                .execute();
    }

    public static Contributor getRandomBringer(int broughtNumber) {
        return new Select()
                .from(Contributor.class)
                .where("broughtNumber = ?", broughtNumber)
                .orderBy("RANDOM()")
                .executeSingle();
    }

    public static int getMinimumBroughtNumber() {
        Contributor contributor = new Select()
                .from(Contributor.class)
                .orderBy("broughtNumber ASC")
                .executeSingle();

        return contributor != null ? contributor.broughtNumber : 0;
    }
}
