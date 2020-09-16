import org.ietf.jgss.*;
import javax.persistence.*;
@Table(name="user")
public class User {

    @Id
    @GeneratedValue
    public int id;

    public String name;

    public String email;

    public String password;

}
