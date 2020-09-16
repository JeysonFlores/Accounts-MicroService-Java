import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dieselpoint.norm.Database;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.*;
import java.util.Date;
import java.util.List;
import static spark.Spark.*;

public class main {
    public static void main(String[] args) throws JsonProcessingException {

        Database db = new Database();
        db.setJdbcUrl("jdbc:mysql://localhost:3306/accountsdb?useSSL=false");
        db.setUser("admin");
        db.setPassword("password");

        post("/signup", (request, response) -> {
                try{
                    User newuser = new User();
                    JSONObject reqjson = new JSONObject(request.body());
                    newuser.name = reqjson.get("name").toString();
                    newuser.email = reqjson.get("email").toString();
                    newuser.password = reqjson.get("password").toString();
                    db.insert(newuser);
                    return  new JSONObject("{ error: '0', message: User registered }");
                }
                catch (Exception e){
                    return new JSONObject("{ error: '1', message: Invalid data }");
                }
        });

        get("/login", (request, response) -> {
            try{
                Algorithm algorithm = Algorithm.HMAC256("asdasdasdasdads323f");
                JSONObject reqjson = new JSONObject(request.body());
                List<User> query = db.where("email=? and password=?", reqjson.get("email"),reqjson.get("password")).results(User.class);
                User nu = query.get(0);
                String token = JWT.create()
                        .withClaim("exp", new Date(new Date().getTime() + (50 * 60000)))
                        .withClaim("id", nu.id)
                        .withClaim("email", nu.email)
                        .sign(algorithm);
                return new JSONObject("{ error: '0', token: "+token+" }");
            }
            catch (Exception e){
                return new JSONObject("{ error: '1', message: Invalid data }");
            }
        });

    }
}
// ---------------------------- Jeyson Antonio Flores Deras - Sept 2020 -----------------------
